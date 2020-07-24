package com.szz.hello.service.impl;

import com.szz.hello.common.RemoteBusGroup;
import com.szz.hello.service.MyService;

/**
 * @author szz
 */
@RemoteBusGroup(switchClass = Bservice.class)
public class Aservice implements MyService {

    @Override
    public String getMessage() {
        return "A业务";
    }
}
