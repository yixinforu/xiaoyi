package com.yixin.xiaoyi.template;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Velocity配置
 *
 * @author wangyi
 */
@Configuration
public class VelocityEngineConfig {

    /**
     * 初始化并取得Velocity引擎
     *
     * @return
     */
    @Bean
    public VelocityEngine veEngineer() {
        VelocityEngine veEngine = new VelocityEngine();
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        veEngine.init(properties);
        return veEngine;
    }



}
