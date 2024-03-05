package com.yixin.xiaoyi.client.system;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.system.entity.XyDictType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/2 22:31
 */
@FeignClient(
    name = "xy-dict-type",
    contextId = "xy-dict-type-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyDictTypeClient {

    /**
     *
     * 查询字典类型列表
     * @param xyDictTypeJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/system/dict/type/list")
    public TableDataInfo<XyDictType> list(@RequestParam("xyDictTypeJson") String xyDictTypeJson, @RequestParam("pageQueryJson") String pageQueryJson) ;


    /**
     *
     * 查询字典类型详细
     * @param dictId
     * @return
     */
    @GetMapping(value = "/inner/system/dict/type/{dictId}")
    public R<XyDictType> getInfo(@PathVariable("dictId") Long dictId) ;

    /**
     * 新增字典类型
     * @param dict
     * @return
     */
    @PostMapping("/inner/system/dict/type")
    public R<Void> add(@Validated @RequestBody XyDictType dict);

    /**
     *
     * 修改字典类型
     * @param dict
     * @return
     */
    @PutMapping("/inner/system/dict/type")
    public R<Void> edit(@Validated @RequestBody XyDictType dict);

    /**
     *
     * 删除字典类型
     * @param dictIds
     * @return
     */
    @DeleteMapping("/inner/system/dict/type/{dictIds}")
    public R<Void> remove(@PathVariable("dictIds") Long[] dictIds);

    /**
     *
     * 刷新字典缓存
     * @return
     */
    @DeleteMapping("/inner/system/dict/type/refreshCache")
    public R<Void> refreshCache();

    /**
     *
     * 获取字典选择框列表
     * @return
     */
    @GetMapping("/inner/system/dict/type/optionselect")
    public R<List<XyDictType>> optionselect();
}
