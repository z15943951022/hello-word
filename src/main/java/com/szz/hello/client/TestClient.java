package com.szz.hello.client;

import com.szz.hello.common.HistoryStrategy;
import com.szz.hello.common.SwitchController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author szz
 */

@Component
public class TestClient {

    @Autowired
    private HistoryStrategy historyStrategy;

    public void test(){
        historyStrategy.history(TestClient.class);
    }
}
