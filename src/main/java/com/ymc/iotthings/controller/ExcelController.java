package com.ymc.iotthings.controller;

import com.ymc.iotthings.core.constant.ExcelConstant;
import com.ymc.iotthings.core.ret.RetResponse;
import com.ymc.iotthings.core.ret.RetResult;
import com.ymc.iotthings.core.utils.ExcelUtils;
import com.ymc.iotthings.model.ExcelData;
import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 导出 excel 控制器
 *
 * package name: com.ymc.iotthings.controller
 * date :2019/5/31
 * author : ymc
 **/

@RestController
@RequestMapping("excel")
public class ExcelController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/test")
    public RetResult<Integer> test(){
        int rowIndex = 0;
        List<UserInfo> list = userInfoService.selectAll();
        ExcelData data = new ExcelData();
        data.setName("hello");
        List<String> titles = new ArrayList();
        titles.add("ID");
        titles.add("userName");
        titles.add("password");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        for(int i = 0, length = list.size();i<length;i++){
            UserInfo userInfo = list.get(i);
            List<Object> row = new ArrayList();
            row.add(userInfo.getId());
            row.add(userInfo.getUserName());
            row.add(userInfo.getPassword());
            rows.add(row);
        }
        data.setRows(rows);
        try{
            rowIndex = ExcelUtils.generateExcel(data, ExcelConstant.FILE_PATH + ExcelConstant.FILE_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RetResponse.makeOKRsp(Integer.valueOf(rowIndex));
    }


}
