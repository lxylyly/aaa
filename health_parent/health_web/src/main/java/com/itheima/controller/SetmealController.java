package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
//注入JedisPool连接池,能够直接使用这个连接池,从里面获取到连接Jedis
@Autowired
private JedisPool jedisPool;

    @RequestMapping("/upload")
    //在参数中使用MultipartFile获取前端传来的文件,但是变量名必须和前端的文件名称一致
    public Result upload(MultipartFile imgFile){
        try {
            //获取参数文件名称
            String originalFilename = imgFile.getOriginalFilename();
            //将名称截取获取到后缀
            int i = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(i);
            //在使用工具类获取到随机的文件名,加上后缀
            String fileName = UUID.randomUUID().toString() + suffix;
            System.out.println("图片名称"+fileName);
            //使用工具类将文件上传七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //图片上传成功之后,将图片存入redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //响应文件名称
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[]checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
            //当点击确认之后,图片上传成功,再将这个图片存入redis中,
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
       return new Result(true,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       return setmealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
    }


    @RequestMapping("/findById")
    public Result findById(Integer setmeal_id){
        Setmeal setmeal=setmealService.findById(setmeal_id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    @RequestMapping("/findCheckgroupId")
    public Result findCheckgroupId(Integer setmeal_id){
        List<Integer>list=setmealService.findCheckgroupId(setmeal_id);
        return new Result(true,"查询中间表勾选查询组成功",list);
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[]checkgroupIds){
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }
}
