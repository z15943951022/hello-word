package com.szz.hello.common;

import com.szz.hello.client.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class HistoryStrategy {

    @Autowired
    private SwitchController switchController;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${aa:1}")
    private int threshold;


    public void history(Class clazz){
        Long increment = redisTemplate.opsForValue().increment(clazz.getName());
        redisTemplate.expire(clazz.getName(),10, TimeUnit.MINUTES);
        if (increment != null && increment >= threshold){
            switchController.targetSwitch(clazz.getName(),true);
            redisTemplate.delete(clazz.getName());
        }
    }

}
