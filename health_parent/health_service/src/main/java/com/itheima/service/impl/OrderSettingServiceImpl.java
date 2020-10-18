package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.CheckItemService;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void upload(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
            int i = orderSettingDao.upload_findDate(orderSetting);
            if (i > 0) {
                orderSettingDao.upload_update(orderSetting);
            } else {
                orderSettingDao.upload_add(orderSetting);
            }
        }
    }

    @Override
    public List<Map> findAll(String dateym) {
        Map<String, String> mapDate = new HashMap<>();
        mapDate.put("startMonth", dateym + "-1");
        mapDate.put("endMonth", dateym + "-31");
        List<OrderSetting> orderSettingList = orderSettingDao.findAll(mapDate);

        List<Map> mapList = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map map = new HashMap();
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting)throws Exception {

        OrderSetting orderSetting01 = orderSettingDao.findById(orderSetting.getOrderDate());
        if (orderSetting01 != null) {
            if (orderSetting.getNumber() < orderSetting01.getReservations()) {
                throw new Exception("输入可预约人数不能小于已预约人数");
            } else {
                orderSettingDao.editNumberByDate_update(orderSetting);
            }
        } else {
            orderSettingDao.editNumberByDate_add(orderSetting);
        }
    }
}
