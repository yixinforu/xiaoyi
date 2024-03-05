package com.yixin.xiaoyi.system.service.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.constant.CacheConstants;
import com.yixin.xiaoyi.common.constant.CacheNames;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.system.dao.XyDictDataFinder;
import com.yixin.xiaoyi.system.dao.XyDictTypeFinder;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.system.entity.XyDictType;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.core.service.DictService;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.StreamUtils;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.redis.CacheUtils;
import com.yixin.xiaoyi.common.utils.spring.SpringUtils;
import com.yixin.xiaoyi.system.service.IXyDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典 业务层处理
 *
 * @author admin
 */
@RequiredArgsConstructor
@Service
public class XyDictTypeServiceImpl implements IXyDictTypeService, DictService {

    private final XyDictTypeFinder xyDictTypeFinder;
    private final XyDictDataFinder xyDictDataFinder;

    @Override
    public TableDataInfo<XyDictType> selectPageDictTypeList(XyDictType dictType, PageQuery pageQuery) {
        Page<XyDictType> page = xyDictTypeFinder.selectPageDictTypeList(dictType,pageQuery);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<XyDictType> selectDictTypeList(XyDictType dictType) {
        return xyDictTypeFinder.selectDictTypeList(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<XyDictType> selectDictTypeAll() {
        return xyDictTypeFinder.selectAllList();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    @Override
    public List<XyDictData> selectDictDataByType(String dictType) {
        List<XyDictData> dictDatas = xyDictDataFinder.selectDictDataByType(dictType);
        if (CollUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public XyDictType selectDictTypeById(Long dictId) {
        return xyDictTypeFinder.selectById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    @Override
    public XyDictType selectDictTypeByType(String dictType) {
        return xyDictTypeFinder.selectDictTypeByType(dictType);
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            XyDictType dictType = selectDictTypeById(dictId);
            if (xyDictDataFinder.existsByDictType(dictType.getDictType())) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            CacheUtils.evict(CacheNames.SYS_DICT, dictType.getDictType());
        }
        xyDictTypeFinder.deleteBatchIds(Arrays.asList(dictIds));
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        List<XyDictData> dictDataList = xyDictDataFinder.selectListByNormal();
        Map<String, List<XyDictData>> dictDataMap = StreamUtils.groupByKey(dictDataList, XyDictData::getDictType);
        dictDataMap.forEach((k,v) -> {
            List<XyDictData> dictList = StreamUtils.sorted(v, Comparator.comparing(XyDictData::getDictSort));
            CacheUtils.put(CacheNames.SYS_DICT, k, dictList);
        });
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        CacheUtils.clear(CacheNames.SYS_DICT);
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.dictType")
    @Override
    public List<XyDictData> insertDictType(XyDictType dict) {
        int row = xyDictTypeFinder.insert(dict);
        if (row > 0) {
            return new ArrayList<>();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.dictType")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<XyDictData> updateDictType(XyDictType dict) {
        XyDictType oldDict = xyDictTypeFinder.selectById(dict.getDictId());
        xyDictDataFinder.updateByDictType(oldDict.getDictType(),dict.getDictType());
        int row = xyDictTypeFinder.updateById(dict);
        if (row > 0) {
            CacheUtils.evict(CacheNames.SYS_DICT, oldDict.getDictType());
            return xyDictDataFinder.selectDictDataByType(dict.getDictType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(XyDictType dict) {
        boolean exist = xyDictTypeFinder.existsByDictType(dict);
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public String getDictLabel(String dictType, String dictValue, String separator) {
        // 优先从本地缓存获取
        List<XyDictData> datas = (List<XyDictData>) SaHolder.getStorage().get(CacheConstants.SYS_DICT_KEY + dictType);
        if (ObjectUtil.isNull(datas)) {
            datas = SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
            SaHolder.getStorage().set(CacheConstants.SYS_DICT_KEY + dictType, datas);
        }

        Map<String, String> map = StreamUtils.toMap(datas, XyDictData::getDictValue, XyDictData::getDictLabel);
        if (StringUtils.containsAny(dictValue, separator)) {
            return Arrays.stream(dictValue.split(separator))
                .map(v -> map.getOrDefault(v, StringUtils.EMPTY))
                .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictValue, StringUtils.EMPTY);
        }
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public String getDictValue(String dictType, String dictLabel, String separator) {
        // 优先从本地缓存获取
        List<XyDictData> datas = (List<XyDictData>) SaHolder.getStorage().get(CacheConstants.SYS_DICT_KEY + dictType);
        if (ObjectUtil.isNull(datas)) {
            datas = SpringUtils.getAopProxy(this).selectDictDataByType(dictType);
            SaHolder.getStorage().set(CacheConstants.SYS_DICT_KEY + dictType, datas);
        }

        Map<String, String> map = StreamUtils.toMap(datas, XyDictData::getDictLabel, XyDictData::getDictValue);
        if (StringUtils.containsAny(dictLabel, separator)) {
            return Arrays.stream(dictLabel.split(separator))
                .map(l -> map.getOrDefault(l, StringUtils.EMPTY))
                .collect(Collectors.joining(separator));
        } else {
            return map.getOrDefault(dictLabel, StringUtils.EMPTY);
        }
    }

}
