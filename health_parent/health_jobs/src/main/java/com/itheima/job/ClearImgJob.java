package com.itheima.job;

import com.itheima.utils.QiniuUtils;
import com.itheima.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
//查两个集合不同的value,放到set中
        Set<String> set = jedisPool.getResource().sdiff(
                RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //遍历这个set,使用迭代器去七牛云删除这些图片,然后再删除redis中的图片
        System.out.println("avavavavavavava");
        for (String pic : set) {
            QiniuUtils.deleteFileFromQiniu(pic);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }
    }
}
