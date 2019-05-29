package com.ymc.iotthings.service;

import java.util.Map;

/**
 * package name: com.ymc.iotthings.service
 * date :2019/5/29
 * author : ymc
 **/

public interface ShiroService {

    Map<String, String> loadFilterChainDefinitions();

    /**
     * 动态修改权限
     */
    void updatePermission();

}
