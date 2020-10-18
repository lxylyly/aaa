package com.itheima.service;

import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    List<CheckItem>findAll();

    void add(CheckItem checkItem);


    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    CheckItem findById(Integer checkitemId);

    void updata(CheckItem checkItem);
}
