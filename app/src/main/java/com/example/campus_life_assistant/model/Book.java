// ✅ 更新后的 Book.java（模型类）
package com.example.campus_life_assistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable {

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
    @SerializedName("categoryName")
    private String categoryName;

    // ✅ 新增字段：是否收藏 & 阅读次数
    @SerializedName("isFavorite")
    private boolean isFavorite;

    @SerializedName("readCount")
    private int readCount;

    // Parcelable 实现
    protected Book(Parcel in) {
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
        isFavorite = in.readByte() != 0;
        readCount = in.readInt();
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
    public void writeToParcel(Parcel dest, int flags) {
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
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeInt(readCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getBookName() { return bookName; }
    public String getAuthor() { return author; }
    public String getPublishingHouse() { return publishingHouse; }
    public String getTranslator() { return translator; }
    public String getPublishDate() { return publishDate; }
    public int getPages() { return pages; }
    public String getIsbn() { return isbn; }
    public double getPrice() { return price; }
    public String getBriefIntroduction() { return briefIntroduction; }
    public String getAuthorIntroduction() { return authorIntroduction; }
    public String getImgUrl() { return imgUrl; }
    public int getDelFlg() { return delFlg; }
    public String getCategoryName() { return categoryName; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }

    public Book updateFavoriteStatus() {
        this.isFavorite = !this.isFavorite;
        return this;
    }

    public int getReadCount() { return readCount; }
    public void setReadCount(int readCount) { this.readCount = readCount; }

    public Book() {}
}
