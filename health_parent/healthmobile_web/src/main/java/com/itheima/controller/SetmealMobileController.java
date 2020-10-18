package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    private SetmealService setmealService;

    @RequestMapping(value = "/getSetmeal",method = RequestMethod.POST)
    public Result getSetmeal(){
        List<Setmeal>list =setmealService.getSetmeal();
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
Setmeal setmeal=setmealService.findByIdS(id);
return new Result(true,"查询套餐详情成功",setmeal);
    }
}
