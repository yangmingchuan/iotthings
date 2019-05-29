package com.ymc.iotthings.dao;

import com.ymc.iotthings.core.universal.Mapper;
import com.ymc.iotthings.model.RolePerm;

import java.util.List;

public interface RolePermMapper extends Mapper<RolePerm> {

    List<String> getPermsByUserId(String userId);

}