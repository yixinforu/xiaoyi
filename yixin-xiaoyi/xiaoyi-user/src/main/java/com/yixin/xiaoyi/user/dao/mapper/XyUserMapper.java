package com.yixin.xiaoyi.user.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author admin
 */
public interface XyUserMapper extends BaseMapperPlus<XyUserMapper, XyUser, XyUser> {

    Page<XyUser> selectPageUserList(@Param("page") Page<XyUser> page, @Param(Constants.WRAPPER) Wrapper<XyUser> queryWrapper);

    /**
     * 根据条件分页查询用户列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    List<XyUser> selectUserList(@Param(Constants.WRAPPER) Wrapper<XyUser> queryWrapper);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    Page<XyUser> selectAllocatedList(@Param("page") Page<XyUser> page, @Param(Constants.WRAPPER) Wrapper<XyUser> queryWrapper);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    Page<XyUser> selectUnallocatedList(@Param("page") Page<XyUser> page, @Param(Constants.WRAPPER) Wrapper<XyUser> queryWrapper);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    XyUser selectUserByUserName(String userName);

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    XyUser selectUserByPhonenumber(String phonenumber);


    /**
     * 通过加密手机号进行查询
     * @param encryptedPhone
     * @return
     */
    XyUser selectUserByEncryptedPhone(String encryptedPhone) ;

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    XyUser selectUserById(Long userId);

}
