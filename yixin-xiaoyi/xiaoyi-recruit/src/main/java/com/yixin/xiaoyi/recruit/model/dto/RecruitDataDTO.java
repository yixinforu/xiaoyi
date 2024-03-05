package com.yixin.xiaoyi.recruit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: huangzexin
 * @date: 2023/9/3 20:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecruitDataDTO {

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
     * 待审核数
     */
    private Integer todoAuditCount;


    /**
     * 已审核数据量
     */
    private Integer auditCount;

}
