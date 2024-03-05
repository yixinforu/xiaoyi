package com.yixin.xiaoyi.recruit.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: huangzexin
 * @date: 2023/8/7 23:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("recruit_user_deliver")
@ExcelIgnoreUnannotated
public class RecruitUserDeliver extends BaseEntity {

    /**
     * 用户-招聘关联表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 招聘id
     */
    private Long recruitId;


}
