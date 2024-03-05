package com.yixin.xiaoyi.user.service;

import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.module.common.vo.DateVO;
import com.yixin.xiaoyi.user.model.dto.ClientIpTrendDTO;
import com.yixin.xiaoyi.user.model.dto.UserDTO;
import com.yixin.xiaoyi.user.model.dto.UserDataDTO;
import com.yixin.xiaoyi.user.model.dto.UserInfoDTO;
import com.yixin.xiaoyi.user.model.vo.DisableVo;
import com.yixin.xiaoyi.user.model.vo.EmpowerVO;
import com.yixin.xiaoyi.user.model.vo.XyUserInfoVO;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;

import java.util.Date;
import java.util.List;

/**
 * 用户 业务层
 *
 * @author admin
 */
public interface XyUserAdminService {


    TableDataInfo<UserDTO> selectPageUserList(XyUserInfoVO user, PageQuery pageQuery);


    /**
     * 授权
     * @param empowerVO
     * @return
     */
    int empower(EmpowerVO empowerVO);


    /**
     * 在原来vip基础上进行叠加授权
     * @param empowerVO
     * @return
     */
    int increaseEmpower(EmpowerVO empowerVO);

    /**
     * 封禁用户
     * @param disableVo
     * @return
     */
    void disableUser(DisableVo disableVo);

    /**
     * 解除封禁
     * @param userId
     */
    void untieDisableUser(Long userId);

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<XyUser> selectUserList(XyUser user);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    TableDataInfo<XyUser> selectAllocatedList(XyUser user, PageQuery pageQuery);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    TableDataInfo<XyUser> selectUnallocatedList(XyUser user, PageQuery pageQuery);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    XyUser selectUserByUserName(String userName);


    /**
     * 通过手机号进行查找
     * @param encryptedPassword
     * @return
     */
    XyUser selectUserByEncryptedPassword(String encryptedPassword);



    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    XyUser selectUserById(Long userId);




    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    String selectUserRoleGroup(String userName);


    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkPhoneUnique(XyUser user);


    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    void checkUserAllowed(XyUser user);


    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(XyUser user);

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean registerUser(XyUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(XyUser user);

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUserStatus(XyUser user);

    /**
     * 更新用户密钥
     *
     */
    int updateUserSecretKey(Long userId, String secretKey);

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUserProfile(XyUser user);


    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    int resetPwd(XyUser user);


    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);


    /**
     * 获取用户基本数据
     * @return
     */
    UserDataDTO findUserCountData();


    /**
     * 获取Ip访问趋势
     * @param date
     * @return
     */
    List<ClientIpTrendDTO> findClientIpTrendData(DateVO date);

}
