package com.yixin.xiaoyi.role.dao.mapper;

import com.yixin.xiaoyi.common.core.mapper.BaseMapperPlus;
import com.yixin.xiaoyi.role.entity.XyUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author admin
 */
public interface XyUserRoleMapper extends BaseMapperPlus<XyUserRoleMapper, XyUserRole, XyUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
