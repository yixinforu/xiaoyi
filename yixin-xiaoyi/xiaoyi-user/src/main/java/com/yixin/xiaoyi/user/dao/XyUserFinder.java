package com.yixin.xiaoyi.user.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.utils.PhoneUtil;
import com.yixin.xiaoyi.role.dao.XyUserRoleFinder;
import com.yixin.xiaoyi.user.enums.RoleEnum;
import com.yixin.xiaoyi.user.model.vo.XyUserInfoVO;
import com.yixin.xiaoyi.common.utils.EncryptionUtil;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.user.dao.mapper.XyUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/20 12:42
 */
@Repository
@RequiredArgsConstructor
public class XyUserFinder {

    private final XyUserMapper xyUserMapper;
    private final XyUserRoleFinder xyUserRoleFinder;




    public List<XyUser> selectUserListByDate(Date date){
        return xyUserMapper.selectUserList(new LambdaQueryWrapper<XyUser>().apply("DATE(create_time) <= {0}",date));
    }

    public Page<XyUser> selectPageUserList(XyUserInfoVO user, PageQuery pageQuery) {
        return xyUserMapper.selectPageUserList(pageQuery.build(), this.buildUserInfoWrapper(user));
    }

    /**
     * 获取指定时间之后注册的用户
     * @param compareDate
     * @return
     */
    public Integer selectUserCountByAfterTime(Date compareDate){
        QueryWrapper<XyUser> wrapper = Wrappers.query();
        wrapper.gt(ObjectUtil.isNotNull(compareDate),"create_time",compareDate);
        return Integer.valueOf(xyUserMapper.selectCount(wrapper).toString());
    }

    public List<XyUser> selectUserList(XyUser user){
        return xyUserMapper.selectUserList(this.buildQueryWrapper(user));
    }

    public Page<XyUser> selectAllocatedList(XyUser user, PageQuery pageQuery) {
        QueryWrapper<XyUser> wrapper = Wrappers.query();
        wrapper
            .eq(ObjectUtil.isNotNull(user.getRoleId()), "r.role_id", user.getRoleId())
            .eq(StringUtils.isNotBlank(user.getPhone()), "u.encrypted_phone", EncryptionUtil.encryptKey(user.getPhone(), UserConfig.getPhoneSecret()))
            .eq(StringUtils.isNotBlank(user.getStatus()), "u.status", user.getStatus());
        return xyUserMapper.selectAllocatedList(pageQuery.build(), wrapper);
    }


    public Page<XyUser> selectUnallocatedList(XyUser user, PageQuery pageQuery, Collection<Long> userIds) {
        QueryWrapper<XyUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
            .and(w -> w.ne("r.role_id", user.getRoleId()).or().isNull("r.role_id"))
            .notIn(CollUtil.isNotEmpty(userIds), "u.user_id", userIds)
            .eq(StringUtils.isNotBlank(user.getPhone()), "u.encrypted_phone", EncryptionUtil.encryptKey(user.getPhone(),UserConfig.getPhoneSecret()));
        return xyUserMapper.selectUnallocatedList(pageQuery.build(), wrapper);
    }

    public XyUser selectUserByUserName(String userName) {
        return xyUserMapper.selectUserByUserName(userName);
    }

    public XyUser selectUserById(Long userId) {
        return xyUserMapper.selectUserById(userId);
    }

    public List<XyUser> selectUserByIds(Collection<Long> userIds){
        return xyUserMapper.selectUserList(new LambdaQueryWrapper<XyUser>().in(XyUser::getUserId,userIds));
    }

    public XyUser selectUserByEncryptedPhone(String encryptedPhone) {
        return xyUserMapper.selectUserByEncryptedPhone(encryptedPhone);
    }

    public XyUser selectUserByUserId(Long userId) {
        return xyUserMapper.selectById(userId);
    }




    public boolean checkPhoneUnique(XyUser user){
        return xyUserMapper.exists(new LambdaQueryWrapper<XyUser>()
            .eq(XyUser::getEncryptedPhone, EncryptionUtil.encryptKey(user.getPhone(),UserConfig.getPhoneSecret()))
            .ne(ObjectUtil.isNotNull(user.getUserId()), XyUser::getUserId, user.getUserId()));
    }



    public int insert(XyUser user){
        return xyUserMapper.insert(user);
    }

    public int updateById(XyUser user){
        return xyUserMapper.updateById(user);
    }


    public int deleteById(Long userId){
        return xyUserMapper.deleteById(userId);
    }

    public int deleteBatchIds(Collection<Long> ids){
        return xyUserMapper.deleteBatchIds(ids);
    }





    private Wrapper<XyUser> buildQueryWrapper(XyUser user) {
        Map<String, Object> params = user.getParams();
        QueryWrapper<XyUser> wrapper = Wrappers.query();
        wrapper
            .eq(ObjectUtil.isNotNull(user.getUserId()), "u.user_id", user.getUserId())
            .eq(StringUtils.isNotBlank(user.getPhone()), "u.encrypted_phone", EncryptionUtil.encryptKey(user.getPhone(),UserConfig.getPhoneSecret()))
            .eq(StringUtils.isNotBlank(user.getStatus()), "u.status", user.getStatus())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                "u.create_time", params.get("beginTime"), params.get("endTime"));
        return wrapper;
    }

    private Wrapper<XyUser> buildUserInfoWrapper(XyUserInfoVO userInfoVO) {
        Map<String, Object> params = userInfoVO.getParams();
        QueryWrapper<XyUser> wrapper = Wrappers.query();
        //校验为编号还是手机
        if(StringUtils.isNotBlank(userInfoVO.getUserIdOrPhone())){
            if(PhoneUtil.verifyPhone(userInfoVO.getUserIdOrPhone())){
                wrapper.eq("u.encrypted_phone",EncryptionUtil.encryptKey(userInfoVO.getUserIdOrPhone(), UserConfig.getPhoneSecret()));
            }else{
                wrapper.eq("u.user_id",userInfoVO.getUserIdOrPhone());
            }
        }
        if(ObjectUtil.isNotNull(userInfoVO.getRoleId())){
            List<Long> vipUserIds = xyUserRoleFinder.selectUserIdsByRoleId(RoleEnum.ROLE_VIP.getValue());
            //普通用户
            wrapper.eq(RoleEnum.ROLE_USER.getValue().equals(userInfoVO.getRoleId()),"r.role_id",RoleEnum.ROLE_USER.getValue());
            wrapper.notIn(RoleEnum.ROLE_USER.getValue().equals(userInfoVO.getRoleId()),"u.user_id",vipUserIds);
            //VIP用户
            wrapper.in(RoleEnum.ROLE_VIP.getValue().equals(userInfoVO.getRoleId()),"u.user_id",vipUserIds);
        }
        wrapper.between(params.get("beginTime") != null && params.get("endTime") != null,
            "u.create_time", params.get("beginTime"), params.get("endTime"));
        wrapper.orderByAsc("u.user_id");
        return wrapper;
    }


}
