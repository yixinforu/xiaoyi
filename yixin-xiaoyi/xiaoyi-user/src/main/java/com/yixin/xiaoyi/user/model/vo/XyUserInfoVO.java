package com.yixin.xiaoyi.user.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/24 22:19
 */
@Data
@NoArgsConstructor
public class XyUserInfoVO  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号或手机号
     */
    private String userIdOrPhone;

    /**
     * 角色编号
     */
    private Long roleId;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params = new HashMap<>();

}
