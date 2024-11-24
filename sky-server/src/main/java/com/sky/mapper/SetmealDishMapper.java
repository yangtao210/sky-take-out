package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    /**
     * 插入套餐菜品关联信息
     * @param setmealDishList
     */
    void insertBatch(@Param("setmealDishList") List<SetmealDish> setmealDishList);

    /**
     * 根据id删除套餐关联菜品信息
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId} ")
    void deleteBysetmealId(Long setmealId);

    /**
     * 根据套餐id查询套餐菜品关联信息
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{setmealId} ")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
