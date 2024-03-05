package com.yixin.xiaoyi.web.controller.api.dict;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import com.yixin.xiaoyi.common.core.controller.BaseController;
import com.yixin.xiaoyi.common.core.domain.R;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.system.service.IXyDictDataService;
import com.yixin.xiaoyi.system.service.IXyDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/9/20 14:23
 */
@RestController
@RequestMapping("/c/dict")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class XyDictDataApiController extends BaseController {

    private final IXyDictTypeService dictTypeService;


    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     */
    @SaIgnore
    @GetMapping(value = "/type/{dictType}")
    public R<List<XyDictData>> dictType(@PathVariable String dictType) {
        List<XyDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return R.ok(data);
    }
}
