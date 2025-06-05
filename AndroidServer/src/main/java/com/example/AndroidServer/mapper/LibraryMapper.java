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

    @Insert("INSERT INTO favorite_books (user_id, book_id) VALUES (#{userId}, #{bookId})")
    void addFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    @Delete("DELETE FROM favorite_books WHERE user_id = #{userId} AND book_id = #{bookId}")
    void removeFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    // 检查用户是否收藏了某本书
    @Select("SELECT COUNT(*) > 0 FROM favorite_books WHERE user_id = #{userId} AND book_id = #{bookId}")
    boolean isFavorite(@Param("userId") int userId, @Param("bookId") int bookId);

    // ====================== 阅读历史相关操作 ======================
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

    // 获取书籍详情（包含收藏状态和阅读次数）
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

    // 搜索书籍（包含收藏状态）
    @Select({
            "<script>",
            "SELECT ",
            "  b.id, ",
            "  b.book_name AS bookName, ",
            "  b.author, ",
            "  c.name AS categoryName, ",
            "  b.publishing_house AS publishingHouse, ",
            "  b.img_url AS imgUrl, ",
            "  b.price, ",
            "  CASE WHEN f.book_id IS NOT NULL THEN 1 ELSE 0 END AS favorite ",
            "FROM book_info b ",
            "JOIN category c ON b.category_id = c.id ",
            "LEFT JOIN favorite_books f ON b.id = f.book_id AND f.user_id = #{userId} ",
            "WHERE b.del_flg = 0 ",
            "  AND (b.book_name LIKE CONCAT('%',#{keyword},'%') ",
            "       OR b.author LIKE CONCAT('%',#{keyword},'%') ",
            "       OR c.name LIKE CONCAT('%',#{keyword},'%')) ",
            "</script>"
    })
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

    // 按分类查询书籍（包含收藏状态）
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

    // 获取所有书籍（包含收藏状态）
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