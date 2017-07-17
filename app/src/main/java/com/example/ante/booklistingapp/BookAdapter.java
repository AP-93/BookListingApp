package com.example.ante.booklistingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Ante on 16/07/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Activity context, int resource) {

        super(context, resource);

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
        Book currentPlace = getItem(position);

        // Find the TextView in the book_list_row.xml layout with the ID bookName and ID authorName
        TextView bookNameTextView = (TextView) listItemView.findViewById(R.id.bookName);
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

