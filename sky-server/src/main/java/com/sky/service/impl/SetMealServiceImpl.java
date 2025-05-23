package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.insert(setmeal);
        Long setMealId = setmeal.getId();
        List<SetmealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if(setMealDishes != null && !setMealDishes.isEmpty()){
            setMealDishes.forEach(setMealDish -> {
                setMealDish.setSetmealId(setMealId);
            });
            setMealDishMapper.insertBatch(setMealDishes);
        }
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.pageQuery(setmealPageQueryDTO);

        long total = page.getTotal();
        List<SetmealVO> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = setMealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new RuntimeException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setMealMapper.deleteBatch(ids);
        setMealDishMapper.deleteBySetMealIds(ids);


    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setMealMapper.getById(id);
        List<SetmealDish> setmealDishes = setMealDishMapper.getBySetMealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal  setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.update(setmeal);
        setMealDishMapper.deleteBySetMealId(setmealDTO.getId());
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(setMealDish -> {
                setMealDish.setSetmealId(setmealDTO.getId());
            });
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 开启或关闭套餐
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setMealMapper.update(setmeal);
    }
}
