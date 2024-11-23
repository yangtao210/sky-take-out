package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据id删除口味数据
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId} ")
    void deleteByshId(Long dishId);

    /**
     * 根据菜品id查询口味数据
     * @param DishId
     * @return
     */
    @Select("select * from dish_flavor where  dish_id=#{DishId} ")
    List<DishFlavor> getByDishId(Long DishId);
}
