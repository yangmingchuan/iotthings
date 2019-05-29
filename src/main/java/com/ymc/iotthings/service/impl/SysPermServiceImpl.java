package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.dao.SysPermMapper;
import com.ymc.iotthings.model.SysPerm;
import com.ymc.iotthings.service.SysPermService;
import com.ymc.iotthings.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: SysPermService接口实现类
* @author ymc
* @date 2019/05/29 10:22
*/
@Service
public class SysPermServiceImpl extends AbstractService<SysPerm> implements SysPermService {

    @Resource
    private SysPermMapper sysPermMapper;

}