package com.szz.hello.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szz
 */
@RestController
public class HelloWord {

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok().body("hello word");
    }
}
