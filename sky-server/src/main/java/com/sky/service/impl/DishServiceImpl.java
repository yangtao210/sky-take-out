package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    public void saceWithDishFlavor(DishDTO dishDTO) {
        // 创建一个Dish对象
        Dish dish = new Dish();
        // 将dishDTO中的属性值复制到dish对象中
        BeanUtils.copyProperties(dishDTO, dish);
        // 将dish对象插入到数据库中
        dishMapper.insert(dish);
        // 获取插入后的dish对象的id
        Long dishId = dish.getId();
        // 获取dishDTO中的flavors属性
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 如果flavors不为空且长度大于0
        if (flavors != null && flavors.size() > 0) {
            // 遍历flavors
            flavors.forEach(dishFlavor -> {
                // 设置dishFlavor的dishId属性为dishId
                dishFlavor.setDishId(dishId);
            });
            // 将flavors批量插入到数据库中
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分类查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //使用PageHelper插件开启分页功能，传入页码和每页显示数作为参数
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //从数据库中查询分页的彩品信息，并将结果封装到page<DishVo>对象中
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        //创建一个分页查询对象，并将总记录数，和查询后返回的菜品对象封装到pageResult中
        PageResult pageResult=new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }
}
