package com.yixin.xiaoyi.system.service;

import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.system.entity.XyDictType;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author admin
 */
public interface IXyDictTypeService {


    TableDataInfo<XyDictType> selectPageDictTypeList(XyDictType dictType, PageQuery pageQuery);

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    List<XyDictType> selectDictTypeList(XyDictType dictType);

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    List<XyDictType> selectDictTypeAll();

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<XyDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    XyDictType selectDictTypeById(Long dictId);

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    XyDictType selectDictTypeByType(String dictType);

    /**
     * 批量删除字典信息
     *
     * @param dictIds 需要删除的字典ID
     */
    void deleteDictTypeByIds(Long[] dictIds);

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    /**
     * 清空字典缓存数据
     */
    void clearDictCache();

    /**
     * 重置字典缓存数据
     */
    void resetDictCache();

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    List<XyDictData> insertDictType(XyDictType dictType);

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    List<XyDictData> updateDictType(XyDictType dictType);

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    String checkDictTypeUnique(XyDictType dictType);
}
