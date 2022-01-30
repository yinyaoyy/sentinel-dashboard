package com.alibaba.csp.sentinel.dashboard.util;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;
import java.util.concurrent.Executor;

public class Test {
    public static void main(String[] args) throws NacosException {
        String serverAddr="localhost:8848";
        String dataId="nacos-test-dev.properties";
        String groupId="DEFAULT_GROUP";
        Properties properties=new Properties();
        properties.put("serverAddr",serverAddr);
        ConfigService configService = null;
        try {
            //通过nacosfactory创建一个配置中心的服务
            configService= NacosFactory.createConfigService(properties);
            // 5000表示读取配置的超时时间,如果超时或者出现网络故障，会抛出NacosException的异常
//            String content=configService.getConfig(dataId,groupId,3000);
            boolean b = configService.removeConfig(dataId, groupId);
            System.out.println(b);
//            System.out.println(content);
        } catch (NacosException e) {
            e.printStackTrace();
        }

        //添加监听
        configService.addListener(dataId, groupId, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String s) {
                System.out.println("receive config:"+s);
            }
        });

        // 测试让主线程不退出，因为订阅配置是守护线程，主线程退出守护线程就会退出。 正式代码中无需下面代码
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

