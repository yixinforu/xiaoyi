package com.yixin.xiaoyi.web.controller.inner.system;

import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.system.service.IXyDictDataService;
import com.yixin.xiaoyi.system.service.IXyDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("inner/system/dict/data")
public class XyDictDataInnerController extends BaseController {

    private final IXyDictDataService dictDataService;
    private final IXyDictTypeService dictTypeService;

    /**
     * 查询字典数据列表
     * @param xyDictDataJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<XyDictData> list(@RequestParam("xyDictDataJson") String xyDictDataJson,@RequestParam("pageQueryJson") String pageQueryJson) {
        return dictDataService.selectPageDictDataList(JsonUtils.parseObject(xyDictDataJson,XyDictData.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }


    /**
     * 查询字典数据详细
     *
     * @param dictCode 字典code
     */
    @GetMapping(value = "/{dictCode}")
    public R<XyDictData> getInfo(@PathVariable Long dictCode) {
        return R.ok(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     */
    @GetMapping(value = "/type/{dictType}")
    public R<List<XyDictData>> dictType(@PathVariable String dictType) {
        List<XyDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return R.ok(data);
    }

    /**
     * 新增字典类型
     * @param dict
     * @return
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody XyDictData dict) {
        dictDataService.insertDictData(dict);
        return R.ok();
    }

    /**
     * 修改保存字典类型
     * @param dict
     * @return
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody XyDictData dict) {
        dictDataService.updateDictData(dict);
        return R.ok();
    }

    /**
     * 删除字典类型
     *
     * @param dictCodes 字典code串
     */
    @DeleteMapping("/{dictCodes}")
    public R<Void> remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return R.ok();
    }
}
