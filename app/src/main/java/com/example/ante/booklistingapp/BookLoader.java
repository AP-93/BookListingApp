package com.example.ante.booklistingapp;

/**
 * Created by Ante on 17/07/2017.
 */


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Loads a list of books and authors by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl ;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public ArrayList<Book> loadInBackground() {


        URL url = createUrl(createStringUrl());
        String jsonResponse = "";


        if (mUrl == null) {
            return null;
        }


        try {
            jsonResponse = Utils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Book> books = parseJson(jsonResponse);
        return books;

    }
    private ArrayList<Book> parseJson(String json) {

        if (json == null) {
            return null;
        }

        ArrayList<Book> books =  Utils.getBooks(json);
        return books;
    }
}