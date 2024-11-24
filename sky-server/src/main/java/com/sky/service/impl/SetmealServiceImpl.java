package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增套餐，同时需要保存菜品和套餐的关联关系
     * @param setmealDTO
     */
    @Transactional
    public void savewithDish(SetmealDTO setmealDTO) {
        //创建套餐对象
        Setmeal setmeal = new Setmeal();
        //将前端传过来的数据进行拷贝
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //将套餐数据插入数据库
        setmealMapper.insert(setmeal);

        //获取生成的套餐id
        Long setmealId = setmeal.getId();
        //创建对象集合存放套餐菜品关联信息
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        //遍历集合将套餐id写进每一个菜品套餐关联对象
        setmealDishList.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        log.info("套餐菜品关联数据：{}",setmealDishList);
        //将套餐关联菜品数据批量插入数据库
        setmealDishMapper.insertBatch(setmealDishList);
    }
}
