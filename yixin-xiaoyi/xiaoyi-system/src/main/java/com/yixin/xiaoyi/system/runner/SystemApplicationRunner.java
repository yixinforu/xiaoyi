package com.yixin.xiaoyi.system.runner;

import com.yixin.xiaoyi.system.service.IXyConfigService;
import com.yixin.xiaoyi.system.service.IXyDictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 system 模块对应业务数据
 *
 * @author admin
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    private final IXyConfigService configService;
    private final IXyDictTypeService dictTypeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        configService.loadingConfigCache();
        log.info("加载参数缓存数据成功");
        dictTypeService.loadingDictCache();
        log.info("加载字典缓存数据成功");
    }

}
