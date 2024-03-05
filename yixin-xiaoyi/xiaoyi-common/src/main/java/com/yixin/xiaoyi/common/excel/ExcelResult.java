package com.yixin.xiaoyi.common.excel;

import java.util.List;

/**
 * excel返回对象
 *
 * @author admin
 */
public interface ExcelResult<T> {

    /**
     * 对象列表
     */
    List<T> getList();

    /**
     * 错误列表
     */
    List<String> getErrorList();

    /**
     * 导入回执
     */
    String getAnalysis();
}
