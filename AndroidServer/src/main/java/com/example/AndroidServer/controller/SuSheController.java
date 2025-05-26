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
    private SuSheMapper suSheMapper;
    // 模拟 dormitoryInfo 数据
    @GetMapping("/sushe/dormitoryInfo")
    public ResponseEntity<Dormitory> getDormitoryInfo(@RequestParam String username) {
        System.out.println("收到请求: username=" + username);
        Dormitory dormitory = new Dormitory();
        dormitory.setId(1L);
        dormitory.setBuildingNo("8");
//        dormitory.setRoomNo("210");
        dormitory.setBalance(new BigDecimal("123.45"));

        return ResponseEntity.ok(dormitory);
    }

    // 模拟 getAllDormitories 数据
    @GetMapping("/sushe/getAllDormitories")
    public ResponseEntity<List<Dormitory>> getAllDormitories() {
        List<Dormitory> dormitoryList = new ArrayList<>();

        Dormitory d1 = new Dormitory();
        d1.setId(1L);
        d1.setBuildingNo("8");
        d1.setRoomNo("210");
        d1.setBalance(new BigDecimal("123.45"));
        dormitoryList.add(d1);

        Dormitory d2 = new Dormitory();
        d2.setId(2L);
        d2.setBuildingNo("8");
        d2.setRoomNo("211");
        d2.setBalance(new BigDecimal("89.00"));
        dormitoryList.add(d2);

        Dormitory d3 = new Dormitory();
        d3.setId(3L);
        d3.setBuildingNo("9");
        d3.setRoomNo("305");
        d3.setBalance(new BigDecimal("76.50"));
        dormitoryList.add(d3);

        Dormitory d4 = new Dormitory();
        d4.setId(4L);
        d4.setBuildingNo("9");
        d4.setRoomNo("306");
        d4.setBalance(new BigDecimal("45.00"));
        dormitoryList.add(d4);

        return ResponseEntity.ok(dormitoryList);
    }

    @GetMapping("/sushe/loadChargeHistory")
    public ResponseEntity<List<ChargeHistory>> loadChargeHistory(@RequestParam("buildingNo") String buildingNo, @RequestParam("roomNo") String roomNo) {

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

        // 模拟数据开始
        Dormitory dormitory = new Dormitory();
        dormitory.setId(1L);
        dormitory.setBuildingNo(buildingNo); // 使用传入的楼号
        dormitory.setRoomNo(roomNo);         // 使用传入的房间号
        dormitory.setBalance(new BigDecimal("123.45")); // 固定余额

        // 可以加个判断，只允许特定宿舍号测试
        if ("8".equals(buildingNo) && "210".equals(roomNo)) {
            return ResponseEntity.ok(dormitory);
        } else {
            return ResponseEntity.notFound().build();
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
