package com.fisher.fishspear.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.fisher.fishspear.entity.BusAddons;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fisher.fishspear.entity.SysAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuchen
 * @since 2019-05-23
 */
public interface BusAddonsMapper extends BaseMapper<BusAddons> {

    List<BusAddons> list(@Param("name")String name, @Param("page") Page page);

}
