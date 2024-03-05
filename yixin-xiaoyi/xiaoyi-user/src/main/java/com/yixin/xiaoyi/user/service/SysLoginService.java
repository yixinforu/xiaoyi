package com.yixin.xiaoyi.user.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.ShortCodeUtil;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.constant.Constants;
import com.yixin.xiaoyi.common.core.domain.model.LoginBody;
import com.yixin.xiaoyi.common.utils.EncryptionUtil;
import com.yixin.xiaoyi.invite.dao.UserInviteCodeFinder;
import com.yixin.xiaoyi.invite.entity.UserInviteCode;
import com.yixin.xiaoyi.invite.service.UserInviteRecordService;
import com.yixin.xiaoyi.role.dao.XyUserRoleFinder;
import com.yixin.xiaoyi.role.entity.XyUserRole;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.core.domain.model.LoginUser;
import com.yixin.xiaoyi.common.core.service.LogininforService;
import com.yixin.xiaoyi.common.enums.DeviceType;
import com.yixin.xiaoyi.common.enums.LoginType;
import com.yixin.xiaoyi.common.enums.UserStatus;
import com.yixin.xiaoyi.common.exception.user.CaptchaException;
import com.yixin.xiaoyi.common.exception.user.CaptchaExpireException;
import com.yixin.xiaoyi.common.exception.user.UserException;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.DateUtils;
import com.yixin.xiaoyi.common.utils.MessageUtils;
import com.yixin.xiaoyi.common.utils.ServletUtils;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.user.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author admin
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysLoginService {

    private final XyUserAdminService userService;
    private final LogininforService asyncService;
    private final SysPermissionService permissionService;
    private final XyUserRoleFinder xyUserRoleFinder;
    private final UserInviteCodeFinder userInviteCodeFinder;
    private final UserInviteRecordService userInviteRecordService;



    /**
     * 登录验证
     *
     * @param loginBody   手机号
     * @return 结果
     */
    public String login( LoginBody loginBody) {
        HttpServletRequest request = ServletUtils.getRequest();
        //登录校验
        checkLogin(LoginType.SMS, loginBody.getPhone(), () -> !validateSmsCode(loginBody.getPhone(),loginBody.getSmsCode(),request));
        XyUser user = loadUserByPhone(loginBody.getPhone(),loginBody.getInviteCode());
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 校验指定账号是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(user.getUserId());
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        // 缓存权限信息
        permissionService.refreshUserPermission(user.getUserId());
        asyncService.recordLogininfor(user.getEncryptedPhone(),user.getMaskPhone(), Constants.LOGIN_SUCCESS, "登录成功", request);
        recordLoginInfo(user.getUserId());
        return StpUtil.getTokenValue();
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            StpUtil.logout();
            asyncService.recordLogininfor(loginUser.getEncryptedPhone(),loginUser.getMaskPhone(), Constants.LOGOUT, "退出成功", ServletUtils.getRequest());
        } catch (NotLoginException ignored) {
        }
    }

    /**
     * 校验短信验证码
     */
    private boolean validateSmsCode(String phone, String smsCode, HttpServletRequest request) {
        String code = RedisUtils.getCacheObject(CacheConstants.SMS_LOGIN_CODE_KEY + phone);
        if (StringUtils.isBlank(code)) {
            asyncService.recordLogininfor(EncryptionUtil.encryptKey(phone, UserConfig.getPhoneSecret()),DesensitizedUtil.mobilePhone(phone), Constants.LOGIN_FAIL, "验证码已失效", request);
            throw new ServiceException(ErrorCode.SMS_INVALID_CODE);
        }
        return code.equals(smsCode);
    }

    /**
     * 校验验证码
     *
     * @param phone 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String phone, String code, String uuid, HttpServletRequest request) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            asyncService.recordLogininfor(EncryptionUtil.encryptKey(phone,UserConfig.getPhoneSecret()),DesensitizedUtil.mobilePhone(phone), Constants.LOGIN_FAIL, "验证码已失效", request);
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            asyncService.recordLogininfor(EncryptionUtil.encryptKey(phone,UserConfig.getPhoneSecret()),DesensitizedUtil.mobilePhone(phone), Constants.LOGIN_FAIL, "验证码错误", request);
            throw new CaptchaException();
        }
    }


    /**
     * 根据手机号注册用户
     * @param phone
     * @return
     */
    public XyUser register(String phone){
        XyUser xyUser = new XyUser();
        xyUser.setEncryptedPhone(EncryptionUtil.encryptKey(phone,UserConfig.getPhoneSecret()));
        xyUser.setMaskPhone(DesensitizedUtil.mobilePhone(phone));
        xyUser.setStatus("0");
        //注册用户
        userService.insertUser(xyUser);
        //初始化用户角色信息
        insertUserRole(xyUser.getUserId(),new Long[]{RoleEnum.ROLE_USER.getValue()} );
        xyUser.setRoles(new ArrayList<>());
        //新增用户-邀请码
        UserInviteCode userInviteCode = new UserInviteCode();
        userInviteCode.setUserId(xyUser.getUserId());
        userInviteCode.setInviteCode(getUniqueShortCode(xyUser.getUserId()));
        userInviteCodeFinder.insert(userInviteCode);
        return xyUser;
    }

    public String getUniqueShortCode(Long userId){
        Random random = new Random();
        StringBuilder content = new StringBuilder();
        content.append("user_id=").append(userId).append("&")
            .append("time=").append(Instant.now()).append("&")
            .append("random=").append(random.nextInt(Integer.MAX_VALUE));
        //MD5加密
        String md5 = DigestUtils
            .md5DigestAsHex(content.toString().getBytes(StandardCharsets.UTF_8));
        //Base64加密
        String base = Base64.getEncoder().encodeToString(md5.getBytes(StandardCharsets.UTF_8));
        return ShortCodeUtil.getShortCode(base);
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

    private XyUser loadUserByPhone(String phone,String inviteCode) {
        String encryptedPhone = EncryptionUtil.encryptKey(phone,UserConfig.getPhoneSecret());
        XyUser xyUser = userService.selectUserByEncryptedPassword(encryptedPhone);
        if(ObjectUtil.isNull(xyUser)){
            log.info("注册用户:{}.",phone);
            //注册流程
            XyUser user =  register(phone);
            if(StringUtils.isNotBlank(inviteCode)){
                //添加邀请记录
                userInviteRecordService.insertInviteRecord(user.getUserId(),inviteCode);
            }
           return user;
        }else if(UserStatus.DISABLE.getCode().equals(xyUser.getStatus())){
            log.info("登录用户：{} 已被停用.", phone);
            throw new UserException("user.blocked", phone);
        }
        return xyUser;
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(XyUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setMaskPhone(user.getMaskPhone());
        loginUser.setEncryptedPhone(user.getEncryptedPhone());
        return loginUser;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        XyUser XyUser = new XyUser();
        XyUser.setUserId(userId);
        XyUser.setLoginIp(ServletUtils.getClientIP());
        XyUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(XyUser);
    }

    /**
     * 登录校验
     */
    private void checkLogin(LoginType loginType, String phone, Supplier<Boolean> supplier) {
        HttpServletRequest request = ServletUtils.getRequest();
        String errorKey = CacheConstants.SMS_ERR_CNT_KEY + phone;
        String loginFail = Constants.LOGIN_FAIL;

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(UserConfig.getMaxRetryCount())) {
            asyncService.recordLogininfor(EncryptionUtil.encryptKey(phone,UserConfig.getPhoneSecret()),DesensitizedUtil.mobilePhone(phone), loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), UserConfig.getMaxRetryCount(), UserConfig.getLockTime()), request);
            throw new UserException(loginType.getRetryLimitExceed(), UserConfig.getMaxRetryCount(), UserConfig.getLockTime());
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(UserConfig.getMaxRetryCount())) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(UserConfig.getLockTime()));
                asyncService.recordLogininfor(EncryptionUtil.encryptKey(phone,UserConfig.getPhoneSecret()),DesensitizedUtil.mobilePhone(phone), loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), UserConfig.getMaxRetryCount(), UserConfig.getLockTime()), request);
                throw new UserException(loginType.getRetryLimitExceed(), UserConfig.getMaxRetryCount(), UserConfig.getLockTime());
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                asyncService.recordLogininfor(EncryptionUtil.encryptKey(phone,UserConfig.getPhoneSecret()),DesensitizedUtil.mobilePhone(phone), loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber), request);
                throw new ServiceException(ErrorCode.SMS_ERROR_CODE);
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
        //登录成功，清空其手机验证码
        RedisUtils.deleteObject(CacheConstants.SMS_LOGIN_CODE_KEY + phone);
    }
}
