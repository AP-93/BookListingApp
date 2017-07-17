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
            //setting up HTTP request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            //establish connection
            urlConnection.connect();

            // If the request was successful (response code 200),
            // read the input stream and parse the response.
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

    //parse string json and return ArrayList<Book>
    public static ArrayList<Book> getBooks(String json) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(json);

            //if there is no books return early
            if (jsonResponse.getInt("totalItems") == 0) {
                return books;
            }
            //"items" is array which contains other objects
            JSONArray jsonArray = jsonResponse.getJSONArray("items");

            //run trough each "items" object and get book and author name
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);

                //object "volumeInfo" contains object "title" and array "authors"
                JSONObject bookInfo = bookObject.getJSONObject("volumeInfo");

                //get string from JSONObject
                String bookName = bookInfo.getString("title");

                String authors;
                if (bookInfo.has("authors")) {
                    // get JSONArray for authors and call method to convert array to string
                    JSONArray authorsArray = bookInfo.getJSONArray("authors");
                    authors = convertAuthorArrayToString(authorsArray);
                } else {
                   authors = "N/A";
                }

                //add strings bookName and authors to Book object
                Book book = new Book(bookName, authors);
                //add Book object to ArrayList<Book> books
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    //method used to get string from Json array
    public static String convertAuthorArrayToString(JSONArray authorsList) throws JSONException {

        String authorsListInString = null;

        //if there is no authors return early
        if (authorsList.length() == 0) {
            return null;
        }

        for (int i = 0; i < authorsList.length(); i++) {
            //for only one author get string and end loop
            if (i == 0) {
                authorsListInString = authorsList.getString(0);
                // for multiple authors put "," and add next author
            } else {
                authorsListInString += ", " + authorsList.getString(i);
            }
        }
        return authorsListInString;
    }
}