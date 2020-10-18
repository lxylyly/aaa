package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String,Integer> map);

    CheckGroup findById(Integer id);

    List<Integer> findGroupAndItem(Integer id);

    void edit(CheckGroup checkGroup);

    void edit_delete(Integer checkgroupId);

    void edit2(Map<String, Integer> map);

    Page<CheckGroup>findPage(String queryString);


    Integer findById_cc(Integer checkGroupId);

    Integer findById_sc(Integer checkGroupId);

    void delete(Integer checkGroupId);

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupListById(Integer setmeal_id);
}
