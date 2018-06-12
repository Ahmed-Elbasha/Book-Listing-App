package com.example.ahmedelbasha.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {

    private static String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils () {

    }

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP Request.", e );
        }

        List<Book> books = extractItemsFromJsonResponse(jsonResponse);

        return books;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url.", e );
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(400000);
            urlConnection.setConnectTimeout(500000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response coe: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem retrieving the book JSON Response results.", e);

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

    private static String readFromStream(InputStream inputStream)throws  IOException {
        StringBuilder stringOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                stringOutput.append(line);
                line = reader.readLine();
            }
        }
        return stringOutput.toString();
    }

    private static List<Book> extractItemsFromJsonResponse(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(bookJSON);
            JSONArray items = rootJsonObject.optJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.optJSONObject(i);
                JSONObject volumeInfo = item.optJSONObject("volumeInfo");

                String bookTitle = volumeInfo.optString("title");

                String listOfAuthors = "";

                JSONArray authors = volumeInfo.optJSONArray("authors");

                for (int j = 0; j < authors.length(); j++) {
                    String author = authors.getString(j);
                    int authorsMaxLength = 1 - authors.length();
                    if (j == authorsMaxLength) {
                        listOfAuthors += author + ".";
                    } else {
                        listOfAuthors += author + ", ";
                    }
                }

                String description = volumeInfo.optString("description");

                String publishDate = volumeInfo.optString("publishedDate");

                JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");

                String thumbnailLink = imageLinks.optString("thumbnail");

                String previewLink = volumeInfo.optString("previewLink");

                Book book = new Book(bookTitle, listOfAuthors, description, thumbnailLink, previewLink, publishDate);

                books.add(book);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return  books;
    }
}
