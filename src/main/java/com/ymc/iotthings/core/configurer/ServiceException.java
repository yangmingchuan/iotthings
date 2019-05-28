package com.ymc.iotthings.core.configurer;

import java.io.Serializable;

/**
 * 统一异常处理类
 *
 * package name: com.ymc.iotthings.core.configurer
 * date :2019/5/27
 * author : ymc
 **/

public class ServiceException  extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1213855733833039552L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

