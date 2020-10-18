package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    @Override
    public List<Integer> findGroupAndItem(Integer id) {
        return checkGroupDao.findGroupAndItem(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        edit_delete(checkGroup.getId());
        checkGroupDao.edit(checkGroup);
        edit2(checkGroup.getId(), checkitemIds);
    }


    public void edit_delete(Integer checkgroupId) {
        checkGroupDao.edit_delete(checkgroupId);
    }

    public void edit2(Integer checkgroupId, Integer[] checkitemIds) {
        //循环查询项将查询项id和查询组id分别封装到map中,(查询组和查询项时一对多的关系,就是一个查询组id会对应多个查询项)
        //就是在中标中,查询组id会出现多次,(例如一个查询组19对应查询项的28,29,30--->因为表格是一对一的格式存在的)
        //所以要循环得出表格一对一的格式
        for (Integer checkitemId : checkitemIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("checkgroup_id", checkgroupId);
            map.put("checkitem_id", checkitemId);
            checkGroupDao.edit2(map);
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //设置参数
        PageHelper.startPage(currentPage, pageSize);
        //调用dao
        Page<CheckGroup> groupPage = checkGroupDao.findPage(queryString);
        return new PageResult(groupPage.getTotal(), groupPage.getResult());
    }

    @Override
    public boolean delete(Integer checkGroupId) {
        Integer i1 = checkGroupDao.findById_cc(checkGroupId);
        Integer i2 = checkGroupDao.findById_sc(checkGroupId);
        if (i1 > 0 || i2 > 0) {
            return false;
        } else {
            checkGroupDao.delete(checkGroupId);
            return true;
        }
    }

    @Override
    public List<CheckGroup> findAll() {
       return checkGroupDao.findAll();
    }

}
