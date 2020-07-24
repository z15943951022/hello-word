package com.szz.hello.service.impl;

import com.szz.hello.common.LocalBusGroup;
import com.szz.hello.service.MyService;

/**
 * @author szz
 */
@LocalBusGroup(switchClass = Aservice.class)
public class Bservice implements MyService {

    @Override
    public String getMessage() {
        return "B业务";
    }
}
