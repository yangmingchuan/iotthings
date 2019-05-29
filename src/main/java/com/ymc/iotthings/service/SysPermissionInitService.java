package com.ymc.iotthings.service;

import com.ymc.iotthings.core.universal.Service;
import com.ymc.iotthings.model.SysPermissionInit;

import java.util.List;

/**
* @Description: SysPermissionInitService接口
* @author ymc
* @date 2019/05/29 14:54
*/
public interface SysPermissionInitService extends Service<SysPermissionInit> {

    List<SysPermissionInit> selectAllOrderBySort();

}
