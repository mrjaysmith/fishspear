package com.fisher.fishspear.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuchen
 * @since 2019-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    private String pids;
    private String code;
    private String pcode;
    private String name;
    private String icon;
    private String url;
    private Integer sequence;
    @TableField("is_menu")
    private Boolean isMenu;
    private Integer status;
    @TableField("is_open")
    private Boolean isOpen;
    /**
     * 是否删除，0删除， 1-未删除
     */
    private Boolean del;
    /**
     * 是否对外开放 0-否 1-是，默认1
     */
    private Boolean outside;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
