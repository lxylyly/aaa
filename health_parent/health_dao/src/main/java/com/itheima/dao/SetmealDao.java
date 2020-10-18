package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add_setmeal(Setmeal setmeal);

    void add_s_c(Map<String,Integer>map);

    Page<Setmeal> findPage(String queryString);

    Setmeal findById(Integer setmeal_id);

    List<Integer> findCheckgroupId(Integer setmeal_id);

    void edit_update(Setmeal setmeal);

    void edit_delete(Integer id);

    List<Setmeal> findAll();

    List<Setmeal> getSetmeal();

    Setmeal findByIdS(Integer setmeal_id);
}
