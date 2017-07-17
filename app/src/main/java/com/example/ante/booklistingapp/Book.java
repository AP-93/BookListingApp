package com.example.ante.booklistingapp;

/**
 * Created by Ante on 16/07/2017.
 */

public class Book {

    private String mBookName, mAuthorName;

    public Book(String vBookName, String vAuthorName) {

        mBookName = vBookName;
        mAuthorName= vAuthorName;
    }

    public String getmBookName() {
        return mBookName;
    }

    public void setmBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }

    public void setmAuthorName(String mAuthorName) {
        this.mAuthorName = mAuthorName;
    }
}

