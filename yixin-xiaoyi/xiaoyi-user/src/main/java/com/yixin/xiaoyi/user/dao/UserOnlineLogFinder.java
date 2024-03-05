package com.yixin.xiaoyi.user.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yixin.xiaoyi.user.entity.UserOnlineLog;
import com.yixin.xiaoyi.user.dao.mapper.UserOnlineLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/17 22:13
 */
@Repository
@RequiredArgsConstructor
public class UserOnlineLogFinder {

    private final UserOnlineLogMapper userOnlineLogMapper;



    public List<UserOnlineLog> findOnlineLogsByBetween(Date start, Date end){
        return userOnlineLogMapper.selectList(
            new LambdaQueryWrapper<UserOnlineLog>()
            .apply("DATE(create_time) >= {0}",start)
            .apply("DATE(create_time) <= {0}",end)
        );
    }

    /**
     * 批量插入
     * @param userOnlineLogs
     * @return
     */
    public boolean insertBatch(Collection<UserOnlineLog> userOnlineLogs){
        return userOnlineLogMapper.insertBatch(userOnlineLogs);
    }

    /**
     * 获取最近登录的用户数据
     * @param lastLoginTime
     * @return
     */
    public List<UserOnlineLog> findOnlineLogsByLastLoginTime(Date lastLoginTime) {
        if(ObjectUtils.isEmpty(lastLoginTime)){
            return new ArrayList<>();
        }
        QueryWrapper<UserOnlineLog> wrapper = new QueryWrapper<>();
        wrapper.select("user_id","last_request_time").gt("last_request_time",lastLoginTime).isNotNull("user_id");
        List<UserOnlineLog> userOnlineLogs = userOnlineLogMapper.selectList(wrapper);
        return userOnlineLogs;
    }
}
