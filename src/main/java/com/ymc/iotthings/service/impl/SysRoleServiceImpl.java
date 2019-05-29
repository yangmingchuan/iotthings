package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.dao.SysRoleMapper;
import com.ymc.iotthings.model.SysRole;
import com.ymc.iotthings.service.SysRoleService;
import com.ymc.iotthings.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: SysRoleService接口实现类
* @author ymc
* @date 2019/05/29 10:18
*/
@Service
public class SysRoleServiceImpl extends AbstractService<SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

}