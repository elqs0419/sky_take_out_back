package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    //select setmeal_id from setmeal_dish where dish_id in (1,2,3)
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐中的菜品数据
     * @param setMealDishes
     */
    void insertBatch(List<SetmealDish> setMealDishes);

    /**
     * 根据套餐id删除套餐和菜品的关联数据
     * @param SetMealIds
     */
    void deleteBySetMealIds(List<Long> SetMealIds);

    /**
     * 根据套餐id查询套餐和菜品的关联数据
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetMealId(Long id);

    /**
     * 根据套餐id删除套餐和菜品的关联数据
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetMealId(Long id);
}
