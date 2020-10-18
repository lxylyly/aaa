package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    //注入这个类,就可以直接使用这个类
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String outputpath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add_setmeal(setmeal);
        add_setmeal_checkgroup(setmeal.getId(), checkgroupIds);

        //一个方法来调用套餐列表,套餐详情
        generateMobileStaticHtml();
    }

    //两个方法分开写,删除只需要套餐列表
    private void generateMobileStaticHtml() {
        //查询到所有的Setmeal(套餐)对象信息放在集合中,再页面中展示
        List<Setmeal> setmealList = setmealDao.findAll();
        System.out.println(setmealList);
        //套餐列表
        generateMobileSetmealListHtml(setmealList);
        //套餐详情
        generateMobileSetmealDetailHtml(setmealList);
    }

    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        //创建一个Map,将查到的套餐对象的集合放进去,
        // 通过freemarker生成填写到模板,然后生成一个静态页面
        Map<String, Object> map = new HashMap<>();
        map.put("setmealList", setmealList);//注意这里的key必须和模板文件中的名字一样
        String templateName = "mobile_setmeal.ftl";
        generateHtml(templateName, "m_setmeal.html", map);
    }

    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
            String templateName="mobile_setmeal_detail.ftl";
        //遍历这个套餐对象集合,得到每一个对象,
        // 再根据对象去查到每一个查询组,
        // 再去查到每一个查询项
        //再将返回的值存到map中(展示的时候是以一个套餐作为一个单位,后面跟着套餐详情)
        for (Setmeal setmeal : setmealList) {
            //创建一个Map,将查到的套餐对象套餐详情放进去放进去,
            Map map=new HashMap<>();
            Integer setmeal_id = setmeal.getId();
            Setmeal SM=setmealDao.findByIdS(setmeal_id);
            map.put("setmeal",SM);//注意:这里的key必须和模板文件中的名字一样
            //第三个参数的意思就是把数据库查到的数据返回给到freemarker,
            // 然后填充到模板,再根据地址生成页面
            generateHtml(templateName,"setmeal_detail_"+setmeal_id+".html",map);
        }
    }

    private void generateHtml(String templateName, String s, Map map) {
        BufferedWriter bufferedWriter = null;
        //模板配置
        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //获取模板,传入模板地址
            Template template = configuration.getTemplate(templateName);
            //创建一个新的文件地址就是freemarker.properties中的地址,还要加上模板名称,
            //(因为要使用这个文件向模板中写入数据)
            String newPathFileName = outputpath + "/" + s;
            //用写入流,将数据库查到的数据写到文件中   从缓冲区写到文件
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newPathFileName)));
            //再使用模板方法将数据填充到模板中,然后再生成页面
            template.process(map,bufferedWriter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void add_setmeal_checkgroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", id);
            map.put("checkgroup_id", checkgroupId);
            setmealDao.add_s_c(map);
        }
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> setmealPage = setmealDao.findPage(queryString);
        return new PageResult(setmealPage.getTotal(), setmealPage.getResult());
    }


    @Override
    public Setmeal findById(Integer setmeal_id) {
        return setmealDao.findById(setmeal_id);
    }

    @Override
    public List<Integer> findCheckgroupId(Integer setmeal_id) {
        return setmealDao.findCheckgroupId(setmeal_id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.edit_update(setmeal);
        setmealDao.edit_delete(setmeal.getId());
        add_setmeal_checkgroup(setmeal.getId(), checkgroupIds);
    }

    @Override
    public List<Setmeal> getSetmeal() {
       return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findByIdS(Integer setmeal_id) {
       return setmealDao.findByIdS(setmeal_id);
    }

}
