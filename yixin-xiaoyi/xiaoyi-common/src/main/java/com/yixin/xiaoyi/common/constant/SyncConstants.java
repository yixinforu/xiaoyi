package com.yixin.xiaoyi.common.constant;


/**
 * 同屏状态常量
 *
 * @author admin
 */
public interface SyncConstants {
    /**
     * 待用户接起
     */
    Integer WAITING = 0;

    /**
     * 用户接起同屏
     */
    Integer ANSWER = 1;

    /**
     * 用户关闭同屏
     */
    Integer CLIENT_CLOSE = 3;

    /**
     * 营销员关闭同屏
     */
    Integer WORKER_CLOSE = 4;

    /**
     * 用户确认温馨提示
     */
    Integer CONFIRM = 5;
}
