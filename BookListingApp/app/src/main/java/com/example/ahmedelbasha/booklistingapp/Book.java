package com.example.ahmedelbasha.booklistingapp;

public class Book {

    private String mBookTitle;
    private String mAuthorName;
    private String mDescription;
    private String mBookThumbnailLink;
    private String mPreviewLink;
    private String mPublishDate;


    public Book(String bookTitle, String authorName, String description, String bookThumbnailLink, String previewLink, String publishDate) {
        mBookTitle = bookTitle;
        mAuthorName = authorName;
        mDescription = description;
        mBookThumbnailLink = bookThumbnailLink;
        mPreviewLink = previewLink;
        mPublishDate = publishDate;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getBookThumbnailLink() {
        return mBookThumbnailLink;
    }

    public String getPreviewLink() {
        return mPreviewLink;
    }

    public String getPublishDate() {
        return mPublishDate;
    }
}
