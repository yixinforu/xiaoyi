package com.yixin.xiaoyi.recruit.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.enums.ErrorCode;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.DateUtils;
import com.yixin.xiaoyi.recruit.dao.NewCountFinder;
import com.yixin.xiaoyi.recruit.dao.RecruitFinder;
import com.yixin.xiaoyi.recruit.dao.mapper.NewCountMapper;
import com.yixin.xiaoyi.recruit.entity.NewCount;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import com.yixin.xiaoyi.recruit.enums.RecruitNatureEnum;
import com.yixin.xiaoyi.recruit.model.dto.RecruitDataDTO;
import com.yixin.xiaoyi.recruit.model.vo.RecruitAuditVO;
import com.yixin.xiaoyi.recruit.service.RecruitAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: yixin
 * @date: 2023/3/9 17:20
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RecruitAdminServiceImpl implements RecruitAdminService {

    private final RecruitFinder recruitFinder;
    private final NewCountFinder newCountFinder;



    /**
     * 根据id获取具体招聘信息
     * @param id
     * @return
     */
    @Override
    public Recruit getRecruit(Long id){
        Recruit jobInfo = Optional.ofNullable(recruitFinder.getRecruitById(id)).orElseThrow(()->new ServiceException(ErrorCode.RECRUIT_NOT_FOUND));
        return jobInfo;
    }

    /**
     * 获取招聘统计数据
     * @return
     */
    @Override
    public RecruitDataDTO findRecruitBaseData(){
        Date currentDate = DateUtils.getTodayTimeZero();
        List<Recruit> newRecruitList = recruitFinder.findRecruitCountByTime(currentDate);
        //今日新增总数
        Integer newTotalCount = newRecruitList.size();
        //新增校招数
        Integer newCampusCount = newRecruitList.stream().filter(it->RecruitNatureEnum.CAMPUS_NATURE.getValue().equals(it.getNature())).collect(Collectors.toList()).size();
        //新增实习数
        Integer newPracticeCount =newRecruitList.stream().filter(it->RecruitNatureEnum.PRACTICE_NATURE.getValue().equals(it.getNature())).collect(Collectors.toList()).size();
        //待审核数
        Integer todoAuditCount = recruitFinder.findAuditStatusCount(false);
        //已审核数
        Integer auditCount = recruitFinder.findAuditStatusCount(true);

        return RecruitDataDTO.builder()
            .newTotalCount(newTotalCount)
            .newCampusCount(newCampusCount)
            .newPracticeCount(newPracticeCount)
            .todoAuditCount(todoAuditCount)
            .auditCount(auditCount).build();
    }

    /**
     * 删除招聘信息
     * @param jobIds
     * @return
     */
    @Override
    public int deleteRecruitByIds(Long[] jobIds){
        return recruitFinder.deleteByIds(Arrays.asList(jobIds));
    }


    /**
     * 批量审核招聘信息
     * @param recruitAuditVO
     * @return
     */
    @Override
    public boolean auditRecruitByIds(RecruitAuditVO recruitAuditVO) {
        if(CollectionUtils.isEmpty(recruitAuditVO.getRecruitIds())){
            return false;
        }
        List<Recruit> recruits = new ArrayList<>();
        recruitAuditVO.getRecruitIds().stream().forEach(it->{
            recruits.add(new Recruit().setId(it).setAuditFlag(recruitAuditVO.getAuditFlag()).setReleaseTime(recruitAuditVO.getAuditFlag()?new Date():null));
        });
        return recruitFinder.updateByIds(recruits);
    }

    /**
     * 删除招聘信息
     * @param recruit
     * @return
     */
    @Override
    public int editRecruit(Recruit recruit){
        return recruitFinder.updateById(recruit);
    }

    /**
     * 增加招聘信息
     * @param recruit
     * @return
     */
    @Override
    public int addRecruit( Recruit recruit){
        //手动录入的默认未审核
        recruit.setAuditFlag(false);
        return recruitFinder.insert(recruit);
    }

    /**
     * 更新计数
     * @param newCount
     * @return
     */
    @Override
    public int updateCount(NewCount newCount) {
        return newCountFinder.updateCountById(newCount);
    }

    /**
     * 获取当前计数
     * @return
     */
    @Override
    public NewCount selectNewCount() {
        return newCountFinder.selectNewCount();
    }

    /**
     * 获取招聘信息
     * @param searchKey
     * @param nature
     * @param lateDays
     * @param location
     * @param auditStatus
     * @param pageQuery
     * @return
     */
    @Override
    public Page<Recruit> selectRecruitList(String searchKey, String nature, Integer lateDays, String location, Boolean auditStatus, PageQuery pageQuery){
        Page<Recruit> recruits = recruitFinder.selectRecruitPageList(searchKey,nature, lateDays, location,auditStatus,pageQuery);
        return recruits;
    }




}
