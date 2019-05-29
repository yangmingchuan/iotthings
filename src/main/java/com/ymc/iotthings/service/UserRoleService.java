package com.ymc.iotthings.service;

import com.ymc.iotthings.core.universal.Service;
import com.ymc.iotthings.model.UserRole;

import java.util.List;

/**
* @Description: UserRoleService接口
* @author ymc
* @date 2019/05/29 10:28
*/
public interface UserRoleService extends Service<UserRole> {

    List<String> getRolesByUserId(String userId);

}
