package com.yixin.xiaoyi.role.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yixin.xiaoyi.common.annotation.ExcelDictFormat;
import com.yixin.xiaoyi.common.constant.UserConstants;
import com.yixin.xiaoyi.common.convert.ExcelDictConvert;
import com.yixin.xiaoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * 角色表 xy_role
 *
 * @author admin
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("xy_role")
@ExcelIgnoreUnannotated
public class XyRole extends BaseEntity {

    /**
     * 角色ID
     */
    @ExcelProperty(value = "角色序号")
    @TableId(value = "role_id",type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    @ExcelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /**
     * 角色权限
     */
    @ExcelProperty(value = "角色权限")
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    private String roleKey;

    /**
     * 角色排序
     */
    @ExcelProperty(value = "角色排序")
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;



    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private Boolean menuCheckStrictly;


    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    @TableField(exist = false)
    private boolean flag = false;

    /**
     * 菜单组
     */
    @TableField(exist = false)
    private Long[] menuIds;


    /**
     * 角色菜单权限
     */
    @TableField(exist = false)
    private Set<String> permissions;

    public XyRole(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.roleId);
    }

    /**
     * 用户角色过期时间
     */
    @TableField(exist = false)
    private Date roleExpireTime;
}
