package com.yixin.xiaoyi.recruit.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: huangzexin
 * @date: 2023/8/12 18:38
 */
@Data
@NoArgsConstructor
public class RecruitImportVo implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 公司名称
     */
    @ExcelProperty(value = "公司名称")
    private String company;

    /**
     * 岗位
     */
    @ExcelProperty(value = "行业")
    private String industry;

    /**
     * 地点
     */
    @ExcelProperty(value = "地点")
    private String location;

    /**
     * 渠道
     */
    @ExcelProperty(value = "渠道")
    private String channel;


    /**
     * 性质
     */
    @ExcelProperty(value = "性质")
    private String nature;

}

