package com.yixin.xiaoyi.common.core.module.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: huangzexin
 * @date: 2023/7/9 23:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DateVO {

    /**
     * 开始时间 yyyy-MM-dd
     */
    private String startDate;

    /**
     * 结束时间  yyyy-MM-dd
     */
    private String endDate;

    public DateVO(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateVO() {}
}
