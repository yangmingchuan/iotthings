package com.ymc.iotthings.controller;

import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.ShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Shiro Util 控制器
 *
 * package name: com.ymc.iotthings.controller
 * date :2019/5/29
 * author : ymc
 **/

@RestController
@RequestMapping("/ShiroUtils")
public class ShiroUtilsController {

    @Autowired
    ShiroService shiroService;

    @GetMapping("/noLogin")
    public void noLogin() {
        throw new UnauthenticatedException();
    }

    @GetMapping("/noAuthorize")
    public void noAuthorize() {
        throw new UnauthorizedException();
    }

    @PostMapping("/getNowUser")
    public UserInfo getNowUser() {
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * @Description: 重新加载shiro权限
     * @throws Exception
     */
    @PostMapping("/updatePermission")
    public void updatePermission() throws Exception {
        shiroService.updatePermission();
    }


}
