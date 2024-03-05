package com.yixin.xiaoyi.user.service.impl;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yixin.xiaoyi.common.config.UserConfig;
import com.yixin.xiaoyi.common.constant.Constants;
import com.yixin.xiaoyi.common.core.domain.PageQuery;
import com.yixin.xiaoyi.common.core.page.TableDataInfo;
import com.yixin.xiaoyi.common.core.service.LogininforService;
import com.yixin.xiaoyi.common.utils.EncryptionUtil;
import com.yixin.xiaoyi.common.utils.ServletUtils;
import com.yixin.xiaoyi.common.utils.StringUtils;
import com.yixin.xiaoyi.common.utils.ip.AddressUtils;
import com.yixin.xiaoyi.user.dao.XyLogininforFinder;
import com.yixin.xiaoyi.user.entity.XyLogininfor;
import com.yixin.xiaoyi.user.service.XyLogininforAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author admin
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class XyLogininforAdminServiceImpl implements XyLogininforAdminService, LogininforService {

    private final XyLogininforFinder xyLogininforFinder;

    /**
     * 记录登录信息
     *
     * @param maskPhone   脱敏手机号
     * @param status   状态
     * @param message  消息
     * @param args     列表
     */
    @Async
    @Override
    public void recordLogininfor(final String encryptedPhone,final String maskPhone,  final String status, final String message,
                                 HttpServletRequest request, final Object... args) {
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        final String ip = ServletUtils.getClientIP(request);

        String address = AddressUtils.getRealAddressByIP(ip);
        StringBuilder s = new StringBuilder();
        s.append(getBlock(ip));
        s.append(address);
        s.append(getBlock(status));
        s.append(getBlock(message));
        // 打印信息到日志
        log.info(s.toString(), args);
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        XyLogininfor logininfor = new XyLogininfor();
        logininfor.setMaskPhone(maskPhone);
        logininfor.setEncryptedPhone(encryptedPhone);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(address);
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus(Constants.SUCCESS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus(Constants.FAIL);
        }
        // 插入数据
        insertLogininfor(logininfor);
    }

    private String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }

    @Override
    public TableDataInfo<XyLogininfor> selectPageLogininforList(XyLogininfor logininfor, PageQuery pageQuery) {
        Page<XyLogininfor> page = xyLogininforFinder.selectPageLogininforList(logininfor, pageQuery);
        page.getRecords().forEach(xyLogininfor -> xyLogininfor.setPhone(EncryptionUtil.decryptKey(xyLogininfor.getEncryptedPhone(), UserConfig.getPhoneSecret())));
        return TableDataInfo.build(page);
    }

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(XyLogininfor logininfor) {
        logininfor.setLoginTime(new Date());
        xyLogininforFinder.insert(logininfor);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<XyLogininfor> selectLogininforList(XyLogininfor logininfor) {
        return xyLogininforFinder.selectLogininforList(logininfor);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        return xyLogininforFinder.deleteBatchIds(Arrays.asList(infoIds));
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        xyLogininforFinder.cleanLogininfor();
    }
}
