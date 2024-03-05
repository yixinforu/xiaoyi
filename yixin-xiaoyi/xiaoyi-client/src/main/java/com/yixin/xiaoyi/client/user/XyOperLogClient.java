package com.yixin.xiaoyi.client.user;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.user.entity.XyOperLog;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: huangzexin
 * @date: 2023/8/5 21:31
 */
@FeignClient(
    name = "xy-operlog",
    contextId = "xy-operlog-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyOperLogClient {

    /**
     * 获取操作日志记录列表
     * @param xyOperLogJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/user/operlog/list")
    public TableDataInfo<XyOperLog> list(@RequestParam("xyOperLogJson") String xyOperLogJson, @RequestParam("pageQueryJson") String pageQueryJson) ;

    /**
     * 批量删除操作日志记录
     * @param operIds
     * @return
     */
    @DeleteMapping("/inner/user/operlog/{operIds}")
    public R<Void> remove(@PathVariable("operIds") Long[] operIds);

    /**
     * 清理操作日志记录
     * @return
     */
    @DeleteMapping("/inner/user/operlog/clean")
    public R<Void> clean();


}
