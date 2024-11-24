package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id修改套餐状态
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    static void update(Setmeal setmeal) {
    }

    /**
     * 新增套餐数据
     * @param setmeal 套餐对象
     */
    void insert(Setmeal setmeal);
}
