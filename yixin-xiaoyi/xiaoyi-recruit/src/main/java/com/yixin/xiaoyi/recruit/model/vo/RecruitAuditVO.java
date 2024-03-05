package com.yixin.xiaoyi.recruit.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangzexin
 * @date: 2023/11/30 17:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RecruitAuditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 招聘id
     */
    private List<Long> recruitIds;

    /**
     * 审核状态
     */
    private Boolean auditFlag;

}
