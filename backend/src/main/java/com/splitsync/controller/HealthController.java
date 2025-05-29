package com.splitsync.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {
                "http://localhost:3000",
                "https://splitsync.vercel.app",
                "https://splitsync-9lwa.vercel.app"
        },
        allowCredentials = "true"
)
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

