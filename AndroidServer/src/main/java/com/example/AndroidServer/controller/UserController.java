package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.UserMapper;
import com.example.AndroidServer.model.User;
import com.example.AndroidServer.model.UserRequest;
import com.example.AndroidServer.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;

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
                // 生成 Token
                String token = jwtUtil.generateToken(username);

                // 构建响应数据
                Map<String, Object> response = new HashMap<>();
                response.put("message", "登录成功");
                response.put("username", user.getU_name());
                response.put("token", token);

                logger.info("【成功】找到用户: {}", user.toString());

                return ResponseEntity.ok(response);
            } else {
                logger.warn("【失败】未找到用户，用户名或密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "用户名或密码错误"));
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

            if (userMapper.countByName(username) > 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "用户名已被注册，请换一个用户名"));
            }

            // 插入新用户
            userMapper.insertByRegister(username, password);

            logger.info("【成功】用户注册成功");
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "注册成功"));

        } catch (Exception e) {
            logger.error("【系统异常】注册过程中发生错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "服务器内部错误，请稍后再试"));
        }
    }
}