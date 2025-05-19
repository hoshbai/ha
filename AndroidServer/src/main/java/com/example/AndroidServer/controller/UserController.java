package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.UserMapper;
import com.example.AndroidServer.model.User;
import com.example.AndroidServer.model.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest userRequest) {
        try {
            // 打印请求数据用于调试
            logger.info("【调试】收到登录请求");
            logger.info("【调试】用户名: {}", userRequest.getUsername());
            logger.info("【调试】密码: {}", userRequest.getPassword());

            String username = userRequest.getUsername();
            String password = userRequest.getPassword();

            // 查询数据库
            User user = userMapper.selectByName(username, password);

            if (user != null) {
                logger.info("【成功】找到用户: {}", user.toString());
                return ResponseEntity.ok(user);
            } else {
                logger.warn("【失败】未找到用户，用户名或密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("用户名或密码错误");
            }

        } catch (Exception e) {
            logger.error("【系统异常】登录过程中发生错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器内部错误，请稍后再试");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        try {
            logger.info("【调试】收到注册请求");
            logger.info("【调试】用户名: {}", userRequest.getUsername());
            logger.info("【调试】密码: {}", userRequest.getPassword());

            String username = userRequest.getUsername();
            String password = userRequest.getPassword();

            // 检查用户名是否已存在
            User existingUser = userMapper.selectByName(username, password);
            if (existingUser != null) {
                logger.warn("【失败】用户名已存在");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("用户名已存在");
            }

            // 插入新用户
            userMapper.insertByRegister(username, password);

            logger.info("【成功】用户注册成功");
            return ResponseEntity.status(HttpStatus.CREATED).body("注册成功");

        } catch (Exception e) {
            logger.error("【系统异常】注册过程中发生错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器内部错误，请稍后再试");
        }
    }
}