package com.example.AndroidServer.controller;

import com.example.AndroidServer.model.CampusCard;
import com.example.AndroidServer.model.CardTransaction;
import com.example.AndroidServer.model.InternetFee;
import com.example.AndroidServer.service.CampusCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/campuscard") // 设置基础请求路径
public class CampusCardController {

    private final CampusCardService campusCardService;

    @Autowired
    public CampusCardController(CampusCardService campusCardService) {
        this.campusCardService = campusCardService;
    }

    // 根据用户ID获取校园卡信息
    @GetMapping("/user/{userId}")
    public ResponseEntity<CampusCard> getCampusCardByUserId(@PathVariable("userId") String userId) {
        CampusCard campusCard = campusCardService.getCampusCardByUserId(userId);
        if (campusCard != null) {
            System.out.println("Debug: CampusCard fetched for user " + userId + ", cardId: " + campusCard.getCardId());
            return ResponseEntity.ok(campusCard);
        } else {
            System.out.println("Debug: No CampusCard found for user " + userId);
            return ResponseEntity.notFound().build();
        }
    }

    // 根据卡ID获取校园卡信息
    @GetMapping("/{cardId}")
    public ResponseEntity<CampusCard> getCampusCardByCardId(@PathVariable("cardId") String cardId) {
        CampusCard campusCard = campusCardService.getCampusCardByCardId(cardId);
        if (campusCard != null) {
             System.out.println("Debug: CampusCard fetched for cardId " + cardId + ", userId: " + campusCard.getUserId());
            return ResponseEntity.ok(campusCard);
        } else {
             System.out.println("Debug: No CampusCard found for cardId " + cardId);
            return ResponseEntity.notFound().build();
        }
    }

    // 创建新的校园卡
    @PostMapping
    public ResponseEntity<Void> createCampusCard(@RequestBody CampusCard campusCard) {
        campusCardService.createCampusCard(campusCard);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 校园卡充值
    @PostMapping("/recharge")
    public ResponseEntity<Void> recharge(@RequestParam("cardId") String cardId, @RequestParam("amount") BigDecimal amount) {
        boolean success = campusCardService.recharge(cardId, amount);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build(); // 可以返回更具体的错误信息
    }

    // 校园卡消费
    @PostMapping("/consume")
    public ResponseEntity<Void> consume(@RequestParam("cardId") String cardId, @RequestParam("amount") BigDecimal amount, @RequestParam("description") String description) {
        boolean success = campusCardService.consume(cardId, amount, description);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build(); // 可以返回更具体的错误信息，如余额不足或卡片已挂失
    }

    // 支付网费
    @PostMapping("/payInternetFee")
    public ResponseEntity<Void> payInternetFee(@RequestParam("cardId") String cardId, @RequestParam("amount") BigDecimal amount) {
        boolean success = campusCardService.payInternetFee(cardId, amount);
        if (success) {
            return ResponseEntity.ok().build();
        }
        // 根据实际情况返回不同的错误码，例如余额不足、卡片已挂失等
        return ResponseEntity.badRequest().build();
    }

    // 获取交易记录
    @GetMapping("/{cardId}/transactions")
    public ResponseEntity<List<CardTransaction>> getTransactionsByCardId(@PathVariable("cardId") String cardId) {
        try {
            System.out.println("Debug: Received request to get transactions for cardId: " + cardId);
            List<CardTransaction> transactions = campusCardService.getTransactionsByCardId(cardId);
            System.out.println("Debug: Returning transactions for cardId: " + cardId);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            System.err.println("Error in controller while fetching transactions for cardId: " + cardId + ", error: " + e.getMessage());
            e.printStackTrace();
            // 返回一个包含错误信息的ResponseEntity
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 或者返回一个包含错误详情的body
        }
    }

    // 根据用户ID获取网费信息
    @GetMapping("/internetFee/user/{userId}")
    public ResponseEntity<InternetFee> getInternetFeeByUserId(@PathVariable("userId") String userId) {
        InternetFee internetFee = campusCardService.getInternetFeeByUserId(userId);
        if (internetFee != null) {
            return ResponseEntity.ok(internetFee);
        }
        return ResponseEntity.notFound().build();
    }

    // 挂失校园卡
    @PostMapping("/{cardId}/reportLoss")
    public ResponseEntity<Void> reportLoss(@PathVariable("cardId") String cardId) {
        boolean success = campusCardService.reportLoss(cardId);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build(); // 可以返回更具体的错误信息，如卡片不存在或已挂失
    }

    // 解挂校园卡
     @PostMapping("/{cardId}/unreportLoss")
    public ResponseEntity<Void> unreportLoss(@PathVariable("cardId") String cardId) {
        boolean success = campusCardService.unreportLoss(cardId);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build(); // 可以返回更具体的错误信息，如卡片不存在或已是正常状态
    }

    // 可以根据需要添加其他API接口
} 