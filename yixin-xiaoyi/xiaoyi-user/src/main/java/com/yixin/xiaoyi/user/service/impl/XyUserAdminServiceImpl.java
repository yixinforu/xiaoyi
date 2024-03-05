package com.yixin.xiaoyi.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.core.module.common.dto.DataCountDTO;
import com.yixin.xiaoyi.common.core.module.common.vo.DateVO;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.DateUtils;
import com.yixin.xiaoyi.common.utils.EncryptionUtil;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.user.dao.UserOnlineLogFinder;
import com.yixin.xiaoyi.user.entity.UserOnlineLog;
import com.yixin.xiaoyi.user.enums.RoleEnum;
import com.yixin.xiaoyi.user.model.dto.ClientIpTrendDTO;
import com.yixin.xiaoyi.user.model.dto.UserDTO;
import com.yixin.xiaoyi.user.model.dto.UserDataDTO;
import com.yixin.xiaoyi.user.model.dto.UserInfoDTO;
import com.yixin.xiaoyi.user.model.vo.DisableVo;
import com.yixin.xiaoyi.user.model.vo.EmpowerVO;
import com.yixin.xiaoyi.user.model.vo.XyUserInfoVO;
import com.yixin.xiaoyi.role.dao.XyRoleFinder;
import com.yixin.xiaoyi.role.dao.XyUserRoleFinder;
import com.yixin.xiaoyi.user.dao.XyUserFinder;
import com.yixin.xiaoyi.user.service.SysPermissionService;
import com.yixin.xiaoyi.user.service.XyUserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.role.entity.XyRole;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.StreamUtils;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.role.entity.XyUserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author admin
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class XyUserAdminServiceImpl implements XyUserAdminService {

    private final XyRoleFinder xyRoleFinder;
    private final XyUserRoleFinder xyUserRoleFinder;
    private final XyUserFinder xyUserFinder;
    private final UserOnlineLogFinder userOnlineLogFinder;
    private final SysPermissionService permissionService;
    public static final Long SECOND_TIME = 1000L;


    @Override
    public TableDataInfo<UserDTO> selectPageUserList(XyUserInfoVO user, PageQuery pageQuery) {
        Page<XyUser> page = xyUserFinder.selectPageUserList(user, pageQuery);
        List<XyUser> xyUsers =  page.getRecords();
        //获取用户在线日志
        Map<Long, UserOnlineLog> userIdAndOnlineLogs = handleUserOnlineLogsByDays(30);

        List<UserDTO> userDTOS = new ArrayList<>();
        xyUsers.stream().forEach(it->{
            Boolean disable = StpUtil.isDisable(it.getUserId());
            UserOnlineLog userOnlineLog = userIdAndOnlineLogs.getOrDefault(it.getUserId(),new UserOnlineLog());
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(it.getUserId());
            userDTO.setPhone(EncryptionUtil.decryptKey(it.getEncryptedPhone(),UserConfig.getPhoneSecret()));
            userDTO.setRoles(it.getRoles());
            userDTO.setLastActiveTime(userOnlineLog.getLastRequestTime());
            userDTO.setLastActiveIp(userOnlineLog.getClientIp());
            userDTO.setCreateTime(it.getCreateTime());
            userDTO.setIsDisabled(disable);
            userDTO.setDisableTime(disable?DateUtils.addSeconds(new Date(),(int)StpUtil.getDisableTime(it.getUserId())):null);
            userDTOS.add(userDTO);
        });
        Page<UserDTO> userDTOPage = new Page<>();
        userDTOPage.setTotal(page.getTotal());
        userDTOPage.setRecords(userDTOS);
        return TableDataInfo.build(userDTOPage);
    }

    @Override
    public int empower(EmpowerVO empowerVO) {
        //用户校验
        Optional.ofNullable(xyUserFinder.selectUserById(empowerVO.getUserId())).orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND));
        //先删除用户VIP权限
        XyUserRole userRole = new XyUserRole();
        userRole.setUserId(empowerVO.getUserId());
        userRole.setRoleId(RoleEnum.ROLE_VIP.getValue());
        xyUserRoleFinder.deleteAuthUser(userRole);
        if(empowerVO.getVipDays() == 0){
            //清空权限缓存
            permissionService.deleteUserPermission(empowerVO.getUserId());
            return 1;
        }
        //开始授权
        if(empowerVO.getVipDays() > 0){
            userRole.setExpireTime(DateUtils.addDays(new Date(), empowerVO.getVipDays()));
        }
        int updateNum = xyUserRoleFinder.insertUserRole(userRole);
        //清空权限缓存
        permissionService.deleteUserPermission(empowerVO.getUserId());
        return updateNum;
    }

    @Override
    public int increaseEmpower(EmpowerVO empowerVO) {
        XyUserRole xyUserRole = xyUserRoleFinder.selectUserRoleByUserIdAndRoleId(empowerVO.getUserId(),RoleEnum.ROLE_VIP.getValue());
        if(ObjectUtil.isNotNull(xyUserRole)){
            if(ObjectUtil.isNull(xyUserRole.getExpireTime())){
                //说明vip为永久，不需要进行再授权了
                return 0;
            }
            Integer differentDays = DateUtils.differentDaysByMillisecond(new Date(),xyUserRole.getExpireTime());
            empowerVO.setVipDays(empowerVO.getVipDays()+differentDays);
        }
        return empower(empowerVO);
    }

    @Override
    public void disableUser(DisableVo disableVo) {
        // 先踢下线
        StpUtil.kickout(disableVo.getUserId());
        if(disableVo.getDisableDays() == -1){
            // 永久封禁
            StpUtil.disable(disableVo.getUserId(), disableVo.getDisableDays());
        }else {
            // 再封禁账号 单位：秒
            StpUtil.disable(disableVo.getUserId(), disableVo.getDisableDays() * 24 * 60 * 60);
        }
    }

    @Override
    public void untieDisableUser(Long userId) {
        if(!StpUtil.isDisable(userId)){
            //说明没有被封禁
            return;
        }
        StpUtil.untieDisable(userId);
    }


    /**
     * 获取最近N天的在线日志记录
     * @param day
     * @return
     */
    private Map<Long, UserOnlineLog> handleUserOnlineLogsByDays(int day){
        Long lastTime = 60*60*24L*day;
        Long nowMillis = System.currentTimeMillis()/1000;
        Long time = nowMillis - lastTime;
        List<UserOnlineLog> userOnlineLogs = userOnlineLogFinder.findOnlineLogsByLastLoginTime(new Date(time * SECOND_TIME));
        //因为用户登录的记录分为历史和当天的（而当天的记录只有在晚上23：59分才会同步到数据库）
        //1天前记录
        Map<Long,UserOnlineLog> userIdAndLastRequestTime = userOnlineLogs.stream().collect(Collectors.toMap(UserOnlineLog::getUserId,it->it,(a,b)->b));
        //当天记录
        Map<Long,UserOnlineLog> todayIds = getTodayOnlineUserIds();
        if(CollectionUtils.isEmpty(userIdAndLastRequestTime)){
            userIdAndLastRequestTime = new HashMap<>();
        }
        for(Long item:todayIds.keySet()){
            userIdAndLastRequestTime.put(item,todayIds.getOrDefault(item,null));
        }
        return userIdAndLastRequestTime;
    }

    /**
     * 获取当天登录的用户Id
     *
     * @return
     */
    public Map<Long,UserOnlineLog> getTodayOnlineUserIds(){
        //获取用户请求日志
        Collection<String> keys = RedisUtils.keys(CacheConstants.CLIENT_ONLINE_LOG+"*");
        List<UserOnlineLog> userOnlineLogs = new ArrayList<>();
        for(String key:keys){
            UserOnlineLog userOnlineLog = RedisUtils.getCacheObject(key);
            userOnlineLog.setLastRequestTime(new Date(userOnlineLog.getCurrentRequestTime()));
            userOnlineLogs.add(userOnlineLog);
        }
        return userOnlineLogs.stream().filter(item-> StrUtil.isNotBlank(String.valueOf(item.getUserId()))).collect(Collectors.toMap(UserOnlineLog::getUserId,it->it,(a,b)->b));
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<XyUser> selectUserList(XyUser user) {
        return xyUserFinder.selectUserList(user);
    }


    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public TableDataInfo<XyUser> selectAllocatedList(XyUser user, PageQuery pageQuery) {
        Page<XyUser> page = xyUserFinder.selectAllocatedList(user,pageQuery);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public TableDataInfo<XyUser> selectUnallocatedList(XyUser user, PageQuery pageQuery) {
        List<Long> userIds = xyUserRoleFinder.selectUserIdsByRoleId(user.getRoleId());
        Page<XyUser> page = xyUserFinder.selectUnallocatedList(user, pageQuery,userIds);
        return TableDataInfo.build(page);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public XyUser selectUserByUserName(String userName) {
        return xyUserFinder.selectUserByUserName(userName);
    }

    @Override
    public XyUser selectUserByEncryptedPassword(String encryptedPassword) {
        return xyUserFinder.selectUserByEncryptedPhone(encryptedPassword);
    }

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */


    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public XyUser selectUserById(Long userId) {
        return xyUserFinder.selectUserById(userId);
    }


    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<XyRole> list = xyRoleFinder.selectRolesByUserName(userName);
        if (CollUtil.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return StreamUtils.join(list, XyRole::getRoleName);
    }



    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(XyUser user) {
        boolean exist = xyUserFinder.checkPhoneUnique(user);
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }



    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(XyUser user) {
        if (ObjectUtil.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }



    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(XyUser user) {
        // 新增用户信息
        int rows = xyUserFinder.insert(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(XyUser user) {
        return xyUserFinder.insert(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(XyUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        xyUserRoleFinder.deleteRolesByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        return xyUserFinder.updateById(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds) {
        xyUserRoleFinder.deleteRolesByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(XyUser user) {
        return xyUserFinder.updateById(user);
    }

    @Override
    public int updateUserSecretKey(Long userId, String secretKey) {
        XyUser xyUser = new XyUser();
        xyUser.setUserId(userId);
        return xyUserFinder.updateById(xyUser);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(XyUser user) {
        return xyUserFinder.updateById(user);
    }



    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(XyUser user) {
        return xyUserFinder.updateById(user);
    }



    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(XyUser user) {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (ArrayUtil.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<XyUserRole> list = new ArrayList<>(roleIds.length);
            for (Long roleId : roleIds) {
                XyUserRole ur = new XyUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            xyUserRoleFinder.insertBatch(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        xyUserRoleFinder.deleteRolesByUserId(userId);
        return xyUserFinder.deleteById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(new XyUser(userId));
        }
        List<Long> ids = Arrays.asList(userIds);
        // 删除用户与角色关联
        xyUserRoleFinder.deleteUserByIds(ids);
        return xyUserFinder.deleteBatchIds(ids);
    }

    @Override
    public UserDataDTO findUserCountData() {
        //当天零时
         Date todayZeroDate = getTodayTimeZero();
         //获取用户总数
         Integer currentCount = xyUserFinder.selectUserCountByAfterTime(null);
         //获取当天新增用户
         Integer increaseTodayCount = xyUserFinder.selectUserCountByAfterTime(todayZeroDate);
         //获取VIP总数
         Date currentTime = new Date();
         Integer vipTotal = xyUserRoleFinder.selectUserRoleCountByAfterTime(RoleEnum.ROLE_VIP.getValue(),null,currentTime);
         //获取今日新增VIP数
         Integer increaseVipTodayCount = xyUserRoleFinder.selectUserRoleCountByAfterTime(RoleEnum.ROLE_VIP.getValue(), todayZeroDate,currentTime);

         UserDataDTO userDataDTO = new UserDataDTO();
         userDataDTO.setTodayIpCount(getTodayIpCount());
         userDataDTO.setCurrentActiveIpCount(getActiveIpCount(10));
         userDataDTO.setUserDataCountDTO(new DataCountDTO(currentCount, increaseTodayCount));
         userDataDTO.setVipDataCountDTO(new DataCountDTO(vipTotal,increaseVipTodayCount));
        return userDataDTO;
    }

    @Override
    public List<ClientIpTrendDTO> findClientIpTrendData(DateVO date) {
        Date startDate = DateUtils.dateTime("yyyy-MM-dd", date.getStartDate());
        Date endDate = DateUtils.dateTime("yyyy-MM-dd", date.getEndDate());
        Date currentTime = getTodayTimeZero();
        //获取历史日志数据
        List<UserOnlineLog> userOnlineLogs = userOnlineLogFinder.findOnlineLogsByBetween(startDate,endDate);
        //判断是否包含当天，因为当天的日志数据在缓存里
        if(isDateWithinRange(currentTime,startDate,endDate)){
            userOnlineLogs.addAll(findCacheOnlineLogs(currentTime));
        }
        //按日期进行分组
        Map<String,List<UserOnlineLog>> dateAndOnlineLogs = userOnlineLogs.stream().collect(Collectors.groupingBy(logs->DateUtils.parseDateToStr("yyyy-MM-dd",logs.getCreateTime())));
        List<ClientIpTrendDTO> result = new ArrayList<>();
        // 遍历startDate到endDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        // 循环 startDate 达到 endDate
        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            String dateTimeStr = DateUtils.parseDateToStr("yyyy-MM-dd",calendar.getTime());
            Integer clientIpCount = 0;
            if(CollectionUtils.isNotEmpty(dateAndOnlineLogs.get(dateTimeStr))){
                clientIpCount =  dateAndOnlineLogs.get(dateTimeStr).stream().map(UserOnlineLog::getClientIp).distinct().collect(Collectors.toList()).size();
            }
            result.add(
                ClientIpTrendDTO.builder()
                    .clientIpCount(clientIpCount).dateTime(calendar.getTime()).build()
            );
            // 将 Calendar 的日期增加一天
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    /**
     * 获取缓存日志
     * @return
     */
    public List<UserOnlineLog> findCacheOnlineLogs(Date currentTime){
        //获取缓存在线日志
        Collection<String> cacheKeys = RedisUtils.keys(CacheConstants.CLIENT_ONLINE_LOG+ "*");
        List<UserOnlineLog> userOnlineLogs = new ArrayList<>();
        for(String key:cacheKeys){
            UserOnlineLog userOnlineLog = RedisUtils.getCacheObject(key);
            userOnlineLog.setLastRequestTime(new Date(userOnlineLog.getCurrentRequestTime()));
            userOnlineLog.setCreateTime(currentTime);
            userOnlineLogs.add(userOnlineLog);
        }
        return userOnlineLogs;
    }

    // 判断日期是否在时间区间内的方法（包括等于的情况）
    public static boolean isDateWithinRange(Date date, Date startDate, Date endDate) {
        return !date.before(startDate) && !date.after(endDate);
    }


    public Date getTodayTimeZero() {
        // 获取当前日期的 Calendar 实例
        Calendar calendar = Calendar.getInstance();
        // 将小时、分钟、秒和毫秒设置为零
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当天IP数
     * @return
     */
    public Integer getTodayIpCount(){
        //获取缓存在线日志
        Collection<String> cacheKeys = RedisUtils.keys(CacheConstants.CLIENT_ONLINE_LOG+ "*");
        List<UserOnlineLog> userOnlineLogs = new ArrayList<>();
        cacheKeys.stream().forEach(key->userOnlineLogs.add(RedisUtils.getCacheObject(key)));
        //同一个用户可能在登录前后访问，此次在日志记录中该ip会有两次
        return userOnlineLogs.stream().map(UserOnlineLog::getClientIp).distinct().collect(Collectors.toList()).size();
    }

    /**
     * 获取近N分钟内的活跃IP
     * @param minutes
     * @return
     */
    public Integer getActiveIpCount(int minutes){
        //获取缓存在线日志
        Collection<String> cacheKeys = RedisUtils.keys(CacheConstants.CLIENT_ONLINE_LOG+ "*");
        List<UserOnlineLog> activeIpLogs = new ArrayList<>();
        for(String key:cacheKeys){
            UserOnlineLog userOnlineLog = RedisUtils.getCacheObject(key);
            Date lastRequestTime = new Date(userOnlineLog.getCurrentRequestTime());
            //只获取最近N分钟内活跃的IP
            if(lastRequestTime.after(DateUtils.addMinutes(new Date(),-minutes))){
                activeIpLogs.add(userOnlineLog);
            }
        }
        //同一个用户可能在登录前后访问，此次在日志记录中该ip会有两次
        return activeIpLogs.stream().map(UserOnlineLog::getClientIp).distinct().collect(Collectors.toList()).size();
    }

}
