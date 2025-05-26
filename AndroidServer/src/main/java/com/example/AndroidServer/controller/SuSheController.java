package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.SuSheMapper;
import com.example.AndroidServer.mapper.UserMapper;
import com.example.AndroidServer.model.ChargeHistory;
import com.example.AndroidServer.model.Dormitory;
import com.example.AndroidServer.model.User;
import com.example.AndroidServer.model.UserRequest;
import com.example.AndroidServer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: w2424
 * Date: 2025-05-26
 * Time: 13:19
 * Description:
 */

@RestController
@RequestMapping("/api")
public class SuSheController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SuSheMapper suSheMapper;
    // 模拟 dormitoryInfo 数据
    @GetMapping("/sushe/dormitoryInfo")
    public ResponseEntity<Dormitory> getDormitoryInfo(@RequestParam String username) {
        try {
            // 1. 查询用户是否存在
            User user = userMapper.selectByNameOnly(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 用户不存在
            }

            Integer susheId = user.getSusheId();

            // 2. 判断是否已绑定宿舍
            if (susheId == null || susheId == 0) {
                // 用户未绑定宿舍，返回空 Dormitory 对象
                return ResponseEntity.ok(new Dormitory());
            }

            // 3. 查询宿舍详细信息
            Dormitory dormitory = suSheMapper.findById(susheId);
            if (dormitory == null) {
                // 数据异常：用户绑定了宿舍，但宿舍表中查不到
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.ok(dormitory);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 模拟 getAllDormitories 数据
    @GetMapping("/sushe/getAllDormitories")
    public ResponseEntity<List<Dormitory>> getAllDormitories() {
        try {
            List<Dormitory> dormitoryList = suSheMapper.findAll();
            return ResponseEntity.ok(dormitoryList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sushe/loadChargeHistory")
    public ResponseEntity<List<ChargeHistory>> loadChargeHistory(
            @RequestParam("buildingNo") String buildingNo,
            @RequestParam("roomNo") String roomNo) {

        try {
            // 打印调试信息
            System.out.println("收到请求: 楼号=" + buildingNo + ", 房间号=" + roomNo);

            // TODO: 这里替换为从数据库查询真实数据
            List<ChargeHistory> mockData = new ArrayList<>();
            mockData.add(new ChargeHistory(new Date(2025 - 1900, 5 - 1, 25), 20.0));
            mockData.add(new ChargeHistory(new Date(2025 - 1900, 5 - 1, 26), 10.0));

            return ResponseEntity.ok(mockData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/sushe/getBalance")
    public ResponseEntity<Dormitory> loadCurrentBalance(
            @RequestParam("buildingNo") String buildingNo,
            @RequestParam("roomNo") String roomNo) {

        try {
            // 1. 查询数据库中的宿舍信息
            Dormitory dormitory = suSheMapper.findByBuildingAndRoom(buildingNo, roomNo);

            if (dormitory != null) {
                // 存在 → 返回宿舍信息
                return ResponseEntity.ok(dormitory);
            } else {
                // 不存在 → 返回 404
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/sushe/updateDormitory")
    public ResponseEntity<Boolean> updateDormitory(@RequestBody Dormitory dormitory) {
        try {
            String buildingNo = dormitory.getBuildingNo();
            String roomNo = dormitory.getRoomNo();
            String username = dormitory.getUsername();

            int rowsAffected = suSheMapper.updateDormitoryByBuildingAndRoom(buildingNo, roomNo, username);

            if (rowsAffected > 0) {
                return ResponseEntity.ok(true); // 成功
            } else {
                return ResponseEntity.badRequest().body(false); // 无数据更新（可能用户名或宿舍不存在）
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
