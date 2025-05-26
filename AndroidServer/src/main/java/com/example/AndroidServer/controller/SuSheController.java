package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.SuSheMapper;
import com.example.AndroidServer.mapper.UserMapper;
import com.example.AndroidServer.model.*;
import com.example.AndroidServer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

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
    @PostMapping("/sushe/charge")
    public ResponseEntity<ChargeResponse> handleCharge(@RequestBody ChargeRequest request) {
        try {
            // 1. 查询宿舍信息
            Dormitory dormitory = suSheMapper.findByBuildingAndRoom(request.getBuildingNo(), request.getRoomNo());
            if (dormitory == null) {
                return ResponseEntity.badRequest().body(new ChargeResponse(false, "无效的宿舍信息", 0, null));
            }

            Long dormitoryId = dormitory.getId();

            // 2. 构造充值历史记录
            ChargeHistory history = new ChargeHistory();
            history.setDormitoryId(dormitoryId);
            history.setAmount(request.getAmount());
            history.setName(request.getName());
            history.setDate(new Date(System.currentTimeMillis()));

            // 3. 写入数据库
            suSheMapper.insertInToChargeHistory(history);

            // 4. 更新宿舍余额
            BigDecimal newBalance = dormitory.getBalance().add(BigDecimal.valueOf(request.getAmount()));
            dormitory.setBalance(newBalance);
            suSheMapper.updateBalance(dormitory);

            // 5. 构造返回结果
            ChargeResponse response = new ChargeResponse(true, "充值成功", newBalance.doubleValue(), history);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChargeResponse(false, "服务器内部错误", 0, null));
        }
    }
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
            // 1. 查询宿舍信息，得到 dormitory_id
            Dormitory dormitory = suSheMapper.findByBuildingAndRoom(buildingNo, roomNo);
            if (dormitory == null) {
                return ResponseEntity.notFound().build(); // 宿舍不存在
            }

            Long dormitoryId = dormitory.getId();
            if (dormitoryId == null) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }

            // 2. 查询充电记录
            List<ChargeHistory> histories = suSheMapper.findByDormitoryId(dormitoryId);

            return ResponseEntity.ok(histories);

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
