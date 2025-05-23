package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAKey;
import java.util.List;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("套餐新增：{}",setmealDTO);
        setMealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询，参数为：{}",setmealPageQueryDTO);
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除套餐，ids：{}",ids);
        setMealService.deleteBatch(ids);

        return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("根据id查询套餐：{}",id);
        SetmealVO setmealVO = setMealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}",setmealDTO);
        setMealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("套餐起售停售：{},{}",status,id);
        setMealService.startOrStop(status,id);
        return Result.success();
    }

}
