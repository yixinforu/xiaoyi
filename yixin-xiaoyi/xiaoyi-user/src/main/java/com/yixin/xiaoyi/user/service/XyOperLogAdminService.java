package com.yixin.xiaoyi.user.service;

import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.user.entity.XyOperLog;
import com.yixin.xiaoyi.user.model.dto.UserDataDTO;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author admin
 */
public interface XyOperLogAdminService {

    TableDataInfo<XyOperLog> selectPageOperLogList(XyOperLog operLog, PageQuery pageQuery);

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    void insertOperlog(XyOperLog operLog);

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    List<XyOperLog> selectOperLogList(XyOperLog operLog);

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    int deleteOperLogByIds(Long[] operIds);

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    XyOperLog selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    void cleanOperLog();




}
