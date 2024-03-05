package com.yixin.xiaoyi.common.helper;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.core.domain.model.UserPermission;
import com.yixin.xiaoyi.common.enums.UserType;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.model.LoginUser;
import com.yixin.xiaoyi.common.enums.DeviceType;
import com.yixin.xiaoyi.common.exception.UtilException;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * 登录鉴权助手
 *
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 *
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author admin
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";


    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getLoginId());
        setLoginUser(loginUser);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, DeviceType deviceType) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getLoginId(), deviceType.getDevice());
        setLoginUser(loginUser);
    }




    /**
     * 设置用户数据(多级缓存)
     */
    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }


    /**
     * 不再从请求Storage获取是因为缓存会变更，避免与本地缓存数据不一致
     * @return
     */
    public static LoginUser getLoginUserByCache(){
        return (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (ObjectUtil.isNull(loginUser)) {
            String loginId = StpUtil.getLoginIdAsString();
            String[] strs = StringUtils.split(loginId, JOIN_CODE);
            if (!ArrayUtil.containsAny(strs, UserType.values())) {
                throw new UtilException("登录用户: LoginId异常 => " + loginId);
            }
            // 用户id在总是在最后
            return Long.parseLong(strs[strs.length - 1]);
        }
        return loginUser.getUserId();
    }

    /**
     * 获取用户id 无不抛异常
     * @return
     */
    public static Long getUserIdNotException(){
        LoginUser loginUser = getLoginUser();
        if (ObjectUtil.isNull(loginUser)) {
            String loginId = StpUtil.getLoginIdAsString();
            String[] strs = StringUtils.split(loginId, JOIN_CODE);
            // 用户id在总是在最后
            return Long.parseLong(strs[strs.length - 1]);
        }
        return loginUser.getUserId();
    }


    /**
     * 获取用户类型
     */
    public static UserType getUserType() {
        String loginId = StpUtil.getLoginIdAsString();
        return UserType.getUserType(loginId);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_ID.equals(userId);
    }

    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

}
