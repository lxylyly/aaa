package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        try {
            //取出验证码,判断验证码是否正确
        String telephone = (String) map.get("telephone");
        String code = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER+telephone);
        if (!code.equals(map.get("validateCode"))){
            return new Result(false,"验证码错误");
        }
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
            return orderService.submit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"其他未知错误");
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        return orderService.findById(id);
    }
}
