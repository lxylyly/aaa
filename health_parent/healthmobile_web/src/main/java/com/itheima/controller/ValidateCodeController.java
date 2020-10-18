package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
@RequestMapping("/send4Order")
    public Result send4Order(String telephone){
    Result result=null;
    try {
        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,"123456");
        jedisPool.getResource().set(RedisMessageConstant.SENDTYPE_ORDER+telephone,"123456");
        result= new Result(true,"验证码获取成功");
    } catch (ClientException e) {
        e.printStackTrace();
    }
    return result;
}
}
