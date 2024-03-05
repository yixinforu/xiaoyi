package com.yixin.xiaoyi.recruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.recruit.entity.NewCount;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import com.yixin.xiaoyi.recruit.model.dto.RecruitDataDTO;
import com.yixin.xiaoyi.recruit.model.vo.RecruitAuditVO;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/20 10:50
 */
public interface RecruitAdminService {





    /**
     * 根据id获取具体招聘信息
     * @param id
     * @return
     */
    public Recruit getRecruit(Long id);


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
    public Page<Recruit> selectRecruitList(String searchKey, String nature, Integer lateDays, String location, Boolean auditStatus, PageQuery pageQuery);


    /**
     * 获取招聘统计数据
     * @return
     */
    public RecruitDataDTO findRecruitBaseData();


    /**
     * 删除招聘信息
     * @param jobIds
     * @return
     */
    public int deleteRecruitByIds(Long[] jobIds);


    /**
     * 批量审核招聘信息
     * @param recruitAuditVO
     * @return
     */
    public boolean auditRecruitByIds(RecruitAuditVO recruitAuditVO);


    /**
     * 删除招聘信息
     * @param recruit
     * @return
     */
    public int editRecruit(Recruit recruit);


    /**
     * 增加招聘信息
     * @param recruit
     * @return
     */
    public int addRecruit( Recruit recruit);


    /**
     * 更新计数
     * @param newCount
     * @return
     */
    public int updateCount(NewCount newCount);

    /**
     * 获取当前计数
     * @return
     */
    public NewCount selectNewCount();
}
