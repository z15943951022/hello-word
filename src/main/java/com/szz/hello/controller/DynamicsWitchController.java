package com.szz.hello.controller;

import com.szz.hello.common.SwitchController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author szz
 */
@RestController
@RequestMapping("/api/course/switch")
public class DynamicsWitchController {

    @Autowired
    private SwitchController switchController;

    @PostMapping("/remote/all")
    public ResponseEntity switchAllFeign() {
        switchController.switchRemoteALLBean();
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/local/all")
    public ResponseEntity switchAllLocal() {
        switchController.switchLocalALLBean();
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/remote/{label}")
    public ResponseEntity switchAllFeign(@PathVariable String label) {
        switchController.switchRemoteALLBeanByLabel(label);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/local/{label}")
    public ResponseEntity switchAllLocal(@PathVariable String label) {
        switchController.switchLocalALLBeanByLabel(label);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/local/target/{className}/{fieldName}")
    public ResponseEntity targetLocal(@PathVariable String className, @PathVariable String fieldName) {
        switchController.targetSwitch(className,fieldName,true);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/remote/target/{className}/{fieldName}")
    public ResponseEntity targetRemote(@PathVariable String className, @PathVariable String fieldName) {
        switchController.targetSwitch(className,fieldName,false);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/print")
    public ResponseEntity print() {
        return ResponseEntity.ok().body(switchController.printAllStatus());
    }


}
