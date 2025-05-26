package com.example.AndroidServer.service;

import com.example.AndroidServer.mapper.CampusCardMapper;
import com.example.AndroidServer.mapper.InternetFeeMapper;
import com.example.AndroidServer.model.CampusCard;
import com.example.AndroidServer.model.CardTransaction;
import com.example.AndroidServer.model.InternetFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CampusCardService {

    private final CampusCardMapper campusCardMapper;
    private final InternetFeeMapper internetFeeMapper;

    // 定义卡片状态常量
    private static final int STATUS_NORMAL = 0; // 正常
    private static final int STATUS_LOST = 1;   // 已挂失

    @Autowired
    public CampusCardService(CampusCardMapper campusCardMapper, InternetFeeMapper internetFeeMapper) {
        this.campusCardMapper = campusCardMapper;
        this.internetFeeMapper = internetFeeMapper;
    }

    // 根据用户ID获取校园卡信息
    public CampusCard getCampusCardByUserId(String userId) {
        return campusCardMapper.getCampusCardByUserId(userId);
    }

    // 根据卡ID获取校园卡信息
    public CampusCard getCampusCardByCardId(String cardId) {
        return campusCardMapper.getCampusCardByCardId(cardId);
    }

    // 创建新的校园卡 (用于用户第一次使用校园卡功能)
    @Transactional
    public void createCampusCard(CampusCard campusCard) {
        // 在这里可以生成 cardId，例如使用 UUID 或者其他策略
        // campusCard.setCardId(UUID.randomUUID().toString());
        campusCard.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        campusCard.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        campusCard.setStatus(STATUS_NORMAL); // 新创建的卡片状态为正常
        campusCardMapper.insertCampusCard(campusCard);

        // 同时为新用户创建网费记录，初始余额为0
        InternetFee internetFee = new InternetFee();
        internetFee.setUserId(campusCard.getUserId()); // 关联用户ID
        internetFee.setBalance(BigDecimal.ZERO);
        internetFee.setLastUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        internetFeeMapper.insertInternetFee(internetFee);
    }

    // 校园卡充值
    @Transactional
    public boolean recharge(String cardId, BigDecimal amount) {
        CampusCard campusCard = campusCardMapper.getCampusCardByCardId(cardId);
        if (campusCard == null) {
            return false; // 卡片不存在
        }

        // 检查卡片状态，如果已挂失则不能充值
        if (campusCard.getStatus() == STATUS_LOST) {
            return false; // 卡片已挂失
        }

        BigDecimal newBalance = campusCard.getBalance().add(amount);
        campusCard.setBalance(newBalance);
        campusCard.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        campusCardMapper.updateCampusCard(campusCard);

        CardTransaction transaction = new CardTransaction();
        transaction.setCardId(cardId);
        transaction.setAmount(amount);
        transaction.setTransactionType("recharge");
        transaction.setTransactionTime(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setDescription("在线充值");
        campusCardMapper.insertCardTransaction(transaction);

        return true;
    }

    // 校园卡消费
    @Transactional
    public boolean consume(String cardId, BigDecimal amount, String description) {
        CampusCard campusCard = campusCardMapper.getCampusCardByCardId(cardId);
        if (campusCard == null) {
            return false; // 卡片不存在
        }

        // 检查卡片状态，如果已挂失则不能消费
         if (campusCard.getStatus() == STATUS_LOST) {
            return false; // 卡片已挂失
        }

        if (campusCard.getBalance().compareTo(amount) < 0) {
            return false; // 余额不足
        }

        BigDecimal newBalance = campusCard.getBalance().subtract(amount);
        campusCard.setBalance(newBalance);
        campusCard.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        campusCardMapper.updateCampusCard(campusCard);

        CardTransaction transaction = new CardTransaction();
        transaction.setCardId(cardId);
        transaction.setAmount(amount.negate());
        transaction.setTransactionType("payment");
        transaction.setTransactionTime(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setDescription(description);
        campusCardMapper.insertCardTransaction(transaction);

        return true;
    }

    // 支付网费
    @Transactional
    public boolean payInternetFee(String cardId, BigDecimal amount) {
        CampusCard campusCard = campusCardMapper.getCampusCardByCardId(cardId);
        if (campusCard == null) {
            return false; // 校园卡不存在
        }

        // 检查卡片状态，如果已挂失则不能支付网费
         if (campusCard.getStatus() == STATUS_LOST) {
            return false; // 卡片已挂失
        }

        // 获取用户ID来查询网费信息
        String userId = campusCard.getUserId();
        InternetFee internetFee = internetFeeMapper.getInternetFeeByUserId(userId);

        if (internetFee == null) {
            // 如果网费记录不存在，创建一条
            internetFee = new InternetFee();
            internetFee.setUserId(userId);
            internetFee.setBalance(BigDecimal.ZERO);
            internetFee.setLastUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            internetFeeMapper.insertInternetFee(internetFee);
             // 重新获取以便更新
             internetFee = internetFeeMapper.getInternetFeeByUserId(userId);
        }

        // 检查校园卡余额是否充足
        if (campusCard.getBalance().compareTo(amount) < 0) {
            return false; // 校园卡余额不足
        }

        // 从校园卡余额扣款
        BigDecimal newCardBalance = campusCard.getBalance().subtract(amount);
        campusCard.setBalance(newCardBalance);
        campusCard.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        campusCardMapper.updateCampusCard(campusCard);

        // 更新网费余额 (增加网费)
        BigDecimal newInternetFeeBalance = internetFee.getBalance().add(amount);
        internetFee.setBalance(newInternetFeeBalance);
        internetFee.setLastUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        internetFeeMapper.updateInternetFee(internetFee);

        // 记录网费支付交易 (在校园卡账单中显示为支出)
        CardTransaction transaction = new CardTransaction();
        transaction.setCardId(cardId);
        transaction.setAmount(amount.negate()); // 支付网费是支出，金额为负
        transaction.setTransactionType("internet_fee");
        transaction.setTransactionTime(Timestamp.valueOf(LocalDateTime.now()));
        transaction.setDescription("支付网费");
        campusCardMapper.insertCardTransaction(transaction);

        return true;
    }

    // 挂失校园卡
    @Transactional
    public boolean reportLoss(String cardId) {
        CampusCard campusCard = campusCardMapper.getCampusCardByCardId(cardId);
        if (campusCard == null) {
            return false; // 卡片不存在
        }

        // 检查当前状态，避免重复挂失
        if (campusCard.getStatus() == STATUS_LOST) {
            return false; // 卡片已经是挂失状态
        }

        campusCardMapper.updateCampusCardStatus(cardId, STATUS_LOST);
        return true;
    }

    // 解挂校园卡
    @Transactional
    public boolean unreportLoss(String cardId) {
         CampusCard campusCard = campusCardMapper.getCampusCardByCardId(cardId);
        if (campusCard == null) {
            return false; // 卡片不存在
        }

        // 检查当前状态，避免重复解挂
        if (campusCard.getStatus() == STATUS_NORMAL) {
            return false; // 卡片已经是正常状态
        }

        campusCardMapper.updateCampusCardStatus(cardId, STATUS_NORMAL);
        return true;
    }

    // 根据用户ID获取网费信息
    public InternetFee getInternetFeeByUserId(String userId) {
        return internetFeeMapper.getInternetFeeByUserId(userId);
    }

    // 获取交易记录
    public List<CardTransaction> getTransactionsByCardId(String cardId) {
        try {
            System.out.println("Debug: Attempting to fetch transactions for cardId: " + cardId);
            List<CardTransaction> transactions = campusCardMapper.getTransactionsByCardId(cardId);
            System.out.println("Debug: Successfully fetched " + (transactions != null ? transactions.size() : 0) + " transactions for cardId: " + cardId);
            return transactions;
        } catch (Exception e) {
            System.err.println("Error fetching transactions for cardId: " + cardId + ", error: " + e.getMessage());
            e.printStackTrace();
            // 可以选择抛出自定义异常或者返回null/空列表
            throw new RuntimeException("Failed to fetch transactions for cardId: " + cardId, e);
        }
    }

    // 可以根据需要添加其他业务逻辑方法
} 