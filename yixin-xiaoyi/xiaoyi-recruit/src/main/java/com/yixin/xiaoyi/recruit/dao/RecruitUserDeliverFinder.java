package com.yixin.xiaoyi.recruit.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yixin.xiaoyi.recruit.entity.RecruitUserDeliver;
import com.yixin.xiaoyi.recruit.dao.mapper.RecruitUserDeliverMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yixin
 * @date: 2023/3/8 16:58
 */
@Repository
@RequiredArgsConstructor
public class RecruitUserDeliverFinder {

    private final RecruitUserDeliverMapper recruitUserDeliverMapper;


    /**
     * 获取用户已经投递的招聘信息数据
     * @return
     */
    public List<RecruitUserDeliver> findByUserId(Long userId,List<Long> recruitIds){
        if(ObjectUtil.isNull(userId)){
            return Collections.emptyList();
        }
        QueryWrapper<RecruitUserDeliver> wrapper = new QueryWrapper<>();
        HashMap<String,Object> map = new HashMap<String,Object>(){{
            put("user_id",userId);
        }};
        wrapper.allEq(map);
        wrapper.in(CollectionUtils.isNotEmpty(recruitIds),"recruit_id",recruitIds);
        return recruitUserDeliverMapper.selectList(wrapper);
    }

    /**
     * 通过用户id和招聘id进行查找
     * @param userId
     * @param jobId
     * @return
     */
    public RecruitUserDeliver findByUserIdAndJobId(Long userId,Long jobId){
        if(ObjectUtil.isNull(userId)|| ObjectUtil.isNull(jobId)){
            return null;
        }
        QueryWrapper<RecruitUserDeliver> wrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<String,Object>(){
            {
                put("user_id",userId);
                put("recruit_id",jobId);
            }
        };
        wrapper.allEq(map);
        return recruitUserDeliverMapper.selectOne(wrapper);
    }

    /**
     * 新增
     * @param jobUserDeliver
     * @return
     */
    public int insert(RecruitUserDeliver jobUserDeliver){
        if(jobUserDeliver==null){
            return -1;
        }
        return recruitUserDeliverMapper.insert(jobUserDeliver);
    }

    /**
     * 修改
     * @param jobUserDeliver
     * @return
     */
    public int update(RecruitUserDeliver jobUserDeliver){
        if(jobUserDeliver==null){
            return -1;
        }
        return recruitUserDeliverMapper.updateById(jobUserDeliver);
    }

    /**
     * 根据用户id和招聘id修改
     * @param userId
     * @param recruitId
     * @return
     */
    public int deleteByUserIdAndRecruitId(Long userId,Long recruitId){
        QueryWrapper<RecruitUserDeliver> wrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<String,Object>(){
            {
                put("user_id",userId);
                put("recruit_id",recruitId);
            }
        };
        wrapper.allEq(map);
        return recruitUserDeliverMapper.delete(wrapper);
    }
 }
