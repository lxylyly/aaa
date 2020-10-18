package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import org.springframework.http.HttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {
    @Reference
    private OrderService orderService;
    @Reference
    private SetmealService setmealService;

//根据id去查询预约信息,套餐id

    //根据套餐id去查询,套餐名字,套餐数量,套餐占比,备注;返回的是List<Map>4条

    //两头一体
    //生成pdf(5步骤)创建文档,实例化放入地址,打开文档,添加内容,关闭
    //设置表格字体
    //添加数据到文档中(添加的是参数部分)
    //生成表格,用来存放热门套餐list
    //写表头,写数据 写到表中
    //添加表到文档中
    //关闭资源
}
    // 传递内容和字体样式，生成单元格

