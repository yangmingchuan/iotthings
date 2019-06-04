package com.ymc.iotthings.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ymc.iotthings.core.aop.AnnotationLog;
import com.ymc.iotthings.core.configurer.ServiceException;
import com.ymc.iotthings.core.ret.RetResponse;
import com.ymc.iotthings.core.ret.RetResult;
import com.ymc.iotthings.core.utils.ApplicationUtils;
import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: UserInfoController类
* @author ymc
* @date 2019/05/29 10:17
*/

@RestController
@RequestMapping("/userInfo")
@Api(tags = {"用户操作接口"}, description = "RedisController")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "用户名",
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码",
                    dataType = "String", paramType = "query")
    })
    @AnnotationLog(remark = "登录")
    @PostMapping("/login")
    public RetResult<UserInfo> login(@RequestParam String userName, @RequestParam String password) {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(new UsernamePasswordToken(userName, password));
        //从session取出用户信息 currentUser.getSession().getId()
        //UserInfo user = (UserInfo) currentUser.getPrincipal();
        //登录  获取session currentUser.getSession().getId()
        try {
            currentUser.login(new UsernamePasswordToken(userName, password));
        }catch (IncorrectCredentialsException i){
            throw new ServiceException("密码输入错误");
        }
        //从session取出用户信息
        UserInfo user = (UserInfo) currentUser.getPrincipal();
        return RetResponse.makeOKRsp(user);
    }

    @ApiOperation(value = "查询用户", notes = "分页查询用户所有")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码",
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示条数",
                    dataType = "Integer", paramType = "query")
    })
    @AnnotationLog(remark = "查询用户findAll")
    @PostMapping("/selectAll")
    public RetResult<PageInfo<UserInfo>> selectAll(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<UserInfo> userInfoList = userInfoService.selectAll();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoList);
        return RetResponse.makeOKRsp(pageInfo);
    }

    @PostMapping("/insert")
    public RetResult<Integer> insert(UserInfo userInfo) throws Exception{
        userInfo.setId(ApplicationUtils.getUUID());
        Integer state = userInfoService.insert(userInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = userInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(UserInfo userInfo) throws Exception {
         Integer state = userInfoService.update(userInfo);
         return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<UserInfo> selectById(@RequestParam String id) throws Exception {
        UserInfo userInfo = userInfoService.selectById(id);
        return RetResponse.makeOKRsp(userInfo);
    }

     /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<UserInfo>>
     */
     @AnnotationLog(remark = "查询所有用户list")
     @PostMapping("/list")
     public RetResult<PageInfo<UserInfo>> list(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size) throws Exception {
         PageHelper.startPage(page, size);
         List<UserInfo> list = userInfoService.selectAll();
         PageInfo<UserInfo> pageInfo = new PageInfo<>(list);
         return RetResponse.makeOKRsp(pageInfo);
     }
}