package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.AdminMapper;
import com.example.AndroidServer.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @GetMapping("/admin")
    public ResponseEntity<Admin> getAdmin(@RequestParam String name) {
        Admin admin = adminMapper.selectByName(name);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}