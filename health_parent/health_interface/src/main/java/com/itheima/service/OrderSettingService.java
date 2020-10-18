package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void upload(List<OrderSetting> orderSettingList);

    List<Map> findAll(String dateym);


    void editNumberByDate(OrderSetting orderSetting) throws Exception;
}
