package com.xzyun.excelread.service;

import com.xzyun.excelread.service.AppDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PlanGetAppServiceImpl {
    //查询表格中所有的数据

    // AppDO为对应的POJO类，如Student类有name,age属性，每个属性有get和set方法
    public static List<AppDO> getAllByExcel(String file) {
        Workbook wb = null;
        Sheet sheet;
        Row row = null;
        List<AppDO> list = null;
        wb = readExcel(file);
        if (wb != null) {
            //用来存放表中数据
            list = new ArrayList<>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
//            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i < rowNum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    // 读取表格中数据，第一列
                    Cell cellAppName = row.getCell(0);
                    Row row1 = cellAppName.getRow();
                    // 机场飞行航线数据
                    /*String regNumber = row1.getCell(0).getStringCellValue();
                    String shortAircraft = row1.getCell(1).getStringCellValue();
                    String aircraft = row1.getCell(2).getStringCellValue();
                    String F = row1.getCell(3).getStringCellValue();
                    String C = row1.getCell(4).getStringCellValue();
                    String Y = row1.getCell(5).getStringCellValue();
                    String W = row1.getCell(6).getStringCellValue();
                    String sellCode = row1.getCell(7).getStringCellValue();
                    String airline = row1.getCell(8).getStringCellValue();
                    String status = row1.getCell(9).getStringCellValue();
                    String type = row1.getCell(10).getStringCellValue();*/
                    //机场经纬度信息
                    /*if(StringUtils.isEmpty(row1.getCell(1))||StringUtils.isEmpty(row1.getCell(16))||StringUtils.isEmpty(row1.getCell(17))){
                        continue;
                    }
                    String airCode = row1.getCell(1).getStringCellValue();
                    String lon = row1.getCell(16).getStringCellValue();
                    String lat = row1.getCell(17).getStringCellValue();*/

                    //机场经纬度信息
                    if(StringUtils.isEmpty(row1.getCell(1))||StringUtils.isEmpty(row1.getCell(6))||StringUtils.isEmpty(row1.getCell(7))){
                        continue;
                    }
                    String airCode = row1.getCell(0).getStringCellValue();
                    String lon = Dms2D(row1.getCell(5).getStringCellValue());
                    lon = lon.length()>lon.indexOf(".")+6?lon.substring(0,lon.indexOf(".")+6):lon;
                    String lat = Dms2D(row1.getCell(6).getStringCellValue());
                    lat = lat.length()>lat.indexOf(".")+6?lat.substring(0,lat.indexOf(".")+6):lat;
                    System.out.println("update enum_airport_code set l_a_t='"+lat+"', l_o_n='"+lon+"' where airport_name_4code='"+airCode+"' and l_a_t is null and l_o_n is null;");
                    AppDO appDO = new AppDO();
                    list.add(appDO);
                } else {
                    continue;
                }
            }
        }
        return list;
    }



    //读取excel
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.indexOf('.'));
        InputStream is = null;
        try {
            is = new ClassPathResource(filePath).getInputStream();
//            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 经纬度转换 ，度分秒转度
     * @param jwd
     * @author LiuJie
     * @return
     */
    public static String Dms2D(String jwd){
        if(!StringUtils.isEmpty(jwd)){//如果不为空并且存在度单位
            String sec = jwd.substring(jwd.length() - 4);
            sec = sec.substring(0,2)+"."+sec.substring(2,4);
            String min = jwd.substring(jwd.length() - 6, jwd.length() - 4);
                    String temp = jwd.substring(1,jwd.length()-6);
            double d = Double.valueOf(temp);
            double m = Double.valueOf(min);
            double s = Double.valueOf(sec);
            if(jwd.startsWith("S")||jwd.startsWith("W")){
                jwd = "-"+String.valueOf(d+m/60+s/60/60);
            }else{
                jwd = String.valueOf(d+m/60+s/60/60);
            }
            //使用BigDecimal进行加减乘除
	/*BigDecimal bd = new BigDecimal("60");
	BigDecimal d = new BigDecimal(jwd.contains("°")?jwd.split("°")[0]:"0");
	BigDecimal m = new BigDecimal(jwd.contains("′")?jwd.split("°")[1].split("′")[0]:"0");
	BigDecimal s = new BigDecimal(jwd.contains("″")?jwd.split("′")[1].split("″")[0]:"0");
	//divide相除可能会报错（无限循环小数），要设置保留小数点
	jwd = String.valueOf(d.add(m.divide(bd,6,BigDecimal.ROUND_HALF_UP)
            .add(s.divide(bd.multiply(bd),6,BigDecimal.ROUND_HALF_UP))));*/
        }
        return jwd;
    }
}


