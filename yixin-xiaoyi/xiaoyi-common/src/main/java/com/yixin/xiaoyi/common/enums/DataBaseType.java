package com.yixin.xiaoyi.common.enums;

import com.yixin.xiaoyi.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库类型
 *
 * @author admin
 */
@Getter
@AllArgsConstructor
public enum DataBaseType {

    /**
     * MySQL
     */
    MY_SQL("MySQL"),



    /**
     * SQL Server
     */
    SQL_SERVER("Microsoft SQL Server");

    private final String type;

    public static DataBaseType find(String databaseProductName) {
        if (StringUtils.isBlank(databaseProductName)) {
            return null;
        }
        for (DataBaseType type : values()) {
            if (type.getType().equals(databaseProductName)) {
                return type;
            }
        }
        return null;
    }
}
