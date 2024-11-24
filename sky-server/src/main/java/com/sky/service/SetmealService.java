package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SetmealService {

    /**
     * 新增套餐,同时需要保存套餐的关联关系
     *
     * @param setmealDTO
     */
    void savewithDish(SetmealDTO setmealDTO);
}
