package com.splitsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headHealth() {
        return ResponseEntity.ok().build();
    }
}

