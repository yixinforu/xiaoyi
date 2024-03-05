package com.yixin.xiaoyi.user.service;

import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.user.entity.XyLogininfor;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author admin
 */
public interface XyLogininforAdminService {


    TableDataInfo<XyLogininfor> selectPageLogininforList(XyLogininfor logininfor, PageQuery pageQuery);

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    void insertLogininfor(XyLogininfor logininfor);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    List<XyLogininfor> selectLogininforList(XyLogininfor logininfor);

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    void cleanLogininfor();
}
