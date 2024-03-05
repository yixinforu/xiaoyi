package com.yixin.xiaoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.constant.CacheNames;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.system.dao.XyDictDataFinder;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.redis.CacheUtils;
import com.yixin.xiaoyi.system.service.IXyDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author admin
 */
@RequiredArgsConstructor
@Service
public class XyDictDataServiceImpl implements IXyDictDataService {

    private final XyDictDataFinder xyDictDataFinder;

    @Override
    public TableDataInfo<XyDictData> selectPageDictDataList(XyDictData dictData, PageQuery pageQuery) {
        Page<XyDictData> page = xyDictDataFinder.selectPageDictDataList(dictData, pageQuery);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<XyDictData> selectDictDataList(XyDictData dictData) {
        return xyDictDataFinder.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return xyDictDataFinder.selectDictLabel(dictType,dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public XyDictData selectDictDataById(Long dictCode) {
        return xyDictDataFinder.selectById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            XyDictData data = selectDictDataById(dictCode);
            xyDictDataFinder.deleteById(dictCode);
            CacheUtils.evict(CacheNames.SYS_DICT, data.getDictType());
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#data.dictType")
    @Override
    public List<XyDictData> insertDictData(XyDictData data) {
        int row = xyDictDataFinder.insert(data);
        if (row > 0) {
            return xyDictDataFinder.selectDictDataByType(data.getDictType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#data.dictType")
    @Override
    public List<XyDictData> updateDictData(XyDictData data) {
        int row = xyDictDataFinder.updateById(data);
        if (row > 0) {
            return xyDictDataFinder.selectDictDataByType(data.getDictType());
        }
        throw new ServiceException("操作失败");
    }

}
