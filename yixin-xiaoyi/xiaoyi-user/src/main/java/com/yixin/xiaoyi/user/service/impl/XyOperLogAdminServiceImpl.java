package com.yixin.xiaoyi.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.module.user.dto.OperLogDTO;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.core.service.OperLogService;
import com.yixin.xiaoyi.common.utils.ip.AddressUtils;
import com.yixin.xiaoyi.user.dao.XyOperLogFinder;
import com.yixin.xiaoyi.user.entity.XyOperLog;
import com.yixin.xiaoyi.user.service.XyOperLogAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author admin
 */
@RequiredArgsConstructor
@Service
public class XyOperLogAdminServiceImpl implements XyOperLogAdminService, OperLogService {

    private final XyOperLogFinder xyOperLogFinder;

    /**
     * 操作日志记录
     *
     * @param operLogDTO 操作日志信息
     */
    @Async
    @Override
    public void recordOper(final OperLogDTO operLogDTO) {
        XyOperLog operLog = BeanUtil.toBean(operLogDTO, XyOperLog.class);
        // 远程查询操作地点
        operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
        insertOperlog(operLog);
    }

    @Override
    public TableDataInfo<XyOperLog> selectPageOperLogList(XyOperLog operLog, PageQuery pageQuery) {
        Page<XyOperLog> page = xyOperLogFinder.selectPageOperLogList(operLog, pageQuery);
        return TableDataInfo.build(page);
    }

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(XyOperLog operLog) {
        operLog.setOperTime(new Date());
       xyOperLogFinder.insert(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<XyOperLog> selectOperLogList(XyOperLog operLog) {
        return xyOperLogFinder.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return xyOperLogFinder.deleteBatchIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public XyOperLog selectOperLogById(Long operId) {
        return xyOperLogFinder.selectById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        xyOperLogFinder.cleanOperLog();
    }
}
