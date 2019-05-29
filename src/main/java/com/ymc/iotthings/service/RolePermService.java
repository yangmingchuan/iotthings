package com.ymc.iotthings.service;

import com.ymc.iotthings.core.universal.Service;
import com.ymc.iotthings.model.RolePerm;

import java.util.List;

/**
* @Description: RolePermService接口
* @author ymc
* @date 2019/05/29 10:32
*/
public interface RolePermService extends Service<RolePerm> {

    List<String> getPermsByUserId(String userId);

}
