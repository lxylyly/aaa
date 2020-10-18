package com.itheima.dao;

import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Order> orderS(Order order);

    void orderAdd(Order order);

    Map findById(Integer id);

    Integer thisWeekOrderNumber(Map map01);

    Integer thisMonthVisitsNumber(Map map02);

    List<Map> hotSetmeal();

    List<Map<Object,String>> Order_Setmeal_Name_number();

}
