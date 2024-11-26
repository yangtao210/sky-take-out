package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页对象
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        //取出分页数据
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        //调用分页方法
        PageHelper.startPage(pageNum,pageSize);
        //查询数据
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        //将分页数据和套餐数据封装到返回数据中
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 批量删除套餐
     * @param ids 套餐id列表
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //遍历套餐id列表
        ids.forEach(id->{
            //根据id查询套餐
            Setmeal setmeal = setmealMapper.getById(id);
            //如果套餐处于起售中，不能删除
            if (setmeal.getStatus()==StatusConstant.ENABLE){
                //抛出不能删除异常
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        ids.forEach(setmealId->{
            setmealMapper.deleteById(setmealId);
            setmealDishMapper.deleteBysetmealId(setmealId);
        });
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        //创建套餐对象存放套餐
        Setmeal setmeal = setmealMapper.getById(id);
        //创建套餐菜品关联对象列表
        List<SetmealDish> setmealDishList = setmealDishMapper.getBySetmealId(id);

        //创建套餐和菜品对象
        SetmealVO setmealVO = new SetmealVO();
        //将套餐拷贝进对象
        BeanUtils.copyProperties(setmeal,setmealVO);
        //修改菜品关联信息
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //创建套餐对象
        Setmeal setmeal = new Setmeal();
        //拷贝套餐信息
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //修改套餐信息
        log.info("套餐信息：{}",setmeal);
        setmealMapper.update(setmeal);

        //删除套餐关联的菜品信息
        Long setmealId = setmeal.getId();
        setmealDishMapper.deleteBysetmealId(setmealId);

        //新增菜品关联套餐信息
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        //遍历集合将套餐id写进每一个菜品套餐关联对象
        setmealDishList.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        log.info("套餐菜品关联数据：{}",setmealDishList);
        //将套餐关联菜品数据批量插入数据库
        setmealDishMapper.insertBatch(setmealDishList);
    }

    /**
     * 修改套餐你状态
     * @param status
     * @param id
     */
    public void updateStatus(Integer status, Long id) {
        //如果启用套餐，检查套餐下的菜品是否为启用状态
        if (status == StatusConstant.ENABLE){
            //根据套餐id查询菜品
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            //菜品列表不为空则循环遍历
            if (dishList != null && dishList.size()>0){
                dishList.forEach(dish->{
                    //菜品状态为禁售则抛出异常
                    if (dish.getStatus()==StatusConstant.DISABLE){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        //创建套餐对象
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        log.info("套餐对象：{}",setmeal);
        //跟新套餐对象状态信息
        setmealMapper.update(setmeal);
    }
}
