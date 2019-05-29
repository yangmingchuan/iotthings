package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.core.universal.AbstractService;
import com.ymc.iotthings.dao.SysPermissionInitMapper;
import com.ymc.iotthings.model.SysPermissionInit;
import com.ymc.iotthings.service.SysPermissionInitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: SysPermissionInitService接口实现类
* @author ymc
* @date 2019/05/29 14:54
*/
@Service
public class SysPermissionInitServiceImpl extends AbstractService<SysPermissionInit> implements SysPermissionInitService {

    @Resource
    private SysPermissionInitMapper sysPermissionInitMapper;

    @Override
    public List<SysPermissionInit> selectAllOrderBySort() {
        return sysPermissionInitMapper.selectAllOrderBySort();
    }
}