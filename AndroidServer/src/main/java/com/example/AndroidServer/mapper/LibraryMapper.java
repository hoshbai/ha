package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.Book;
import com.example.AndroidServer.model.LibraryNotification;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LibraryMapper {

    // 修复1：移除了错误的 static 修饰符
    @Select("SELECT b.id, b.book_name AS bookName, b.author, b.publishing_house AS publishingHouse, " +
            "b.translator, b.publish_date AS publishDate, b.pages, b.ISBN, b.price, " +
            "b.brief_introduction AS briefIntroduction, b.author_introduction AS authorIntroduction, " +
            "b.img_url AS imgUrl, b.del_flg AS delFlg, " +
            "b.category_id AS categoryId, c.name AS categoryName " +
            "FROM book_info b " +
            "JOIN category c ON b.category_id = c.id " +
            "WHERE b.id = #{bookId} AND b.del_flg = 0")
    Book getBookDetailsById(@Param("bookId") int bookId);  // 移除了 static 关键字

    // 修复2：统一参数命名
    @Select("SELECT b.id, b.book_name AS bookName, b.author, b.publishing_house AS publishingHouse, " +
            "b.translator, b.publish_date AS publishDate, b.pages, b.ISBN, b.price, " +
            "b.brief_introduction AS briefIntroduction, b.author_introduction AS authorIntroduction, " +
            "b.img_url AS imgUrl, b.del_flg AS delFlg, " +
            "b.category_id AS categoryId, c.name AS categoryName " +
            "FROM book_info b JOIN category c ON b.category_id = c.id " +
            "WHERE b.del_flg = 0 AND c.name = #{category}") // #{category} 对应 @Param("category")
    List<Book> getBooksByCategoryName(@Param("category") String category);  // 参数名改为category

    // 其他正确的方法 ↓
    @Select("SELECT b.id, b.book_name AS bookName, b.author, b.publishing_house AS publishingHouse, " +
            "b.translator, b.publish_date AS publishDate, b.pages, b.ISBN, b.price, " +
            "b.brief_introduction AS briefIntroduction, b.author_introduction AS authorIntroduction, " +
            "b.img_url AS imgUrl, b.del_flg AS delFlg, " +
            "b.category_id AS categoryId, c.name AS categoryName " +
            "FROM book_info b JOIN category c ON b.category_id = c.id " +
            "WHERE b.del_flg = 0")
    List<Book> getAllBooks();

    @Update("UPDATE book_info SET status = #{status} WHERE id = #{id}")
    void updateBookStatus(@Param("id") int id, @Param("status") String status);

    @Select("SELECT id, message, date FROM notification ORDER BY date DESC")
    List<LibraryNotification> getNotifications();
}
