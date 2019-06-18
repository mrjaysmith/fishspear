package com.fisher.fishspear.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * @since 2019-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_addons")
public class BusAddons extends Model<BusAddons> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 插件名称
     */
    private String name;
    /**
     * 创建日期
     */
    private Date created;
    /**
     * 更新日期
     */
    private Date updated;
    /**
     * 所有人
     */
    private String owner;
    /**
     * 插件页
     */
    private String info;
    private String version;
    @TableField("addons_version")
    private String addonsVersion;
    private Integer downloads;
    @TableField("download_url")
    private String downloadUrl;
    /**
     * 是否最高版本
     */
    private Boolean latest;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
