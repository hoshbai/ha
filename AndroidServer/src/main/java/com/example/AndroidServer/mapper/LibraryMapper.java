package com.example.AndroidServer.mapper;

import com.example.AndroidServer.model.Book;
import com.example.AndroidServer.model.LibraryNotification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 图书馆数据访问层
 * 包含收藏管理、阅读历史、图书详情、搜索分类等核心功能
 */
@Mapper
public interface LibraryMapper {

    // ====================== 核心数据操作 ======================
    // 收藏管理
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
            b.ISBN,
            b.del_flg AS delFlg,
            b.category_id AS categoryId,
            c.name AS categoryName
        FROM favorite_books f
        JOIN book_info b ON f.book_id = b.id
        LEFT JOIN category c ON b.category_id = c.id
        WHERE f.user_id = #{userId}
          AND b.del_flg = 0
    """)
    List<Book> getFavoritesByUserId(@Param("userId") int userId);
    // Mapper层：新增检查方法
    @Select("SELECT COUNT(*) FROM favorite_books WHERE user_id = #{userId} AND book_id = #{bookId}")
    int isFavorited(@Param("userId") int userId, @Param("bookId") int bookId);

    @Insert("INSERT INTO favorite_books (user_id, book_id) VALUES (#{userId}, #{bookId})")
    @Options(useGeneratedKeys = false) // 禁用自动生成主键
    void addFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    @Delete("DELETE FROM favorite_books WHERE user_id = #{userId} AND book_id = #{bookId}")
    void removeFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    @Select("SELECT COUNT(*) > 0 FROM favorite_books WHERE user_id = #{userId} AND book_id = #{bookId}")
    boolean isFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    // 阅读历史
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
            h.read_count AS readCount,
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

    @Insert("""
        INSERT INTO reading_history (user_id, book_id, last_read_time, read_count)
        VALUES (#{userId}, #{bookId}, NOW(), 1)
        ON DUPLICATE KEY UPDATE
            last_read_time = NOW(),
            read_count = read_count + 1
    """)
    void insertHistory(@Param("userId") int userId, @Param("bookId") int bookId);

    // ====================== 图书详情 ======================
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
            CASE WHEN f.book_id IS NOT NULL THEN 1 ELSE 0 END AS favorite,
            COALESCE(h.read_count, 0) AS readCount
        FROM book_info b
        JOIN category c ON b.category_id = c.id
        LEFT JOIN favorite_books f ON b.id = f.book_id AND f.user_id = #{userId}
        LEFT JOIN reading_history h ON b.id = h.book_id AND h.user_id = #{userId}
        WHERE b.id = #{bookId}
          AND b.del_flg = 0
    """)
    Book getBookDetailsByIdWithUser(@Param("bookId") int bookId, @Param("userId") int userId);

    // ====================== 搜索功能 ======================
    @Select("""
        SELECT
            b.id,
            b.book_name AS bookName,
            b.author,
            c.name AS categoryName,
            b.publishing_house AS publishingHouse,
            b.img_url AS imgUrl,
            b.price
        FROM book_info b
        JOIN category c ON b.category_id = c.id
        WHERE b.del_flg = 0
          AND (b.book_name LIKE CONCAT('%',#{keyword},'%')
               OR b.author LIKE CONCAT('%',#{keyword},'%')
               OR c.name LIKE CONCAT('%',#{keyword},'%'))
    """)
    List<Book> searchBooks(@Param("keyword") String keyword);

    @Select("""
        SELECT
            b.id,
            b.book_name AS bookName,
            b.author,
            c.name AS categoryName,
            b.publishing_house AS publishingHouse,
            b.img_url AS imgUrl,
            b.price,
            CASE WHEN f.book_id IS NOT NULL THEN 1 ELSE 0 END AS favorite
        FROM book_info b
        JOIN category c ON b.category_id = c.id
        LEFT JOIN favorite_books f ON b.id = f.book_id AND f.user_id = #{userId}
        WHERE b.del_flg = 0
          AND (b.book_name LIKE CONCAT('%',#{keyword},'%')
               OR b.author LIKE CONCAT('%',#{keyword},'%')
               OR c.name LIKE CONCAT('%',#{keyword},'%'))
    """)
    List<Book> searchBooksWithUser(@Param("keyword") String keyword, @Param("userId") int userId);

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
            CASE WHEN f.book_id IS NOT NULL THEN 1 ELSE 0 END AS favorite
        FROM book_info b
        JOIN category c ON b.category_id = c.id
        LEFT JOIN favorite_books f ON b.id = f.book_id AND f.user_id = #{userId}
        WHERE b.del_flg = 0
          AND c.name = #{category}
    """)
    List<Book> getBooksByCategoryNameWithUser(@Param("category") String category, @Param("userId") int userId);

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
            CASE WHEN f.book_id IS NOT NULL THEN 1 ELSE 0 END AS favorite
        FROM book_info b
        JOIN category c ON b.category_id = c.id
        LEFT JOIN favorite_books f ON b.id = f.book_id AND f.user_id = #{userId}
        WHERE b.del_flg = 0
    """)
    List<Book> getAllBooksWithUser(@Param("userId") int userId);

    // ====================== 管理功能 ======================
    @Update("UPDATE book_info SET status = #{status} WHERE id = #{id}")
    void updateBookStatus(@Param("id") int id, @Param("status") String status);

    @Select("SELECT id, message, date FROM notification ORDER BY date DESC")
    List<LibraryNotification> getNotifications();
}
