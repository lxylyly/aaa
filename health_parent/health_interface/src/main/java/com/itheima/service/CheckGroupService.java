package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[]checkitemIds);

    CheckGroup findById(Integer id);

    List<Integer> findGroupAndItem(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    boolean delete(Integer checkGroupId);

    List<CheckGroup> findAll();

}
