package com.yixin.xiaoyi.recruit.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yixin.xiaoyi.common.annotation.ExcelDictFormat;
import com.yixin.xiaoyi.common.convert.ExcelDictConvert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: huangzexin
 * @date: 2023/9/20 12:01
 */
@Data
public class RecruitListDTO {

    /**
     * 招聘id
     */
    private Long id;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 行业
     */
    private String industry;

    /**
     * 地点
     */
    private String location;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 性质
     */
    private String nature;


    /**
     * 发布时间
     */
    private Date releaseTime;


    /**
     * 是否投递
     */
    private Boolean delivered;
}
