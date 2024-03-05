package com.yixin.xiaoyi.recruit.dao;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.recruit.entity.Recruit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.yixin.xiaoyi.recruit.dao.mapper.RecruitMapper;
import java.util.Date;
import java.util.List;

/**
 * @author: yixin
 * @date: 2023/3/8 16:27
 */
@Repository
@RequiredArgsConstructor
public class RecruitFinder {

    private final RecruitMapper recruitMapper;


    /**
     * 获取规定时间的招募信息
     * @param compareTime
     * @return
     */
    public List<Recruit> findRecruitCountByTime(Date compareTime){
        return recruitMapper.selectList(new LambdaQueryWrapper<Recruit>()
            .eq(Recruit::getAuditFlag,true)
            .apply("DATE(release_time) = {0}",compareTime));
    }

    /**
     * 获取审核数量
     * @return
     */
    public Integer findAuditStatusCount(Boolean auditFlag){
        return Integer.valueOf(recruitMapper.selectCount(new LambdaQueryWrapper<Recruit>() .eq(Recruit::getAuditFlag,auditFlag)).toString());
    }


    /**
     * 根据ID获取信息
     * @param jobId
     * @return
     */
    public Recruit getRecruitById(Long jobId){
        return recruitMapper.selectById(jobId);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    public int deleteByIds(List<Long> ids){
        return recruitMapper.deleteBatchIds(ids);
    }

    /**
     * 批量修改
     * @param recruits
     * @return
     */
    public boolean updateByIds(List<Recruit> recruits){
        return recruitMapper.updateBatchById(recruits);
    }

    /**
     * 单修改
     * @param recruit
     * @return
     */
    public int updateById(Recruit recruit){
        return recruitMapper.updateById(recruit);
    }

    /**
     * 新增
     * @param recruit
     * @return
     */
    public int insert(Recruit recruit){
        return recruitMapper.insert(recruit);
    }



    /**
     * 获取分页数据
     * @param searchKey
     * @param nature
     * @param lateDays
     * @param location
     * @param audit
     * @param pageQuery
     * @return
     */
    public Page<Recruit> selectRecruitPageList(String searchKey, String nature, Integer lateDays, String location, Boolean audit,  PageQuery pageQuery){
        LambdaQueryWrapper<Recruit> lqw = buildLambdaQueryWrapper(searchKey, nature, lateDays, location, audit);
        Page<Recruit> page = recruitMapper.selectPage(pageQuery.build(),lqw);
        return page;
    }

    /**
     * 获取用户的投递信息
     * @param userId
     * @return
     */
    public Page<Recruit> selectPageDeliverList(Long userId, PageQuery pageQuery){
        QueryWrapper<Recruit> wrapper = Wrappers.query();
        wrapper.eq("r.audit_flag",1);
        wrapper.eq("r.del_flag",0);
        wrapper.eq("rud.user_id",userId);
        wrapper.orderByDesc("rud.create_time");
        return recruitMapper.selectPageDeliverList(pageQuery.build(),wrapper);
    }


    public LambdaQueryWrapper<Recruit> buildLambdaQueryWrapper(String searchKey, String nature, Integer lateDays, String location, Boolean audit){
        LambdaQueryWrapper<Recruit> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(searchKey) && NumberUtil.isNumber(searchKey),Recruit::getId,searchKey);
        lqw.like(StrUtil.isNotBlank(searchKey) && !NumberUtil.isNumber(searchKey),Recruit::getIndustry,searchKey);
        lqw.eq(StrUtil.isNotBlank(nature),Recruit::getNature,nature);
        lqw.like(StrUtil.isNotBlank(location),Recruit::getLocation,location);
        lqw.eq(Recruit::getAuditFlag,audit);
        if(ObjectUtil.isNotEmpty(lateDays)){
            //获取发布时间未最近N天的数据
            Long lateDay = 60*60*24L*lateDays;
            Long nowMillis = System.currentTimeMillis()/1000;
            Long lateDate = nowMillis - lateDay;
            lqw.gt(Recruit::getReleaseTime,new Date(lateDate * 1000));
        }
        if(audit) {
            lqw.orderByDesc(Recruit::getReleaseTime);
        }else{
            lqw.orderByDesc(Recruit::getId);
        }
        return lqw;
    }



}
