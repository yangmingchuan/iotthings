package com.ymc.iotthings.controller;

import com.ymc.iotthings.core.ret.RetResponse;
import com.ymc.iotthings.core.ret.RetResult;
import com.ymc.iotthings.core.utils.UploadActionUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件上传 控制器
 *
 * package name: com.ymc.iotthings.controller
 * date :2019/5/29
 * author : ymc
 **/

@RestController
@RequestMapping("/uploadFile")
public class UploadFileController {

    @PostMapping("/upload")
    public RetResult<List<String>> upload( HttpServletRequest httpServletRequest) throws Exception {
        List<String> list = UploadActionUtil.uploadFile(httpServletRequest);
        return RetResponse.makeOKRsp(list);
    }

}
