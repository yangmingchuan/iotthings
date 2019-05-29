package com.ymc.iotthings.dao;

import com.ymc.iotthings.core.universal.Mapper;
import com.ymc.iotthings.model.SysPermissionInit;

import java.util.List;

public interface SysPermissionInitMapper extends Mapper<SysPermissionInit> {

    List<SysPermissionInit> selectAllOrderBySort();

}