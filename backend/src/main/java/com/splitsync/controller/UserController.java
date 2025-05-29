package com.splitsync.controller;

import com.splitsync.model.User;
import com.splitsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(
        origins = {
                "http://localhost:3000",
                "https://splitsync.vercel.app",
                "https://splitsync-9lwa.vercel.app"
        },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public User joinGroup(@RequestBody Map<String, Object> body) {
        return userService.joinGroup(
                Long.valueOf(body.get("groupId").toString()),
                (String) body.get("name"),
                (String) body.get("email"),
                (String) body.get("upi")
        );
    }
}