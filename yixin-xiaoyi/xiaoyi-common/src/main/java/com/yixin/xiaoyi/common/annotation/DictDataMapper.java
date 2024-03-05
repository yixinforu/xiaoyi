package com.yixin.xiaoyi.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yixin.xiaoyi.common.jackson.DictDataJsonSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典数据映射注解
 *
 * @author itino
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JacksonAnnotationsInside
@JsonSerialize(using = DictDataJsonSerializer.class)
public @interface DictDataMapper {

    /**
     * 设置字典的type值 (如: xy_user_sex)
     */
    String dictType() default "";
}
