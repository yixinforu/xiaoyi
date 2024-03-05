package com.yixin.xiaoyi.recruit.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.core.mapper.BaseMapperPlus;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import org.apache.ibatis.annotations.Param;

/**
 * @author: huangzexin
 * @date: 2023/8/7 22:34
 */
public interface RecruitMapper extends BaseMapperPlus<RecruitMapper, Recruit, Recruit> {


    Page<Recruit> selectPageDeliverList(@Param("page") Page<Recruit> page, @Param(Constants.WRAPPER) Wrapper<Recruit> queryWrapper);

}
