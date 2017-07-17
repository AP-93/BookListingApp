package com.example.ante.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ante on 16/07/2017.
 */

public class BookAdapter extends ArrayAdapter {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context       The current context. Used to inflate the layout file.
     * @param bookAndAuthor A List of ImageAndText objects to display in a list
     */
    public BookAdapter(Activity context, ArrayList<Book> bookAndAuthor) {

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for 2 TextViews , the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, bookAndAuthor);

    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_row, parent, false);
        }

        // Get the object located at this position in the list
        Book currentPlace = (Book) getItem(position);

        // Find the TextView in the book_list_row.xml layout with the ID bookName
        TextView bookNameTextView = (TextView) listItemView.findViewById(R.id.bookName);

        // Find the TextView in the  book_list_row.xml layout with the ID authorName
        TextView authorNameTextView = (TextView) listItemView.findViewById(R.id.authorName);

        // Get the book and author name from the current Book object and
        // set this text on the bookNameTextView and authorNameTextView
        bookNameTextView.setText(currentPlace.getmBookName());
        authorNameTextView.setText(currentPlace.getmAuthorName());


        // Return the whole list item layout (containing 2 TextViews )
        // so that it can be shown in the ListView
        return listItemView;
    }
}

