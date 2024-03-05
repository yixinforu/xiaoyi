package com.yixin.xiaoyi.system.service;

import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.system.entity.XyDictData;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 字典 业务层
 *
 * @author admin
 */
public interface IXyDictDataService {


    TableDataInfo<XyDictData> selectPageDictDataList(XyDictData dictData, PageQuery pageQuery);

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    List<XyDictData> selectDictDataList(XyDictData dictData);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    String selectDictLabel(String dictType, String dictValue);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    XyDictData selectDictDataById(Long dictCode);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    void deleteDictDataByIds(Long[] dictCodes);

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    List<XyDictData> insertDictData(XyDictData dictData);

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    List<XyDictData> updateDictData(XyDictData dictData);
}
