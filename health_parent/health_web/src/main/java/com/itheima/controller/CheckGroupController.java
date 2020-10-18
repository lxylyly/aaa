package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//@RestController是@ResponseBody(接收的格式是Json)+@Controller的结合
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[]checkitemIds){
        checkGroupService.add(checkGroup,checkitemIds);
        System.out.println("11111");
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        System.out.println("11111");
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS,checkGroup);
    }

    @RequestMapping("/findGroupAndItem")
    public Result findGroupAndItem(Integer id){
        List<Integer>checkitemIds= checkGroupService.findGroupAndItem(id);
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS,checkitemIds);
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[]checkitemIds){
        checkGroupService.edit(checkGroup,checkitemIds);
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       return checkGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
    }

   @RequestMapping("/delete")
    public Result delete(Integer checkGroupId){
        boolean b=checkGroupService.delete(checkGroupId);
       System.out.println("111");
        if (b){
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }else{
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> list = checkGroupService.findAll();
        if (list!=null&&list.size()>0){
            return new Result(true,"查询成功",list);
        }else{
            return new Result(false,"查询失败");
        }
    }
}
