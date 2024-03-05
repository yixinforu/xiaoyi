package com.yixin.xiaoyi.common.core.domain.monitor.module;

import com.yixin.xiaoyi.common.utils.Arith;
import lombok.Data;

import java.io.Serializable;

/**
 * 內存相关信息
 *
 * @author ruoyi
 */
@Data
public class MemModule implements Serializable
{
    private static final long serialVersionUID = 1L;
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;


    private double usage;

}
