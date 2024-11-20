package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 数据库查询语句接口
 */
@Mapper
public interface SetmealDishMapper {

    /**
     *根据菜品id查询对应的套餐id
     * @param dishIds
     * @return
     */
    List<Long> getsetmealIdsByDishIds(@Param("dishIds")List<Long> dishIds);
}
