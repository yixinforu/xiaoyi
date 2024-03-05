package com.yixin.xiaoyi.recruit.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 由于新增数据属于动态，暂时没开发"发布计划“功能，故先手动输入
 * @author: huangzexin
 * @date: 2023/11/10 13:11
 */
@Data
@Accessors(chain = true)
@TableName("new_count")
public class NewCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;
    /**
     * 新增总数
     */
    private Integer newTotalCount;

    /**
     * 新增校招数
     */
    private Integer newCampusCount;


    /**
     * 新增实习数
     */
    private Integer newPracticeCount;


    /**
     * 展示更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date showUpdateTime;

}
