package com.yixin.xiaoyi.web.controller.inner.system;

import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.system.service.IXyConfigService;
import com.yixin.xiaoyi.system.entity.XyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置 信息操作处理
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/system/config")
public class XyConfigInnerController extends BaseController {

    private final IXyConfigService configService;

    /**
     * 获取参数配置列表
     * @param xyConfigJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<XyConfig> list(@RequestParam("xyConfigJson") String xyConfigJson,@RequestParam("pageQueryJson") String pageQueryJson) {
        return configService.selectPageConfigList(JsonUtils.parseObject(xyConfigJson,XyConfig.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }


    /**
     * 根据参数编号获取详细信息
     * @param configId
     * @return
     */
    @GetMapping(value = "/{configId}")
    public R<XyConfig> getInfo(@PathVariable Long configId) {
        return R.ok(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     * @param configKey
     * @return
     */
    @GetMapping(value = "/configKey/{configKey}")
    public R<Void> getConfigKey(@PathVariable String configKey) {
        return R.ok(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     * @param config
     * @return
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody XyConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return R.fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.insertConfig(config);
        return R.ok();
    }

    /**
     * 修改参数配置
     * @param config
     * @return
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody XyConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return R.fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.updateConfig(config);
        return R.ok();
    }

    /**
     * 根据参数键名修改参数配置
     * @param config
     * @return
     */
    @PutMapping("/updateByKey")
    public R<Void> updateByKey(@RequestBody XyConfig config) {
        configService.updateConfig(config);
        return R.ok();
    }

    /**
     * 删除参数配置
     *
     * @param configIds 参数ID串
     */
    @DeleteMapping("/{configIds}")
    public R<Void> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return R.ok();
    }

    /**
     * 刷新参数缓存
     * @return
     */
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        configService.resetConfigCache();
        return R.ok();
    }
}
