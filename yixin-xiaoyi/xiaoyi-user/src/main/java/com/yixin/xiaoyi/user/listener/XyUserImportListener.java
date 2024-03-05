package com.yixin.xiaoyi.user.listener;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.yixin.xiaoyi.user.entity.XyUser;
import com.yixin.xiaoyi.common.excel.ExcelListener;
import com.yixin.xiaoyi.common.excel.ExcelResult;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.helper.LoginHelper;
import com.yixin.xiaoyi.common.utils.ValidatorUtils;
import com.yixin.xiaoyi.common.utils.spring.SpringUtils;
import com.yixin.xiaoyi.common.core.module.user.vo.XyUserImportVo;

import com.yixin.xiaoyi.system.service.IXyConfigService;
import com.yixin.xiaoyi.user.service.XyUserAdminService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 系统用户自定义导入
 *
 * @author admin
 */
@Slf4j
public class XyUserImportListener extends AnalysisEventListener<XyUserImportVo> implements ExcelListener<XyUserImportVo> {

    private final XyUserAdminService userService;

    private final String password;

    private final Boolean isUpdateSupport;

    private final Long operUserId;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public XyUserImportListener(Boolean isUpdateSupport) {
        String initPassword = SpringUtils.getBean(IXyConfigService.class).selectConfigByKey("sys.user.initPassword");
        this.userService = SpringUtils.getBean(XyUserAdminService.class);
        this.password = BCrypt.hashpw(initPassword);
        this.isUpdateSupport = isUpdateSupport;
        this.operUserId = LoginHelper.getUserId();
    }

    @Override
    public void invoke(XyUserImportVo userVo, AnalysisContext context) {
        XyUser user = this.userService.selectUserByUserName(userVo.getUserName());
        try {
            // 验证是否存在这个用户
            if (ObjectUtil.isNull(user)) {
                user = BeanUtil.toBean(userVo, XyUser.class);
                ValidatorUtils.validate(user);
                userService.insertUser(user);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getMaskPhone()).append(" 导入成功");
            } else if (isUpdateSupport) {
                Long userId = user.getUserId();
                user = BeanUtil.toBean(userVo, XyUser.class);
                user.setUserId(userId);
                ValidatorUtils.validate(user);
                userService.checkUserAllowed(user);
                userService.updateUser(user);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getMaskPhone()).append(" 更新成功");
            } else {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(user.getMaskPhone()).append(" 已存在");
            }
        } catch (Exception e) {
            failureNum++;
            String msg = "<br/>" + failureNum + "、账号 " + user.getMaskPhone() + " 导入失败：";
            failureMsg.append(msg).append(e.getMessage());
            log.error(msg, e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public ExcelResult<XyUserImportVo> getExcelResult() {
        return new ExcelResult<XyUserImportVo>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<XyUserImportVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }
}
