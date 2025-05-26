// app/src/main/java/com/example/campus_life_assistant/model/Book.java
package com.example.campus_life_assistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable { // 实现Parcelable

    // 原有字段 -------------------------------------------------
    @SerializedName("categoryName")
    private String categoryName;

    @SerializedName("id")
    private int id;

    @SerializedName("bookName")
    private String bookName;

    @SerializedName("author")
    private String author;

    @SerializedName("publishingHouse")
    private String publishingHouse;

    @SerializedName("translator")
    private String translator;

    @SerializedName("publishDate")
    private String publishDate;

    @SerializedName("pages")
    private int pages;

    @SerializedName("ISBN")
    private String isbn;

    @SerializedName("price")
    private double price;

    @SerializedName("briefIntroduction")
    private String briefIntroduction;

    @SerializedName("authorIntroduction")
    private String authorIntroduction;

    @SerializedName("imgUrl")
    private String imgUrl;

    @SerializedName("delFlg")
    private int delFlg;

    // Parcelable 实现开始 ======================================
    protected Book(Parcel in) {
        // 必须严格按照写入顺序读取
        id = in.readInt();
        bookName = in.readString();
        author = in.readString();
        publishingHouse = in.readString();
        translator = in.readString();
        publishDate = in.readString();
        pages = in.readInt();
        isbn = in.readString();
        price = in.readDouble();
        briefIntroduction = in.readString();
        authorIntroduction = in.readString();
        imgUrl = in.readString();
        delFlg = in.readInt();
        categoryName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 必须严格按照构造函数顺序写入
        dest.writeInt(id);
        dest.writeString(bookName);
        dest.writeString(author);
        dest.writeString(publishingHouse);
        dest.writeString(translator);
        dest.writeString(publishDate);
        dest.writeInt(pages);
        dest.writeString(isbn);
        dest.writeDouble(price);
        dest.writeString(briefIntroduction);
        dest.writeString(authorIntroduction);
        dest.writeString(imgUrl);
        dest.writeInt(delFlg);
        dest.writeString(categoryName);
    }
    // Parcelable 实现结束 =====================================

    // 原有构造函数 --------------------------------------------
    public Book(String title, String author, String category, String imageUrl, float rating) {
        this.bookName = title;
        this.author = author;
        this.publishingHouse = category;
        this.imgUrl = imageUrl;
        this.price = rating;
    }

    public Book() {} // 必须保留无参构造

    // Getters & Setters --------------------------------------
    public String getCategoryName() {
        return categoryName;
    }

    public int getId() {
        return id;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public String getTranslator() {
        return translator;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public int getPages() {
        return pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public double getPrice() {
        return price;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public String getAuthorIntroduction() {
        return authorIntroduction;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getDelFlg() {
        return delFlg;
    }
}
