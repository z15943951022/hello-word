package com.szz.hello.client;

import com.szz.hello.common.HystrixStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author szz
 */

@Component
public class TestClient {

    @Autowired
    private HystrixStrategy hystrixStrategy;

    public void test(){
        hystrixStrategy.hystrix(TestClient.class);
    }
}
