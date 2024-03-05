package com.yixin.xiaoyi.client.system;

import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.system.entity.XyDictData;
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
    name = "xy-dict-data",
    contextId = "xy-dict-data-client",
    url = "${feign.client.config.xiaoyi.url}")
public interface XyDictDataClient {


    /**
     * 查询字典数据列表
     * @param xyDictDataJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/inner/system/dict/data/list")
    public TableDataInfo<XyDictData> list(@RequestParam("xyDictDataJson") String xyDictDataJson, @RequestParam("pageQueryJson") String pageQueryJson) ;


    /**
     * 查询字典数据详细
     * @param dictCode 字典code
     * @return
     */
    @GetMapping(value = "/inner/system/dict/data/{dictCode}")
    public R<XyDictData> getInfo(@PathVariable("dictCode") Long dictCode) ;

    /**
     * 根据字典类型查询字典数据信息
     * @param dictType 字典类型
     */
    @GetMapping(value = "/inner/system/dict/data/type/{dictType}")
    public R<List<XyDictData>> dictType(@PathVariable("dictType") String dictType) ;


    /**
     * 新增字典类型
     * @param dict
     * @return
     */
    @PostMapping("/inner/system/dict/data")
    public R<Void> add(@Validated @RequestBody XyDictData dict) ;

    /**
     * 修改保存字典类型
     * @param dict
     * @return
     */
    @PutMapping("/inner/system/dict/data")
    public R<Void> edit(@Validated @RequestBody XyDictData dict) ;

    /**
     * 删除字典类型
     * @param dictCodes 字典code
     * @return
     */
    @DeleteMapping("/inner/system/dict/data/{dictCodes}")
    public R<Void> remove(@PathVariable("dictCodes") Long[] dictCodes) ;


    }
