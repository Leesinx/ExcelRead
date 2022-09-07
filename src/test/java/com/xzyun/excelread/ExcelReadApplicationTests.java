package com.xzyun.excelread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xzyun.excelread.service.AppDO;
import com.xzyun.excelread.service.PlanGetAppServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
class ExcelReadApplicationTests {

    @Test
    void contextLoads() {
        // 得到APP表格中所有的数据
//        String PATH  = "国内航司飞机数据.xlsx";
//        String PATH  = "吉祥机场.xlsx";
        String PATH  = "机场信息补充2 时分秒1.xlsx";
        List<AppDO> listExcel = PlanGetAppServiceImpl.getAllByExcel(PATH);
        System.out.println(listExcel.size());
        int sum = 0;
        for (AppDO ifrPlanAppDO : listExcel) {
            // 插入数据库逻辑 AppDAO实现了具体的插入功能
            // AppDAO.insert(ifrPlanAppDO);
            sum++;
        }

    }

    @Test
    void jsonLoads() throws IOException {
        // 得到APP表格中所有的数据
//        String PATH  = "国内航司飞机数据.xlsx";
//        String PATH  = "吉祥机场.xlsx";
        String PATH  = "user.json";
        InputStream inputStream = new ClassPathResource(PATH).getInputStream();
        int line = 0;
        byte [] b = new byte[1024];
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = inputStream.read(b)) != -1){
            stringBuilder.append(new String(b));
        }
        String substring = stringBuilder.substring(0, stringBuilder.indexOf("]") + 1);
//        System.out.println(substring);
        final JSONArray parse = JSONArray.parseArray(String.valueOf(substring));
        for (Object o : parse) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(o));
            String id = (String)jsonObject.getOrDefault("id", "");
            String name = (String)jsonObject.getOrDefault("name","");
            System.out.println("update blade_user set mcu_user_id='"+id+"' where name='"+name+"';");
        }

    }

}
