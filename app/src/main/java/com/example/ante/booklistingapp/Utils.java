package com.example.ante.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Ante on 17/07/2017.
 */

public class Utils {


    private static final String LOG_TAG = "";


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static ArrayList<Book> getBooks(String json) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(json);

            if (jsonResponse.getInt("totalItems") == 0) {
                return books;
            }
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);

                JSONObject bookInfo = bookObject.getJSONObject("volumeInfo");

                String bookName = bookInfo.getString("title");
                JSONArray authorsArray = bookInfo.getJSONArray("authors");
                String authors = convertAuthorArrayToString(authorsArray);

                Book book = new Book(bookName, authors);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return books;
    }


    public static String convertAuthorArrayToString(JSONArray authorsList) throws JSONException {

        String authorsListInString = null;

        if (authorsList.length() == 0) {
            return null;
        }

        for (int i = 0; i < authorsList.length(); i++) {
            if (i == 0) {
                authorsListInString = authorsList.getString(0);
            } else {
                authorsListInString += ", " + authorsList.getString(i);
            }
        }

        return authorsListInString;
    }


}
