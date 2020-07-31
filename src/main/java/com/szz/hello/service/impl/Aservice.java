package com.szz.hello.service.impl;

import com.szz.hello.client.TestClient;
import com.szz.hello.common.RemoteBusGroup;
import com.szz.hello.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author szz
 */
@RemoteBusGroup(switchClass = Bservice.class)
public class Aservice implements MyService {

    @Autowired
    private TestClient testClient;

    @Override
    public String getMessage() {
        return "A业务";
    }
}
