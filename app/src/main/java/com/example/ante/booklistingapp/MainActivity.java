package com.example.ante.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "";
    ImageButton searchButton;
    ListView bookListView;
    EditText editTextView;
    URL finalUrl;
    BookAdapter bookAdapter;

    /**
     * base URL for book list
     */
    public static final String GOOGLE_BOOKS_API_BASE_QUERY =
            "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (ImageButton) findViewById(R.id.searchButton);
        bookListView = (ListView) findViewById(R.id.listView);
        editTextView = (EditText) findViewById(R.id.editTextView);




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isNetworkConnected()){
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute();
                } else {
                    Toast.makeText(MainActivity.this, "Check internet connection",
                            Toast.LENGTH_SHORT).show();
                }

                editTextView.clearFocus();
            }


        });



    }





    //Check to make sure it is connected to a network:
    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }





    //take user input from edit text and combine it with base address
    public String createStringUrl() {

        //get text from edit text, convert it to string , remove all blank spaces before and after string, replace all spaces between strings
        String userInput = editTextView.getText().toString().trim().replaceAll("\\s+", "+");
        return GOOGLE_BOOKS_API_BASE_QUERY + userInput;
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







    /////////////////////ASYNC TASK//////////////////////////////
    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            URL url = createUrl(createStringUrl());
            String jsonResponse = "";

            try {
                jsonResponse = Utils.makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing input stream", e);
            }

            ArrayList<Book> books = parseJson(jsonResponse);
            return books;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books == null) {
                return;
            }
            updateUi(books);
        }


    }

    private void updateUi(ArrayList<Book> books) {



        // Create an BookAdapter, whose data source is a list of
        // Books's. The adapter knows how to create list item views for each item
        // in the list.
        bookAdapter = new BookAdapter(this, books);

        // attach the adapter to the listView.
        bookListView.setAdapter(bookAdapter);


    }
    ///////////////////////////////////////////////////////////////////////////////


    private ArrayList<Book> parseJson(String json) {

        if (json == null) {
            return null;
        }

        ArrayList<Book> books =  Utils.getBooks(json);
        return books;
    }


}


