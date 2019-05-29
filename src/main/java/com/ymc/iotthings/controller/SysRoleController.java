package com.ymc.iotthings.controller;

import com.ymc.iotthings.core.ret.RetResult;
import com.ymc.iotthings.core.ret.RetResponse;
import com.ymc.iotthings.core.utils.ApplicationUtils;
import com.ymc.iotthings.model.SysRole;
import com.ymc.iotthings.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: SysRoleController类
* @author ymc
* @date 2019/05/29 10:18
*/
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(SysRole sysRole) throws Exception{
        sysRole.setId(ApplicationUtils.getUUID());
        Integer state = sysRoleService.insert(sysRole);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = sysRoleService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(SysRole sysRole) throws Exception {
         Integer state = sysRoleService.update(sysRole);
         return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<SysRole> selectById(@RequestParam String id) throws Exception {
        SysRole sysRole = sysRoleService.selectById(id);
        return RetResponse.makeOKRsp(sysRole);
    }

     /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<SysRole>>
     */
     @PostMapping("/list")
     public RetResult<PageInfo<SysRole>> list(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size) throws Exception {
         PageHelper.startPage(page, size);
         List<SysRole> list = sysRoleService.selectAll();
         PageInfo<SysRole> pageInfo = new PageInfo<SysRole>(list);
         return RetResponse.makeOKRsp(pageInfo);
     }
}