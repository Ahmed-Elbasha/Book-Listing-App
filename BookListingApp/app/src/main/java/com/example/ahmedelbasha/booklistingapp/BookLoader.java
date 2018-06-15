package com.example.ahmedelbasha.booklistingapp;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();
    private String mUrl;
    private static ShowToast showToast = new ShowToast();

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        if (mUrl == null) {
            return  null;
        }

        try {
            List<Book> bookList = QueryUtils.fetchBookData(mUrl);
            return bookList;
        } catch (Exception e) {
            Toast.makeText(showToast, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
            return null;
        }
    }
}

