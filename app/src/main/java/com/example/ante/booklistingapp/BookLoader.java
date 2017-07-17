package com.example.ante.booklistingapp;

/**
 * Created by Ante on 17/07/2017.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Loads a list of books and authors by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = BookLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
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

        String jsonResponse = "";

        if (mUrl == null) {
            return null;
        }
        //creates URL object from string
        URL url = createUrl(mUrl);

        //call makeHttpRequest method from Utils.class and return json as string
        try {
            jsonResponse = Utils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        if (jsonResponse == null) {
            return null;
        }
        //call getBooks method from Utils.class to parse string jsonResponse
        ArrayList<Book> books = Utils.getBooks(jsonResponse);
        return books;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
}