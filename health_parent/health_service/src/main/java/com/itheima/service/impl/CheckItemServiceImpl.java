package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> list = checkItemDao.findAll();
        return list;
    }

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //设置参数
        PageHelper.startPage(currentPage, pageSize);
        //调用dao,组件会返回两个需要的total和rows
        Page<CheckItem> daoPage = checkItemDao.findPage(queryString);
        //获取返回值结果PageResult
return new PageResult(daoPage.getTotal(),daoPage.getResult());
    }

    @Override
    public CheckItem findById(Integer checkitemId) {
      return   checkItemDao.findById(checkitemId);
    }

    @Override
    public void updata(CheckItem checkItem) {
        checkItemDao.updata(checkItem);
    }

}
