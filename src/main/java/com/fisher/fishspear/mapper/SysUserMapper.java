package com.fisher.fishspear.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> list(@Param("username")String username, @Param("page") Page page);
}
