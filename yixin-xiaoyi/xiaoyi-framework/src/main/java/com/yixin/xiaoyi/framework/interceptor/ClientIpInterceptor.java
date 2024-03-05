package com.yixin.xiaoyi.framework.interceptor;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.ServletUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.user.entity.UserOnlineLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;


/**
 * 获取用户IP地址
 * @author: yixin
 * @date: 2023/2/25 10:53
 */
@Slf4j
@Component
@Order(0)
public class ClientIpInterceptor implements HandlerInterceptor {




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 获取请求用户的ip和用户编号
        String clientIp = ServletUtils.getClientIP();
        Long userId = StpUtil.isLogin() ? LoginHelper.getUserIdNotException() : -1L;
        String cacheOnlineKey = CacheConstants.CLIENT_ONLINE_LOG + clientIp+ ":"+userId;
        UserOnlineLog userOnlineLogCache = RedisUtils.getCacheObject(cacheOnlineKey);
        //注意：这是日志请求，尽量不要创建对象，耗性能！
        if(ObjectUtil.isNull(userOnlineLogCache)){
            UserOnlineLog userOnlineLog = new UserOnlineLog();
            userOnlineLog.setUserId(userId);
            userOnlineLog.setClientIp(clientIp);
            userOnlineLog.setCurrentRequestTime(System.currentTimeMillis());
            userOnlineLog.setRequestCount(1);
            RedisUtils.setCacheObject(cacheOnlineKey, userOnlineLog, Duration.ofHours(UserConfig.getAccessLogExpireTime()));
        }else{
            userOnlineLogCache.setUserId(userId);
            userOnlineLogCache.setCurrentRequestTime(System.currentTimeMillis());
            userOnlineLogCache.setRequestCount(userOnlineLogCache.getRequestCount()+1);
            RedisUtils.setCacheObject(cacheOnlineKey, userOnlineLogCache, Duration.ofHours(UserConfig.getAccessLogExpireTime()));
        }
        return true;
    }

}
