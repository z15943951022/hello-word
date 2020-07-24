package com.szz.hello.controller;

import com.szz.hello.common.DynamicSwitch;
import com.szz.hello.service.TestService;
import org.apache.tomcat.util.digester.SetPropertiesRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szz
 */
@RestController
public class TestController extends DynamicSwitch {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok().body(testService.bus());
    }

}
