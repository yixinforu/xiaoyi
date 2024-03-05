package com.yixin.xiaoyi.framework.config;

import com.yixin.xiaoyi.framework.interceptor.ClientIpInterceptor;
import com.yixin.xiaoyi.framework.interceptor.InnerInterceptor;
import com.yixin.xiaoyi.framework.interceptor.PlusWebInvokeTimeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用配置
 *
 * @author admin
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Bean
    public ClientIpInterceptor clientIpInterceptor(){return new ClientIpInterceptor();}

    @Bean
    public InnerInterceptor innerInterceptor(){return new InnerInterceptor();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 全局访问性能拦截
        registry.addInterceptor(new PlusWebInvokeTimeInterceptor());
        registry.addInterceptor(clientIpInterceptor()).addPathPatterns("/c/**");
        registry.addInterceptor(innerInterceptor()).addPathPatterns("/inner/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }
}
