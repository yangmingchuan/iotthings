package com.ymc.iotthings.service.impl;

import com.ymc.iotthings.core.universal.AbstractService;
import com.ymc.iotthings.dao.SystemLogMapper;
import com.ymc.iotthings.model.SystemLog;
import com.ymc.iotthings.service.SystemLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @Description: SystemLogService接口实现类
* @author ymc
* @date 2019/05/28 16:27
*/
@Service
public class SystemLogServiceImpl extends AbstractService<SystemLog> implements SystemLogService {

    @Resource
    private SystemLogMapper systemLogMapper;

}