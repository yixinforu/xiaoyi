package com.yixin.xiaoyi.recruit.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.yixin.xiaoyi.common.excel.ExcelListener;
import com.yixin.xiaoyi.common.excel.ExcelResult;
import com.yixin.xiaoyi.common.exception.ServiceException;
import com.yixin.xiaoyi.common.utils.spring.SpringUtils;
import com.yixin.xiaoyi.recruit.entity.Recruit;
import com.yixin.xiaoyi.recruit.service.impl.RecruitAdminServiceImpl;
import com.yixin.xiaoyi.recruit.model.vo.RecruitImportVo;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/8/12 19:02
 */
@Slf4j
public class RecruitImportListener extends AnalysisEventListener<RecruitImportVo> implements ExcelListener<RecruitImportVo> {


    private final RecruitAdminServiceImpl recruitAdminServiceImpl;

    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public RecruitImportListener(){
        this.recruitAdminServiceImpl = SpringUtils.getBean(RecruitAdminServiceImpl.class);
    }

    @Override
    public ExcelResult<RecruitImportVo> getExcelResult() {
        return new ExcelResult<RecruitImportVo>() {

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
            public List<RecruitImportVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }

    @Override
    public void invoke(RecruitImportVo recruitImportVo, AnalysisContext analysisContext) {
        try {
            if(StrUtil.isNotBlank(recruitImportVo.getCompany())) {
                Recruit recruit = BeanUtil.toBean(recruitImportVo, Recruit.class);
                recruit.setCreateTime(new Date());
                recruit.setUpdateTime(new Date());
                recruitAdminServiceImpl.addRecruit(recruit);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、公司  ").append(recruitImportVo.getCompany()).append(" 导入成功");
            }else{
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、公司 不允许为空");
            }
        }catch (Exception e){
            failureNum++;
            String msg = "<br/>" + failureNum + "、公司 " + recruitImportVo.getCompany() + " 导入失败：";
            failureMsg.append("<br/>").append(failureNum).append("、公司 ").append(recruitImportVo.getCompany()).append(" 插入失败");
            log.error(msg, e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
