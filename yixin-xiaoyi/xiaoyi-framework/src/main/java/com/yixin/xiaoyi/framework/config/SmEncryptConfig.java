package com.yixin.xiaoyi.framework.config;

import com.yixin.xiaoyi.framework.handler.SmEncryptHandler;
import lombok.RequiredArgsConstructor;
import com.yixin.xiaoyi.common.config.XiaoYiConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件国密加解密配置类
 *
 * @author nieHong
 * @date 2023/6/26
 */
@Configuration
@RequiredArgsConstructor
public class SmEncryptConfig {

    private final XiaoYiConfig xiaoYiConfig;

    @Bean
    public SmEncryptHandler smEncryptHandler() {
        return new SmEncryptHandler(xiaoYiConfig.getSm4Key());
    }
}
