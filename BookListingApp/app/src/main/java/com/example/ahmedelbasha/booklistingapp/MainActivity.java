package com.example.ahmedelbasha.booklistingapp;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private BookAdapter mAdapter;
    private TextView issueStateTextView;
    private static final String LOG_TAG = MainActivity.class.getName();
    private ProgressBar loadingIndicator;
    private static final int LOADER_ID = 0;
    EditText searchTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

        issueStateTextView = findViewById(R.id.issue_state_text_view);

        ListView bookListView = findViewById(R.id.books_list);

        bookListView.setAdapter(mAdapter);

        bookListView.setEmptyView(issueStateTextView);

        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


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

        ImageButton searchButton = findViewById(R.id.search_button);

        searchTextField = findViewById(R.id.keyword_user_input);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();

                loadingIndicator.setVisibility(View.VISIBLE);

                ConnectivityManager connectivityManager =
                        (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                if ((activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) &&
                        (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI || activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE ||
                                activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIMAX || activeNetworkInfo.getType() == ConnectivityManager.TYPE_VPN)) {
                    initializeLoader(getLoaderManager());
                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    issueStateTextView.setText("No internet connection");
                }
            }
        });
    }

    private void initializeLoader(LoaderManager loaderManager) {
        loaderManager.destroyLoader(LOADER_ID);
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    private void showBookPreviewLink(String bookPreviewLink) {
        Uri bookPreviewUri = Uri.parse(bookPreviewLink);
        Intent navigateToBookPreviewLinkIntent = new Intent(getIntent().ACTION_VIEW, bookPreviewUri);
        if (navigateToBookPreviewLinkIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(navigateToBookPreviewLinkIntent);
        }
    }

    private String generateQueryRequestUrl (String bookKeyword) {
        String bookApiRequestUrl = "https://www.googleapis.com/books/v1/volumes?q=" + bookKeyword +"";
        return bookApiRequestUrl;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String searchKeyword = searchTextField.getText().toString();
        if (TextUtils.isEmpty(searchKeyword)) {
            return null;
        }
        String queryRequestUrl = generateQueryRequestUrl(searchKeyword);
        return new BookLoader(MainActivity.this, queryRequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);

            if ((books.size() != 0)) {
                loadingIndicator.setVisibility(View.GONE);
            } else {
                issueStateTextView.setText("No Books Available.");
                loadingIndicator.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
