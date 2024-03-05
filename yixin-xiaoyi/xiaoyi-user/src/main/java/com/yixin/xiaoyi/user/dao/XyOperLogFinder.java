package com.yixin.xiaoyi.user.dao;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.user.entity.XyOperLog;
import com.yixin.xiaoyi.user.dao.mapper.XyOperLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/20 12:42
 */
@Repository
@RequiredArgsConstructor
public class XyOperLogFinder {

    private final XyOperLogMapper xyOperLogMapper;

    public int insert(XyOperLog operLog){
        return xyOperLogMapper.insert(operLog);
    }


    public List<XyOperLog> selectOperLogList(XyOperLog operLog) {
        Map<String, Object> params = operLog.getParams();
        return xyOperLogMapper.selectList(new LambdaQueryWrapper<XyOperLog>()
            .like(StringUtils.isNotBlank(operLog.getTitle()), XyOperLog::getTitle, operLog.getTitle())
            .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                XyOperLog::getBusinessType, operLog.getBusinessType())
            .func(f -> {
                if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                    f.in(XyOperLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                }
            })
            .eq(operLog.getStatus() != null && operLog.getStatus() > 0,
                XyOperLog::getStatus, operLog.getStatus())
            .like(!ObjectUtils.isEmpty(operLog.getOperUserId()), XyOperLog::getOperUserId, operLog.getOperUserId())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyOperLog::getOperTime, params.get("beginTime"), params.get("endTime"))
            .orderByDesc(XyOperLog::getOperId));
    }

    public int deleteBatchIds(Collection<Long> ids){
        return xyOperLogMapper.deleteBatchIds(Arrays.asList(ids));
    }

    public XyOperLog selectById(Long operId){
        return xyOperLogMapper.selectById(operId);
    }

    public int cleanOperLog(){
         return xyOperLogMapper.delete(new LambdaQueryWrapper<>());
    }


    public Page<XyOperLog> selectPageOperLogList(XyOperLog operLog, PageQuery pageQuery){
        Map<String, Object> params = operLog.getParams();
        LambdaQueryWrapper<XyOperLog> lqw = new LambdaQueryWrapper<XyOperLog>()
            .like(StringUtils.isNotBlank(operLog.getTitle()), XyOperLog::getTitle, operLog.getTitle())
            .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                XyOperLog::getBusinessType, operLog.getBusinessType())
            .func(f -> {
                if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                    f.in(XyOperLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                }
            })
            .eq(operLog.getStatus() != null,
                XyOperLog::getStatus, operLog.getStatus())
            .like(!ObjectUtils.isEmpty(operLog.getOperUserId()),XyOperLog::getOperUserId, operLog.getOperUserId())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                XyOperLog::getOperTime, params.get("beginTime"), params.get("endTime"));
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("oper_id");
            pageQuery.setIsAsc("desc");
        }
        return xyOperLogMapper.selectPage(pageQuery.build(), lqw);
    }

}
