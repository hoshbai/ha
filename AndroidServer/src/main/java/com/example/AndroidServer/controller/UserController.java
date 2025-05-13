package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.AdminMapper;
import com.example.AndroidServer.mapper.UserMapper;
import com.example.AndroidServer.model.Admin;
import com.example.AndroidServer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user")
    public ResponseEntity<User> getAdmin(@RequestParam String name) {
        User user = userMapper.selectByName(name);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}