package com.example.AndroidServer.model;

import java.util.Date;

public class Book {
    private Integer id;
    private String bookName;
    private String author;
    private String publishingHouse;
    private String translator;
    private Date publishDate;
    private Integer pages;
    private String ISBN;
    private Double price;
    private String briefIntroduction;
    private String authorIntroduction;
    private String imgUrl;
    private Integer delFlg;

    // 新增分类字段
    private Integer categoryId;
    private String categoryName;

    public Book() {}

    // getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublishingHouse() { return publishingHouse; }
    public void setPublishingHouse(String publishingHouse) { this.publishingHouse = publishingHouse; }

    public String getTranslator() { return translator; }
    public void setTranslator(String translator) { this.translator = translator; }

    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getBriefIntroduction() { return briefIntroduction; }
    public void setBriefIntroduction(String briefIntroduction) { this.briefIntroduction = briefIntroduction; }

    public String getAuthorIntroduction() { return authorIntroduction; }
    public void setAuthorIntroduction(String authorIntroduction) { this.authorIntroduction = authorIntroduction; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public Integer getDelFlg() { return delFlg; }
    public void setDelFlg(Integer delFlg) { this.delFlg = delFlg; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
