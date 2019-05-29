package com.ymc.iotthings.controller;

import com.ymc.iotthings.core.ret.RetResponse;
import com.ymc.iotthings.core.ret.RetResult;
import com.ymc.iotthings.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * redis 控制器
 *
 * package name: com.ymc.iotthings.controller
 * date :2019/5/28
 * author : ymc
 **/

@RestController
@RequestMapping("/redis")
@Api(tags = {"Redis操作接口"}, description = "RedisController")
public class RedisController {

    @Resource
    private RedisService redisService;

    /**
     * 存入
     *
     * @param key key
     * @param name val
     * @return return RetResponse.makeOKRsp(name);
     */
    @ApiOperation(value = "存入redis val", notes = "存入redis key 及 val")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "redis key",
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "redis val",
                    dataType = "String", paramType = "query")
    })
    @PostMapping("/setRedis")
    public RetResult<String> setRedis(@RequestParam String key ,@RequestParam String name) {
        redisService.set(key,name);
        return RetResponse.makeOKRsp(name);
    }

    /**
     * 取出
     * @param key key
     * @return RetResult<String>
     */
    @ApiOperation(value = "查询Redis val", notes = "根据key 查询Redis val")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "Redis key", required = true,
                    dataType = "String", paramType = "query")
    })
    @PostMapping("/getRedis")
    public RetResult<String> getRedis(@RequestParam String key) {
        String name = redisService.get(key);
        if(StringUtils.isEmpty(name)){
            return RetResponse.makeErrRsp("redis key 不存在对应 value");
        }
        return RetResponse.makeOKRsp(name);
    }


}
