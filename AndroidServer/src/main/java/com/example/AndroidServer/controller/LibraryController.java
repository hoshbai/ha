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
    private JwtUtil jwtUtil; // JWT工具类

    @Autowired
    private UserMapper userMapper; // 用户Mapper

    @Autowired
    private LibraryMapper libraryMapper;

    // 收藏相关接口
    @GetMapping("/favorites")
    public ResponseEntity<List<Book>> getFavorites(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if(user == null) return ResponseEntity.status(401).build();

        List<Book> books = mapper.getFavoritesByUserId(user.getU_id());
        return ResponseEntity.ok(books);
    }

    @PostMapping("/books/{bookId}/favorite")
    public ResponseEntity<?> toggleFavorite(
            @PathVariable int bookId,
            @RequestParam String action,
            @RequestHeader("Authorization") String token) { // 确保使用正确的大小写
        // 原认证逻辑保持不变
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if(user == null) return ResponseEntity.status(401).build();
        // 操作收藏
        if("add".equals(action)){
            mapper.addFavorite(user.getU_id(), bookId);
        }else{
            mapper.removeFavorite(user.getU_id(), bookId);
        }
        return ResponseEntity.ok().build();
    }

    // 阅读历史相关接口
    @GetMapping("/history")
    public ResponseEntity<List<Book>> getHistory(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if(user == null) return ResponseEntity.status(401).build();

        List<Book> books = mapper.getHistoryByUserId(user.getU_id());
        return ResponseEntity.ok(books);
    }

    @PostMapping("/books/{bookId}/record-view")
    public ResponseEntity<?> recordView(
            @PathVariable int bookId,
            @RequestHeader("Authorization") String token) {

        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        User user = userMapper.selectByNameOnly(username);
        if(user == null) return ResponseEntity.status(401).build();

        mapper.insertHistory(user.getU_id(), bookId);
        return ResponseEntity.ok().build();
    }

    @Autowired
    private LibraryMapper mapper; // 确保实例注入正确

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String keyword) {
        List<Book> books = mapper.searchBooks("%" + keyword + "%");
        return ResponseEntity.ok(books);
    }
    // 根据图书 ID 查询详情
    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookDetails(@PathVariable int bookId) {
        Book book = mapper.getBookDetailsById(bookId);
        if (book == null) {
            // 返回标准化错误格式
            Map<String, Object> error = new HashMap<>();
            error.put("code", 404);
            error.put("message", "ID为 " + bookId + " 的图书不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok().body(book);
    }

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

    @PostMapping("/books/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable int id, @RequestParam String status) {
        mapper.updateBookStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<LibraryNotification>> notifications() {
        return ResponseEntity.ok(mapper.getNotifications());
    }
}
