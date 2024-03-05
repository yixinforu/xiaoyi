package com.yixin.xiaoyi.system.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.system.dao.mapper.XyDictDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/20 19:05
 */
@Repository
@RequiredArgsConstructor
public class XyDictDataFinder {

    private final XyDictDataMapper xyDictDataMapper;

    public Page<XyDictData> selectPageDictDataList(XyDictData dictData, PageQuery pageQuery) {
        LambdaQueryWrapper<XyDictData> lqw = new LambdaQueryWrapper<XyDictData>()
            .eq(StringUtils.isNotBlank(dictData.getDictType()), XyDictData::getDictType, dictData.getDictType())
            .like(StringUtils.isNotBlank(dictData.getDictLabel()), XyDictData::getDictLabel, dictData.getDictLabel())
            .eq(StringUtils.isNotBlank(dictData.getStatus()), XyDictData::getStatus, dictData.getStatus())
            .orderByAsc(XyDictData::getDictSort);
        return xyDictDataMapper.selectPage(pageQuery.build(), lqw);
    }

    public List<XyDictData> selectDictDataList(XyDictData dictData) {
        return  xyDictDataMapper.selectList(new LambdaQueryWrapper<XyDictData>()
            .eq(StringUtils.isNotBlank(dictData.getDictType()), XyDictData::getDictType, dictData.getDictType())
            .like(StringUtils.isNotBlank(dictData.getDictLabel()), XyDictData::getDictLabel, dictData.getDictLabel())
            .eq(StringUtils.isNotBlank(dictData.getStatus()), XyDictData::getStatus, dictData.getStatus())
            .orderByAsc(XyDictData::getDictSort));
    }

    public String selectDictLabel(String dictType, String dictValue) {
        return xyDictDataMapper.selectOne(new LambdaQueryWrapper<XyDictData>()
            .select(XyDictData::getDictLabel)
            .eq(XyDictData::getDictType, dictType)
            .eq(XyDictData::getDictValue, dictValue))
            .getDictLabel();
    }

    public XyDictData selectById(Long dictCode) {
        return xyDictDataMapper.selectById(dictCode);
    }

    public int deleteById(Long dictCode){
        return  xyDictDataMapper.deleteById(dictCode);
    }

    public int insert(XyDictData data){
        return xyDictDataMapper.insert(data);
    }

    public List<XyDictData> selectDictDataByType(String dictType){
        return xyDictDataMapper.selectDictDataByType(dictType);
    }

    public int updateById(XyDictData data){
        return xyDictDataMapper.updateById(data);
    }

    public int updateByDictType(String oldDictType, String newDictType){
        return xyDictDataMapper.update(null, new LambdaUpdateWrapper<XyDictData>()
            .set(XyDictData::getDictType,newDictType)
            .eq(XyDictData::getDictType, oldDictType));
    }

    public boolean existsByDictType(String dictType){
        return xyDictDataMapper.exists(new LambdaQueryWrapper<XyDictData>()
            .eq(XyDictData::getDictType, dictType));
    }

    public List<XyDictData> selectListByNormal(){
        return xyDictDataMapper.selectList(
            new LambdaQueryWrapper<XyDictData>().eq(XyDictData::getStatus, UserConstants.DICT_NORMAL));
    }


}
