package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.Book;
import com.example.AndroidServer.model.LibraryNotification;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LibraryMapper {

    // ====================== 收藏相关操作 ======================
    @Select("""
    SELECT
        b.id,
        b.book_name AS bookName,
        b.author,
        b.publishing_house AS publishingHouse,
        b.translator,
        b.publish_date AS publishDate,
        b.pages,
        b.price,
        b.brief_introduction AS briefIntroduction,
        b.author_introduction AS authorIntroduction,
        b.img_url AS imgUrl,
        b.category_id AS categoryId,
        c.name AS categoryName,
        b.ISBN
    FROM favorite_books f
    JOIN book_info b ON f.book_id = b.id
    LEFT JOIN category c ON b.category_id = c.id
    WHERE f.user_id = #{userId}
      AND b.del_flg = 0
    """)
    List<Book> getFavoritesByUserId(@Param("userId") int userId);

    @Insert("INSERT INTO favorite_books (user_id, book_id) VALUES (#{userId}, #{bookId})")
    void addFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    @Delete("DELETE FROM favorite_books WHERE user_id = #{userId} AND book_id = #{bookId}")
    void removeFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    // ====================== 阅读历史相关操作 ======================
    // 主要修复点：将 viewed_at 替换为 last_read_time
    @Select("""
    SELECT
        b.id,
        b.book_name AS bookName,
        b.author,
        b.publishing_house AS publishingHouse,
        b.translator,
        b.publish_date AS publishDate,
        b.pages,
        b.ISBN,
        b.price,
        b.brief_introduction AS briefIntroduction,
        b.author_introduction AS authorIntroduction,
        b.img_url AS imgUrl,
        b.del_flg AS delFlg,
        b.category_id AS categoryId,
        c.name AS categoryName,
        h.last_read_time AS viewTime
    FROM reading_history h
    JOIN book_info b ON h.book_id = b.id
    LEFT JOIN category c ON b.category_id = c.id
    WHERE h.user_id = #{userId}
      AND b.del_flg = 0
    ORDER BY h.last_read_time DESC
    LIMIT 50
    """)
    List<Book> getHistoryByUserId(@Param("userId") int userId);

    // 历史记录插入/更新逻辑保持不变
    @Insert("""
    INSERT INTO reading_history (user_id, book_id, last_read_time, read_count)
    VALUES (#{userId}, #{bookId}, NOW(), 1)
    ON DUPLICATE KEY UPDATE
        last_read_time = NOW(),
        read_count = read_count + 1
    """)
    void insertHistory(@Param("userId") int userId, @Param("bookId") int bookId);

    // ====================== 图书详情与搜索 ======================
    @Select("""
    SELECT
        b.id,
        b.book_name AS bookName,
        b.author,
        b.publishing_house AS publishingHouse,
        b.translator,
        b.publish_date AS publishDate,
        b.pages,
        b.ISBN,
        b.price,
        b.brief_introduction AS briefIntroduction,
        b.author_introduction AS authorIntroduction,
        b.img_url AS imgUrl,
        b.del_flg AS delFlg,
        b.category_id AS categoryId,
        c.name AS categoryName
    FROM book_info b
    JOIN category c ON b.category_id = c.id
    WHERE b.id = #{bookId}
      AND b.del_flg = 0
    """)
    Book getBookDetailsById(@Param("bookId") int bookId);

    @Select({
            "<script>",
            "SELECT ",
            "  b.id, ",
            "  b.book_name AS bookName, ",
            "  b.author, ",
            "  c.name AS categoryName, ",
            "  b.publishing_house AS publishingHouse, ",
            "  b.img_url AS imgUrl, ",
            "  b.price ",
            "FROM book_info b ",
            "JOIN category c ON b.category_id = c.id ",
            "WHERE b.del_flg = 0 ",
            "  AND (b.book_name LIKE CONCAT('%',#{keyword},'%') ",
            "       OR b.author LIKE CONCAT('%',#{keyword},'%') ",
            "       OR c.name LIKE CONCAT('%',#{keyword},'%')) ",
            "</script>"
    })
    List<Book> searchBooks(@Param("keyword") String keyword);

    // ====================== 分类查询 ======================
    @Select("""
    SELECT
        b.id,
        b.book_name AS bookName,
        b.author,
        b.publishing_house AS publishingHouse,
        b.translator,
        b.publish_date AS publishDate,
        b.pages,
        b.ISBN,
        b.price,
        b.brief_introduction AS briefIntroduction,
        b.author_introduction AS authorIntroduction,
        b.img_url AS imgUrl,
        b.del_flg AS delFlg,
        b.category_id AS categoryId,
        c.name AS categoryName
    FROM book_info b
    JOIN category c ON b.category_id = c.id
    WHERE b.del_flg = 0
      AND c.name = #{category}
    """)
    List<Book> getBooksByCategoryName(@Param("category") String category);

    // ====================== 通用查询 ======================
    @Select("""
    SELECT
        b.id,
        b.book_name AS bookName,
        b.author,
        b.publishing_house AS publishingHouse,
        b.translator,
        b.publish_date AS publishDate,
        b.pages,
        b.ISBN,
        b.price,
        b.brief_introduction AS briefIntroduction,
        b.author_introduction AS authorIntroduction,
        b.img_url AS imgUrl,
        b.del_flg AS delFlg,
        b.category_id AS categoryId,
        c.name AS categoryName
    FROM book_info b
    JOIN category c ON b.category_id = c.id
    WHERE b.del_flg = 0
    """)
    List<Book> getAllBooks();

    // ====================== 管理功能 ======================
    @Update("UPDATE book_info SET status = #{status} WHERE id = #{id}")
    void updateBookStatus(@Param("id") int id, @Param("status") String status);

    @Select("SELECT id, message, date FROM notification ORDER BY date DESC")
    List<LibraryNotification> getNotifications();
}
