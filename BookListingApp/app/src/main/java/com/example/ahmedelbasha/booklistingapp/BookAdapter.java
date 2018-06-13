package com.example.ahmedelbasha.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, @NonNull List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView bookTitleTextView = convertView.findViewById(R.id.book_title_text_view);
        bookTitleTextView.setText(currentBook.getBookTitle());

        TextView publishDateTextView = convertView.findViewById(R.id.publish_date);
        publishDateTextView.setText(currentBook.getPublishDate());

        TextView authorNameTextView = convertView.findViewById(R.id.author_name_text_view);
        authorNameTextView.setText(currentBook.getAuthors());

        TextView bookDescriptionTextView = convertView.findViewById(R.id.description_text_view);
        bookDescriptionTextView.setText(currentBook.getDescription());

        int loader = R.drawable.loader;

        ImageView bookImage = convertView.findViewById(R.id.book_thumbnail_image_view);

        String bookImageThumbnailUrl = currentBook.getBookThumbnailLink();

        Picasso.with(getContext()).load(bookImageThumbnailUrl).error(R.drawable.no_image).fit().centerCrop().into(bookImage);

        return convertView;
    }
}
