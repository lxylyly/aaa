package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
@Reference
    private OrderSettingService orderSettingService;
@RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile file){
    try {
        List<String[]> strings = POIUtils.readExcel(file);
        if (strings!=null&&strings.size()>0){
            List<OrderSetting>orderSettingList=new ArrayList<>();
            for (String[] string : strings) {
                //创建一个OrderSetting对象,将日期和可预约人数直接放入满惨构造方法中
                OrderSetting orderSetting=new OrderSetting(new Date(string[0]),Integer.parseInt(string[1]));
                orderSettingList.add(orderSetting);
            }
        orderSettingService.upload(orderSettingList);
        }
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS);
    } catch (IOException e) {
        e.printStackTrace();
        return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
    }
}

@RequestMapping("/findAll")
    public Result findAll(String dateym){
    //数据为什么要封装成Map返回?根据假数据得知,因为页面要展示的是 date,number,reservations 对象中多了一个id,所以不能用对象
    //封装; 也可以自己再创建一个类来new对象封装,但是太麻烦, 所以直接用map方便
    List<Map>list=orderSettingService.findAll(dateym);
    return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
}

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"输入可预约人数不能小于已预约人数");
        }
    }
}
