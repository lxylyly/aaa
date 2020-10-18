package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    int upload_findDate(OrderSetting orderSetting);

    void upload_update(OrderSetting orderSetting);

    void upload_add(OrderSetting orderSetting);

    List<OrderSetting> findAll(Map<String,String> mapDate);

    OrderSetting findById(Date orderDate);

    void editNumberByDate_update(OrderSetting orderSetting);

    void editNumberByDate_add(OrderSetting orderSetting);

    OrderSetting date(String orderDate);

    void orderSettingUpdate(OrderSetting orderSetting);
}
