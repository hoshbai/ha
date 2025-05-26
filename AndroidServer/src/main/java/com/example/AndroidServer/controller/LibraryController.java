package com.example.AndroidServer.controller;

import com.example.AndroidServer.mapper.LibraryMapper;
import com.example.AndroidServer.model.Book;
import com.example.AndroidServer.model.LibraryNotification;
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
    private LibraryMapper mapper; // 确保实例注入正确

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
