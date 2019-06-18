package com.fisher.fishspear.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单
 */
@Data
@Accessors(chain = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;
    /**
     * 父id
     */
    private Integer pid;
    /**
     * 菜单编码
     */
    private String name;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 跳转路径
     */
    private String jump;
    /**
     * 显示顺序
     */
    private Integer sequence;
    /**
     * 是否默认展示页面
     */
    private Boolean spread;
    /**
     * 子菜单
     */
    private List<TreeNode> list;

    /***************  以下为easyui tree数据字段 **************/
    /**
     * 菜单名称
     */
    private String text;
    /**
     * 展开或关闭：  closed open
     */
    private String state;
    /**
     * 是否选中
     */
    private Boolean checked;
    /**
     * 子菜单
     */
    private List<TreeNode> children;
}
