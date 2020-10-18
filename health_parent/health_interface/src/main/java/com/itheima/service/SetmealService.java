package com.itheima.service;

import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[]checkgroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    Setmeal findById(Integer setmeal_id);

    List<Integer> findCheckgroupId(Integer setmeal_id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> getSetmeal();

    Setmeal findByIdS(Integer setmeal_id);


}
