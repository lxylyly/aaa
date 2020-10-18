package com.itheima.demo;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class Demo {
    @Test
    public void createExcel() {
        try {
            //创建工作薄Excel文件
            XSSFWorkbook sheets = new XSSFWorkbook();
            //创建表格指定名字
            XSSFSheet sheet = sheets.createSheet("华山派");
            //根据表格创建行,使用索引
            XSSFRow row = sheet.createRow(0);
            //根据行创建列,并添加数据
            row.createCell(0).setCellValue("编号");
            row.createCell(1).setCellValue("姓名");
            row.createCell(2).setCellValue("年龄");

            XSSFRow row1 = sheet.createRow(1);
            row.createCell(0).setCellValue("1");
            row.createCell(1).setCellValue("宁旺财");
            row.createCell(2).setCellValue("44");

            //通过输出流将workbooku对象下载到磁盘(指定磁盘位置)
            FileOutputStream fileOutputStream=new FileOutputStream("C:\\Javalll");
            //刷新流,关闭流,关闭工作簿文件
            fileOutputStream.flush();
            fileOutputStream.close();
            sheets.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
