package com.yixin.xiaoyi.system.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author admin
 */
public interface XyDictDataMapper extends BaseMapperPlus<XyDictDataMapper, XyDictData, XyDictData> {

    default List<XyDictData> selectDictDataByType(String dictType) {
        return selectList(
            new LambdaQueryWrapper<XyDictData>()
                .eq(XyDictData::getStatus, UserConstants.DICT_NORMAL)
                .eq(XyDictData::getDictType, dictType)
                .orderByAsc(XyDictData::getDictSort));
    }
}
