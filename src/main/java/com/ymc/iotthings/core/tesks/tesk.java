package com.ymc.iotthings.core.tesks;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务
 *
 * @Scheduled为设置定时任务周期的注解
 *  第一种就是fixedRate，他表示以一种固定频率去执行，单位为毫秒
 *  第二种为cron，他可以表达某种特定频率，例如每天晚上三点执行
 *
 * package name: com.ymc.iotthings.core.tesks
 * date :2019/6/3
 * author : ymc
 **/

@Component
/**
 * 开启定时任务的注解
 */
@EnableScheduling
public class tesk {

//    @Scheduled(fixedRate = 5000)
    public void job1(){
        System.out.println("定时任务1" + new Date());
    }

    // 每隔 五分钟执行一次
    @Scheduled(cron = "0 0/5 * * * ?")
    public void job2(){
        System.out.println("定时任务2" + new Date());
    }

}
