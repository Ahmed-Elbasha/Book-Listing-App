package com.example.ahmedelbasha.booklistingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookAdapter mAdapter;
    private TextView issueStateTextView;
    private static final String LOG_TAG = MainActivity.class.getName();
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

        ListView bookListView = findViewById(R.id.books_list);

        bookListView.setAdapter(mAdapter);

        bookListView.setEmptyView(issueStateTextView);

        loadingIndicator = findViewById(R.id.loading_indicator);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = mAdapter.getItem(position);
                String bookPreviewUrl = null;
                if (currentBook != null) {
                    bookPreviewUrl = currentBook.getPreviewLink();
                }
                showBookPreviewLink(bookPreviewUrl);
            }
        });
    }

    private void showBookPreviewLink(String bookPreviewLink) {
        Uri bookPreviewUri = Uri.parse(bookPreviewLink);
        Intent navigateToBookPreviewLinkIntent = new Intent(getIntent().ACTION_VIEW, bookPreviewUri);
        if (navigateToBookPreviewLinkIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(navigateToBookPreviewLinkIntent);
        }
    }

    private String generateQueryRequestUrl (String bookKeyword) {
        String bookApiRequestUrl = "https://www.googleapis.com/books/v1/volumes?q=" + bookKeyword;
        return bookApiRequestUrl;
    }
}
