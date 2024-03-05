package com.yixin.xiaoyi.recruit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: huangzexin
 * @date: 2023/9/25 11:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecruitDayDTO {

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
     * 更新时间
     */
    private Date updateTime;
}
