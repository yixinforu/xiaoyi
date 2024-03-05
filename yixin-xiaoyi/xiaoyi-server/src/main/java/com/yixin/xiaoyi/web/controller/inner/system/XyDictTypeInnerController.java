package com.yixin.xiaoyi.web.controller.inner.system;

import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.system.entity.XyDictType;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.utils.JsonUtils;
import com.yixin.xiaoyi.system.service.IXyDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author admin
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inner/system/dict/type")
public class XyDictTypeInnerController extends BaseController {

    private final IXyDictTypeService dictTypeService;

    /**
     * 查询字典类型列表
     * @param xyDictTypeJson
     * @param pageQueryJson
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<XyDictType> list(@RequestParam("xyDictTypeJson") String xyDictTypeJson,@RequestParam("pageQueryJson") String pageQueryJson) {
        return dictTypeService.selectPageDictTypeList(JsonUtils.parseObject(xyDictTypeJson, XyDictType.class), JsonUtils.parseObject(pageQueryJson,PageQuery.class));
    }



    /**
     * 查询字典类型详细
     *
     * @param dictId 字典ID
     */
    @GetMapping(value = "/{dictId}")
    public R<XyDictType> getInfo(@PathVariable Long dictId) {
        return R.ok(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     * @param dict
     * @return
     */
    @PostMapping
    public R<Void> add(@Validated @RequestBody XyDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return R.fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.insertDictType(dict);
        return R.ok();
    }

    /**
     * 修改字典类型
     * @param dict
     * @return
     */
    @PutMapping
    public R<Void> edit(@Validated @RequestBody XyDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return R.fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.updateDictType(dict);
        return R.ok();
    }

    /**
     * 删除字典类型
     *
     * @param dictIds 字典ID串
     */
    @DeleteMapping("/{dictIds}")
    public R<Void> remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return R.ok();
    }

    /**
     * 刷新字典缓存
     * @return
     */
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        dictTypeService.resetDictCache();
        return R.ok();
    }

    /**
     * 获取字典选择框列表
     * @return
     */
    @GetMapping("/optionselect")
    public R<List<XyDictType>> optionselect() {
        List<XyDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return R.ok(dictTypes);
    }
}
