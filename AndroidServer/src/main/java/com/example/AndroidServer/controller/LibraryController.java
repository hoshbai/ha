package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.LibraryMapper;
import com.example.AndroidServer.mapper.UserMapper;
import com.example.AndroidServer.model.Book;
import com.example.AndroidServer.model.LibraryNotification;
import com.example.AndroidServer.model.User;
import com.example.AndroidServer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LibraryMapper mapper;

    // ✅ 合并接口：根据 token 动态处理登录/未登录状态
    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookDetails(
            @PathVariable int bookId,
            @RequestHeader(name = "Authorization", required = false) String token) {
        if (token != null && !token.isEmpty()) {
            // 已登录用户：获取包含收藏状态的详情
            String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            User user = userMapper.selectByNameOnly(username);
            if (user == null) return ResponseEntity.status(401).build();

            Book book = mapper.getBookDetailsByIdWithUser(bookId, user.getU_id());
            if (book == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 404);
                error.put("message", "ID为 " + bookId + " 的图书不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok().body(book);
        } else {
            // 未登录用户：获取基础信息
            Book book = mapper.getBookDetailsById(bookId);
            if (book == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("code", 404);
                error.put("message", "ID为 " + bookId + " 的图书不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            return ResponseEntity.ok().body(book);
        }
    }

    // 收藏相关接口
    @GetMapping("/favorites")
    public ResponseEntity<List<Book>> getFavorites(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if (user == null) return ResponseEntity.status(401).build();
        List<Book> books = mapper.getFavoritesByUserId(user.getU_id());
        return ResponseEntity.ok(books);
    }

    @PostMapping("/books/{bookId}/favorite")
    public ResponseEntity<?> toggleFavorite(
            @PathVariable int bookId,
            @RequestParam String action,
            @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if (user == null) return ResponseEntity.status(401).build();

        if ("add".equals(action)) {
            if (!mapper.isFavorite(user.getU_id(), bookId)) {
                mapper.addFavorite(user.getU_id(), bookId);
            }
        } else {
            mapper.removeFavorite(user.getU_id(), bookId);
        }
        return ResponseEntity.ok().build();
    }

    // 阅读历史相关接口
    @GetMapping("/history")
    public ResponseEntity<List<Book>> getHistory(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if (user == null) return ResponseEntity.status(401).build();
        List<Book> books = mapper.getHistoryByUserId(user.getU_id());
        return ResponseEntity.ok(books);
    }

    @PostMapping("/books/{bookId}/record-view")
    public ResponseEntity<?> recordView(
            @PathVariable int bookId,
            @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if (user == null) return ResponseEntity.status(401).build();
        mapper.insertHistory(user.getU_id(), bookId);
        return ResponseEntity.ok().build();
    }

    // 图书搜索
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword) {
        List<Book> books = mapper.searchBooks("%" + keyword + "%");
        return ResponseEntity.ok(books);
    }

    // 所有图书（带分类）
    @GetMapping("/books")
    public ResponseEntity<List<Book>> allBooks(
            @RequestParam(value = "category", required = false, defaultValue = "全部") String category) {
        List<Book> books;
        if ("全部".equals(category)) {
            books = mapper.getAllBooks();
        } else {
            books = mapper.getBooksByCategoryName(category);
        }
        return ResponseEntity.ok(books);
    }

    // 更新图书状态（管理功能）
    @PostMapping("/books/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable int id, @RequestParam String status) {
        mapper.updateBookStatus(id, status);
        return ResponseEntity.ok().build();
    }

    // 获取通知
    @GetMapping("/notifications")
    public ResponseEntity<List<LibraryNotification>> notifications() {
        return ResponseEntity.ok(mapper.getNotifications());
    }
}