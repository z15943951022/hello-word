package com.szz.hello.client;

import com.szz.hello.common.Feign;
import com.szz.hello.common.SwitchController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author szz
 */

@Component
public class TestClient {

    @Autowired
    private SwitchController switchController;

    public void test(){
        switchController.targetSwitch(TestClient.class.getName(),true);
    }
}
