package com.yixin.xiaoyi.client.monitor;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.domain.monitor.Server;
import com.yixin.xiaoyi.common.core.domain.monitor.module.ServerModule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: huangzexin
 * @date: 2023/8/5 21:39
 */
@FeignClient(
    name = "xy-server",
    contextId = "xy-server-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyServerClient {

    /**
     * 服务器配置
     * @return
     */
    @GetMapping("/inner/monitor/server")
    public R<ServerModule> getInfo();
}
