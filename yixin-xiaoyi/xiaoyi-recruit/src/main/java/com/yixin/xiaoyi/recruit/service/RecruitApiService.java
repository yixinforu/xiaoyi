package com.yixin.xiaoyi.recruit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import com.yixin.xiaoyi.recruit.entity.RecruitUserDeliver;
import com.yixin.xiaoyi.recruit.model.dto.RecruitDayDTO;
import com.yixin.xiaoyi.recruit.model.dto.RecruitListDTO;
import com.yixin.xiaoyi.recruit.model.vo.RecruitUserDeliverVO;

import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/20 10:51
 */
public interface RecruitApiService {


    /**
     * 获取招聘信息
     * @param searchKey 职位
     * @param nature 性质
     * @param lateDays  发布时间 最近几天
     * @param location 位置
     * @return
     */
    public Page<RecruitListDTO> selectRecruitList(String searchKey, String nature, Integer lateDays, String location, PageQuery pageQuery,String signature,Long timestamp);


    /**
     * 游客获取数据
     * @param searchKey
     * @param nature
     * @param lateDays
     * @param location
     * @param pageQuery
     * @return
     */
    public Page<RecruitListDTO> selectRecruitListByTourist(String searchKey, String nature, Integer lateDays, String location, PageQuery pageQuery,String signature,Long timestamp);

    /**
     * 编辑 用户-招聘关联
     * @param recruitUserDeliverVO
     * @return
     */
    public int editUserDelivered(RecruitUserDeliverVO recruitUserDeliverVO);


    /**
     * 获取用户的投递信息
     * @param pageQuery
     * @return
     */
    public Page<RecruitListDTO> selectPageDeliverList(PageQuery pageQuery);


    /**
     * 获取当天的信息数据
     * @return
     */
    public RecruitDayDTO findTodayRecruitData();
}
