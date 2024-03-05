package com.yixin.xiaoyi.web.controller.inner.monitor;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.domain.monitor.Server;
import com.yixin.xiaoyi.common.core.domain.monitor.module.CpuModule;
import com.yixin.xiaoyi.common.core.domain.monitor.module.JvmModule;
import com.yixin.xiaoyi.common.core.domain.monitor.module.MemModule;
import com.yixin.xiaoyi.common.core.domain.monitor.module.ServerModule;
import com.yixin.xiaoyi.common.core.domain.monitor.module.SysFileModule;
import com.yixin.xiaoyi.common.core.domain.monitor.module.SysModule;
import com.yixin.xiaoyi.common.utils.BeanCopyUtils;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/inner/monitor/server")
public class XyServerInnerController
{

    /**
     * 获取服务器信息
     * @return
     * @throws Exception
     */
    @GetMapping()
    public R<ServerModule> getInfo() throws Exception
    {
        Map<String, Object> result = new HashMap<>(3);
        Server server = new Server();
        server.copyTo();
        String serverJson = JsonUtils.toJsonString(server);
        return R.ok(JsonUtils.parseObject(serverJson, ServerModule.class));
    }
}
