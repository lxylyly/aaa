package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Result submit(Map map)throws Exception {
        //获取参数(要用到的参数,都是从map里面获取的,有的需要转型)
        String orderDate = (String) map.get("orderDate");//获取预约日期
        String telephone = (String) map.get("telephone");//获取手机号码
        String name = (String) map.get("name");//获取姓名
        String sex = (String) map.get("sex");//获取性别
        String idCard = (String) map.get("idCard");//获取身份证
        String orderType = (String) map.get("orderType");//获取预约类型
        String setmealId = (String) map.get("setmealId");//获取套餐id
        //{"setmealId":"12","sex":"2","orderDate":"2020-08-13","name":"张三",
        //      "telephone":"15572184803","validateCode":"123445","idCard":"110101199003076317"}
        //1.判断当前日期 是否可以预约 (1.根据预约日期查询预约表是否为空)
        //将String类型的date转为Date类型
        Date orderDateNew = DateUtils.parseString2Date(orderDate);
        //1,当前日期是否可预约,查ordersetting表,当前日期看返回值是否为空;
        //返回值为OrderSetting,后面还会用到OrderSetting中的属性
        OrderSetting orderSetting = orderSettingDao.date(orderDate);
        //判断是否为空
        if (orderSetting == null) {
            return new Result(false, "当前日期不可预约");
        }
        //2,判断当前日期人数是否有预约满,通过ordersetting表查询,上面查过了,直接从返回的对象里面调用
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, "当前日期已约满");
        }
        //3,判断用户是否会员,通过member表,用户手机号,查询是否存在
        Member member = memberDao.isMember(telephone);
        if (member == null) {
            //不是会员就注册会员
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.addMember(member);
        } else {
            //是会员就判断是否重复预约
            //查询ordersetting表里面有没有数据,使用会员id,预约日期,套餐id
            Integer memberId = member.getId();
            Order order = new Order();
            order.setMemberId(memberId);
            order.setSetmealId(Integer.parseInt(setmealId));
            order.setOrderDate(orderDateNew);
            List<Order> orderList = orderDao.orderS(order);
            if (orderList != null && orderList.size()>0) {
                return new Result(false, "不可重复预约");
            }
        }
        //所有的条件都满足就向预约表插入一条预约数据
        Integer memberId = member.getId();
        Order order = new Order();
        order.setMemberId(memberId);
        order.setSetmealId(Integer.parseInt(setmealId));
        order.setOrderDate(orderDateNew);
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.orderAdd(order);
        //在预约日期表将已预约人数+1 , 修改数据库
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.orderSettingUpdate(orderSetting);
        //发送短信通知用户
        SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);//orderDate:2020-08-01
        System.out.println("发送预约通知短信成功了。。。");
        //响应
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    public Result findById(Integer id) {
        Map map=new HashMap();
        map=orderDao.findById(id);
        System.out.println(map);
        return new Result(true,"展示预约成功页面",map);
    }


}
