package com.ymc.iotthings.dao;

import com.ymc.iotthings.core.universal.Mapper;
import com.ymc.iotthings.model.UserRole;

import java.util.List;

public interface UserRoleMapper extends Mapper<UserRole> {

    /**
     * 根据 用户 id 获取 角色
     * @param userId
     * @return
     */
    List<String> getRolesByUserId(String userId);

}