package com.yixin.xiaoyi.framework.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.ServletUtils;
import com.yixin.xiaoyi.common.utils.redis.RedisUtils;
import com.yixin.xiaoyi.common.utils.sign.Base64;
import com.yixin.xiaoyi.common.utils.sign.Md5Utils;
import com.yixin.xiaoyi.user.entity.UserOnlineLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

/**
 * @author: huangzexin
 * @date: 2023/10/9 14:34
 */
@Slf4j
@Component
@Order(1)
public class InnerInterceptor implements HandlerInterceptor {

    @Value("${feign.secretKey}")
    private String secretKey;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 进行签名完整性和重发校验
        String signingKey = request.getHeader("Signature");
        String timestamp = request.getHeader("Timestamp");
        if( ObjectUtil.isNotNull(signingKey) && ObjectUtil.isNotNull(timestamp)
        && generateSignature(timestamp).equals(signingKey) && Math.abs(System.currentTimeMillis() - Long.valueOf(timestamp)) < 10000){
            return true;
        }
        return false;
    }

    /**
     * 生成签名
     * @param timestamp
     * @return
     */
    public String generateSignature(String timestamp) {
        StringBuilder content = new StringBuilder();
        content.append("timestamp=").append(timestamp).append("&")
            .append("secretKey=").append(secretKey);
        //先MD5加密 再进行Base64加密
        String signature = Base64.encode(Md5Utils.hash(content.toString()).getBytes(StandardCharsets.UTF_8));
        return signature;
    }
}
