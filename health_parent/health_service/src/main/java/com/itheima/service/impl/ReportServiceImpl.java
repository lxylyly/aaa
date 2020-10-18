package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.ReportDao;
import com.itheima.pojo.Member;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map getSetmealReport() {
        Map<String,Object> map = new HashMap<>();
        List<Map<Object,String>>setmealCount=orderDao.Order_Setmeal_Name_number();
        map.put("setmealCount",setmealCount);

        List<String>setmealNames=new ArrayList<>();
        for (Map nameCount : setmealCount) {
            setmealNames.add((String) nameCount.get("name"));
        }
        map.put("setmealNames",setmealNames);
        return map;
    }

    @Override
    public Map getMemberReport() {
        //向map中添加key的时候一定要对应上页面上的展示的key名称
        Map map=new HashMap();
        //获取一个日历
        Calendar calendar = Calendar.getInstance();
        //将去年的12个月放到日历中
        calendar.add(Calendar.MONTH,-12);
        //创建一个List来装这个12月的数据
        List<String>list=new ArrayList<>();
        //遍历然后将日历的数据,通过解析成String加到List中去
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
        list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        //将月份先放到map中,前端需要的第一组数据就是去年到现在月份
        map.put("months",list);
        //创建以个list用来添加去年每月会员数量
        List<Integer>listMember=new ArrayList<>();
        //现在这个list就是12个月份的String格式,现在遍历然后去dao中查询每个月对应的会员数量
        for (String regTime : list) {
            //将格式转换为yyyy-MM-dd的格式,才能到数据库中查到对应的信息
            regTime=regTime+"-31";
            listMember.add(memberDao.findMemberBayMonths(regTime));
        }
        map.put("memberCount",listMember);
        return map;
    }

    @Override
    public Map getBusinessReportData() {
        Map map=new HashMap();
        try {
            // 获得当前日期
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            map.put("reportDate",today);
            // 获得本周一的日期
            String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            // 获取本周最后一天的日期
            String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            // 获得本月第一天的日期
            String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            // 获取本月最后一天的日期
            String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

            //会员数据统计
            //1,新增会员数
            Integer todayNewMember = memberDao.todayNewMember(today);
            map.put("todayNewMember",todayNewMember);
            System.out.println("............");

            //预约到诊数据统计
            //本周预约数
            Map map01=new HashMap();
            map01.put("thisWeekMonday",thisWeekMonday);
            map01.put("thisWeekSunday",thisWeekSunday);
            Integer thisWeekOrderNumber=orderDao.thisWeekOrderNumber(map01);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            System.out.println("..............");
            //本月到诊数
            Map map02=new HashMap();
            map02.put("firstDay4ThisMonth",firstDay4ThisMonth);
            map02.put("lastDay4ThisMonth",lastDay4ThisMonth);
            Integer thisMonthVisitsNumber=orderDao.thisMonthVisitsNumber(map02);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            //热门套餐
            List<Map>hotSetmeal=orderDao.hotSetmeal();
            System.out.println("......");
            map.put("hotSetmeal",hotSetmeal);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
