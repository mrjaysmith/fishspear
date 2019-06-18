package com.fisher.fishspear.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.SysAdmin;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuchen
 * @since 2019-05-14
 */
public interface SysAdminMapper extends BaseMapper<SysAdmin> {

    List<SysAdmin> list(@Param("username")String username, @Param("page") Page page);

}
