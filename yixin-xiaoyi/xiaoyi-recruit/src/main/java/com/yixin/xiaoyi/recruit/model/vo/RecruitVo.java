package com.yixin.xiaoyi.recruit.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: huangzexin
 * @date: 2023/8/12 19:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RecruitVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultipartFile file;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
