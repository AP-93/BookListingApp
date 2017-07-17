package com.example.ante.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    ImageButton searchButton;
    ListView bookListView;
    EditText editTextView;
    TextView noResult;
    BookAdapter bookAdapter;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * base URL for book list
     */
    public static final String GOOGLE_BOOKS_API_BASE_QUERY = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = (ImageButton) findViewById(R.id.searchButton);
        bookListView = (ListView) findViewById(R.id.listView);
        editTextView = (EditText) findViewById(R.id.editTextView);
        noResult = (TextView) findViewById(R.id.noResult);

        //book adapter initialization
        bookAdapter = new BookAdapter(this, -1);
        bookListView.setAdapter(bookAdapter);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkConnected()) {
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    noResult.setVisibility(View.GONE);

                } else {
                    Toast.makeText(MainActivity.this, "Check internet connection",
                            Toast.LENGTH_SHORT).show();
                    return;
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

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {

        // Create a new loader for the given URL
        return new BookLoader(this, createStringUrl());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {

        noResult.setVisibility(View.VISIBLE);

        // Clear the adapter of previous book data
        bookAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {

            noResult.setVisibility(View.GONE);
            bookAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        bookAdapter.clear();

    }
}