package com.ymc.iotthings.controller;

import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.UserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 参考博客：https://juejin.im/post/5ad6b3c3f265da237c696ba0
 * package name: com.vip.things.controller
 * date :2019年3月25日 08:53:34
 * author : ymc
 **/

@RestController
@RequestMapping("userInfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/hello")
    public String hello(){
        return "hello SpringBoot";
    }

    @PostMapping("/selectById")
    public UserInfo selectById(Integer id){
        return userInfoService.selectById(id);
    }

}
