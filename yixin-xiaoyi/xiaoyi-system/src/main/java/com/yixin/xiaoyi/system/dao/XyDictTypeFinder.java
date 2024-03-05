package com.yixin.xiaoyi.system.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.system.entity.XyDictType;
import com.yixin.xiaoyi.system.dao.mapper.XyDictTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/20 19:05
 */
@Repository
@RequiredArgsConstructor
public class XyDictTypeFinder {

    private final XyDictTypeMapper xyDictTypeMapper;

    public Page<XyDictType> selectPageDictTypeList(XyDictType dictType, PageQuery pageQuery){
        Map<String, Object> params = dictType.getParams();
        LambdaQueryWrapper<XyDictType> lqw = new LambdaQueryWrapper<XyDictType>()
            .like(StringUtils.isNotBlank(dictType.getDictName()), XyDictType::getDictName, dictType.getDictName())
            .eq(StringUtils.isNotBlank(dictType.getStatus()), XyDictType::getStatus, dictType.getStatus())
            .like(StringUtils.isNotBlank(dictType.getDictType()), XyDictType::getDictType, dictType.getDictType())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyDictType::getCreateTime, params.get("beginTime"), params.get("endTime"));
        return xyDictTypeMapper.selectPage(pageQuery.build(), lqw);
    }

    public List<XyDictType> selectDictTypeList(XyDictType dictType){
        Map<String, Object> params = dictType.getParams();
        return xyDictTypeMapper.selectList(new LambdaQueryWrapper<XyDictType>()
            .like(StringUtils.isNotBlank(dictType.getDictName()), XyDictType::getDictName, dictType.getDictName())
            .eq(StringUtils.isNotBlank(dictType.getStatus()), XyDictType::getStatus, dictType.getStatus())
            .like(StringUtils.isNotBlank(dictType.getDictType()), XyDictType::getDictType, dictType.getDictType())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyDictType::getCreateTime, params.get("beginTime"), params.get("endTime")));
    }

    public List<XyDictType> selectAllList(){
        return xyDictTypeMapper.selectList();
    }

    public XyDictType selectById(Long dictId){
        return xyDictTypeMapper.selectById(dictId);
    }

    public XyDictType selectDictTypeByType(String dictType){
        return xyDictTypeMapper.selectById(new LambdaQueryWrapper<XyDictType>().eq(XyDictType::getDictType, dictType));
    }

    public int deleteBatchIds(Collection<Long> dictIds){
        return xyDictTypeMapper.deleteBatchIds(dictIds);
    }

    public int insert(XyDictType dict){
        return xyDictTypeMapper.insert(dict);
    }

    public int updateById(XyDictType dict){
        return xyDictTypeMapper.updateById(dict);
    }

    public boolean existsByDictType(XyDictType dict){
        return xyDictTypeMapper.exists(new LambdaQueryWrapper<XyDictType>()
            .eq(XyDictType::getDictType, dict.getDictType())
            .ne(ObjectUtil.isNotNull(dict.getDictId()), XyDictType::getDictId, dict.getDictId()));
    }
}
