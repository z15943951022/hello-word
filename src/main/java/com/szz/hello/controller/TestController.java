package com.szz.hello.controller;

import com.szz.hello.client.TestClient;
import com.szz.hello.common.DynamicSwitchable;
import com.szz.hello.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szz
 */
@RestController
public class TestController extends DynamicSwitchable {

    @Autowired
    private TestService testService;


    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok().body(testService.bus());
    }

}
