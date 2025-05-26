package com.example.AndroidServer.controller;

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
}
