package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CheckItemDao {
    List<CheckItem>findAll();

    void add(CheckItem checkItem);

    Page<CheckItem>findPage(String queryString);

    CheckItem findById(Integer checkitemId);

    void updata(CheckItem checkItem);

    List<CheckItem> findCheckItemListById(Integer checkGroupId);
}
