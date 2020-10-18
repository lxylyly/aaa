package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Map map=reportService.getMemberReport();
        return new Result(true,"查询会员折线图成功",map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
Map map=reportService.getSetmealReport();
return new Result(true,"查询套餐占比饼图成功",map);
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
       Map map= reportService.getBusinessReportData();
       return new Result(true,"运营数据统计成功",map);
    }

/*
//导出Excel表格
    @RequestMapping("/exportBusinessReport")
    //1,参数requset和response
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //2,获取到模板,使用参数requset,
            String template = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template-bak.xlsx";
            //3,获取到Excel对象,POI
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(template)));
            //4,查询-会员数据统计t_member;预约到诊数据统计t_order;热门套餐 两个表,查出所有数据
            Map result=reportService.getBusinessReportData();
            //取出Map中的值下一步赋值到表中
            String reportDate =(String) result.get("reportDate");
            Integer todayNewMember = (Integer)result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> mapList = (List<Map>)result.get("hotSetmeal");
            //5,将数据赋值到表中
            //分为填充普通类型和list类型(遍历),要精确的填充到每个单元格
            XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);//获取表
            XSSFRow row = sheetAt.getRow(2);//获取行
            row.getCell(5).setCellValue(reportDate);//获取列赋值
            row = sheetAt.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            //row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheetAt.getRow(5);
            //row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            //row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheetAt.getRow(7);
            //row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            //row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheetAt.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            //row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheetAt.getRow(9);
            //row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rownum=12;//将开始行设置成12
            for (Map map : mapList) {
                row = sheetAt.getRow(rownum);
                row.getCell(4).setCellValue((String) map.get("name"));
                row.getCell(5).setCellValue((long)map.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
                row.getCell(7).setCellValue((String)map.get("remark"));//备注
                rownum++;
            }
            //6,导出
            //以流的形式导出
            ServletOutputStream outputStream = response.getOutputStream();
            //设置文件类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置文件头信息,名字,附件导出,文件名
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            xssfWorkbook.write(outputStream);
            //关闭资源
            outputStream.flush();
            outputStream.close();
            xssfWorkbook.close();
            return new Result(true,"导出Excel成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"导出Excel失败");
        }
    }
*/

    @RequestMapping("/exportBusinessReport")
    //1,参数requset和response
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取模板路径,
            String template = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //使用POI创建模板
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(template)));
            //获取值Map
            Map result = reportService.getBusinessReportData();
            //将Map赋值给到模板
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook,result);
            //设置类型,设置头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            //输出
            xssfWorkbook.write(response.getOutputStream());
            //关闭资源
            xssfWorkbook.close();
            return new Result(true,"导出Excel成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,"导出Excel失败");
        }
    }

    @RequestMapping("/exportBusinessReport4PDF")
//参数requset(获取两个文件绝对路径)和response(要将数据写出)
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response){
        try {
//web+service+dao 查询所有的数据 参数parms和套餐详情list
            Map result=reportService.getBusinessReportData();
            //取出所有热门套餐;因为前端要的是多个(List)键值对(通过键值对来获取Map)来展示
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
//找到模板(为了进行编译,先找到两个文件的绝对路径,将jrxml转换成为jasper,idea只能识别jasper)
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "reportbusiness.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") + File.separator + "reportbusiness.jasper.jrxml";
//进行编译
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);
//将数据赋值到模板-使用JavaBean醒来填充
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));
            //设置文件类型
            response.setContentType("application/pdf");
//设置头信息, 文件名称固定name,attachment附件,filename文件名;
            response.setHeader("content-Disposition","attachment;filename=bussiness.pdf");
//使用输出流进行输出
            JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
//关闭资源,没有单独把流创建出来就不用关闭
            return new Result(true,"导出运营数据PDF成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"导出运营数据PDF失败");
        }
    }
}
