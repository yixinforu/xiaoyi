package com.yixin.xiaoyi.recruit.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yixin.xiaoyi.common.annotation.ExcelDictFormat;
import com.yixin.xiaoyi.common.convert.ExcelDictConvert;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/7 22:16
 */
@Data
@Accessors(chain = true)
@TableName("recruit")
@ExcelIgnoreUnannotated
public class Recruit extends BaseEntity {

    /**
     * 招聘id
     */
    @ExcelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    /**
     * 审核状态：0-未审核/1-已审核
     */
    @ExcelProperty(value = "审核状态",converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "true=已审核,false=未审核")
    private Boolean auditFlag;

    /**
     * 逻辑删除：0-未删/1-已删
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 发布时间
     */
    @ExcelProperty(value = "发布时间")
    private Date releaseTime;










}
