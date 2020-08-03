package com.szz.hello.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class HystrixStrategy {

    @Autowired
    private SwitchController switchController;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${switch.threshold:5}")
    private int threshold;

    public void hystrix(Class clazz){
        Long increment = redisTemplate.opsForValue().increment(clazz.getName());
        redisTemplate.expire(clazz.getName(),10, TimeUnit.MINUTES);
        if (increment != null && increment >= threshold){
            switchController.targetSwitch(clazz.getName(),true);
            redisTemplate.delete(clazz.getName());
        }
    }

}
