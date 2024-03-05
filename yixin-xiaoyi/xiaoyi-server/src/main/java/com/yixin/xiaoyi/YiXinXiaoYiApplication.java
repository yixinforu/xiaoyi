package com.yixin.xiaoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;


/**
 * 启动程序
 *
 * @author admin
 */

@SpringBootApplication
public class YiXinXiaoYiApplication {

    public static void main(String[] args) {
//        System.out.println("test");
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication application = new SpringApplication(YiXinXiaoYiApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        successfulStartup();
    }

    public static void  successfulStartup(){
        System.out.println("======= 一心启动成功 =======  ﾞﾞ                \n" +
            "                                                           \n" +
            "                     .--.                                \n" +
            "                    /  /      .--.       .--.            \n" +
            "                   /  / .--.   \\  \\       \\  \\       \n " +
            ".--------------. /  /  \\   \\   '--'   .--.'--'          \n" +
            " '--------------''--'    \\   \\. _ _ _ /  /              \n" +
            "                          ' _ _ _ _ _ _.'                \n" +
            "                                                           \n" );
    }
}
