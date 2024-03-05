package com.yixin.xiaoyi.user.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: huangzexin
 * @date: 2023/8/28 23:30
 */
@Data
public class EmpowerVO  implements Serializable {

    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不允许为空")
    private Long userId;


    /** -1:永久 0：无
     * 授权期限
     */
    @NotNull(message = "授权期限不允许为空")
    private Integer vipDays;

}
