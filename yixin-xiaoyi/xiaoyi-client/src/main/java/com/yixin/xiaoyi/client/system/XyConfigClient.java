package com.yixin.xiaoyi.client.system;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.system.entity.XyConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: huangzexin
 * @date: 2023/8/2 22:31
 */
@FeignClient(
    name = "xy-config",
    contextId = "xy-config-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyConfigClient {


    /**
     * 获取参数配置列表
     * @param xyConfigJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/system/config/list")
    public TableDataInfo<XyConfig> list(@RequestParam("xyConfigJson") String xyConfigJson, @RequestParam("pageQueryJson") String pageQueryJson) ;


    /**
     * 根据参数编号获取详细信息
     * @param configId 参数id
     * @return
     */
    @GetMapping(value = "/inner/system/config/{configId}")
    public R<XyConfig> getInfo(@PathVariable("configId") Long configId);


    /**
     * 根据参数键名查询参数值
     * @param configKey
     * @return 配置key
     */
    @GetMapping(value = "/inner/system/config/configKey/{configKey}")
    public R<Void> getConfigKey(@PathVariable("configKey") String configKey) ;


    /**
     * 新增参数配置
     * @param config
     * @return
     */
    @PostMapping("/inner/system/config")
    public R<Void> add(@Validated @RequestBody XyConfig config) ;


    /**
     * 修改参数配置
     * @param config
     * @return
     */
    @PutMapping("/inner/system/config")
    public R<Void> edit(@Validated @RequestBody XyConfig config) ;


    /**
     * 根据参数键名修改参数配置
     * @param config
     * @return
     */
    @PutMapping("/inner/system/config/updateByKey")
    public R<Void> updateByKey(@RequestBody XyConfig config) ;


    /**
     *
     * 删除参数配置
     * @param configIds 参数ID串
     * @return
     */
    @DeleteMapping("/inner/system/config/{configIds}")
    public R<Void> remove(@PathVariable("configIds") Long[] configIds) ;


    /**
     * 刷新参数缓存
     * @return
     */
    @DeleteMapping("/inner/system/config/refreshCache")
    public R<Void> refreshCache() ;
}
