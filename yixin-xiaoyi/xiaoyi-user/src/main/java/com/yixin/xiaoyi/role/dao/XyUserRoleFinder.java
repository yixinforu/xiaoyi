package com.yixin.xiaoyi.role.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yixin.xiaoyi.role.entity.XyUserRole;
import com.yixin.xiaoyi.role.dao.mapper.XyUserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/20 17:13
 */
@Repository
@RequiredArgsConstructor
public class XyUserRoleFinder {
    private final XyUserRoleMapper xyUserRoleMapper;

    /**
     * 获取过期的用户角色信息
     * @return
     */
    public List<XyUserRole> findExpireUserRoles(Date currentDate){
        return xyUserRoleMapper.selectList(new LambdaQueryWrapper<XyUserRole>().lt(XyUserRole::getExpireTime,currentDate));
    }


    /**
     * 获取指定时间之后未过期对应角色的用户数量
     * @param compareDate
     * @param roleId
     * @return
     */
    public Integer selectUserRoleCountByAfterTime(Long roleId,Date compareDate,Date currentDate){
        QueryWrapper<XyUserRole> wrapper = Wrappers.query();
        wrapper.eq("role_id",roleId);
        wrapper.gt(ObjectUtil.isNotNull(compareDate),"create_time",compareDate);
        wrapper.and(wq->{
            wq.isNull("expire_time")
                .or()
                .gt("expire_time",currentDate);
        });
        return Integer.valueOf(xyUserRoleMapper.selectCount(wrapper).toString());
    }

    public int deleteExpireUserRoles(Date currentDate){
        QueryWrapper<XyUserRole> wrapper = Wrappers.query();
        wrapper.lt("expire_time",currentDate);
        return xyUserRoleMapper.delete(wrapper);
    }

    public long selectCount(Long roleId){
        return xyUserRoleMapper.selectCount(new LambdaQueryWrapper<XyUserRole>().eq(XyUserRole::getRoleId, roleId));
    }

    public int deleteAuthUser(XyUserRole userRole){
        return xyUserRoleMapper.delete(new LambdaQueryWrapper<XyUserRole>()
            .eq(XyUserRole::getRoleId, userRole.getRoleId())
            .eq(XyUserRole::getUserId, userRole.getUserId()));
    }
    public XyUserRole selectUserRoleByUserIdAndRoleId(Long userId,Long roleId){
        return xyUserRoleMapper.selectVoOne(new LambdaQueryWrapper<XyUserRole>()
            .eq(XyUserRole::getUserId,userId)
            .eq(XyUserRole::getRoleId,roleId));
    }
    public int deleteRolesByUserId(Long userId){
        return xyUserRoleMapper.delete(new LambdaQueryWrapper<XyUserRole>().eq(XyUserRole::getUserId, userId));
    }
    public int deleteUserByIds(List<Long> userIds){
        return xyUserRoleMapper.delete(new LambdaQueryWrapper<XyUserRole>().in(XyUserRole::getUserId, userIds));
    }

    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return xyUserRoleMapper.delete(new LambdaQueryWrapper<XyUserRole>()
            .eq(XyUserRole::getRoleId, roleId)
            .in(XyUserRole::getUserId, Arrays.asList(userIds)));
    }

    public int insertUserRole(XyUserRole userRole){
        return xyUserRoleMapper.insert(userRole);
    }

    public boolean insertBatch(List<XyUserRole> list){
        return xyUserRoleMapper.insertBatch(list);
    }

    public List<Long> selectUserIdsByRoleId(Long roleId){
        return xyUserRoleMapper.selectUserIdsByRoleId(roleId);
    }
}
