package com.sergiocasero.epubdrobpoxreader.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";

    private DropboxAPI<AndroidAuthSession> dropboxAPI;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation)
    NavigationView navigationView;

    @Bind(R.id.book_list)
    RecyclerView bookList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        initDropboxApi();
        getEpubNames();
    }

    private void initDropboxApi() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String token = preferences.getString(getString(R.string.token_key), "");

        AppKeyPair keys = new AppKeyPair(Util.APP_KEY, Util.APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(keys);
        session.setOAuth2AccessToken(token);

        dropboxAPI = new DropboxAPI<>(session);
        dropboxAPI.getSession().finishAuthentication();
    }

    public void getEpubNames() {
        FilesRetriever retriever = new FilesRetriever();
        retriever.execute();
    }

    public void getBooks(List<String> fileNames) {
        DownloadBook downloadBook;
        for (String fileName : fileNames) {
            downloadBook = new DownloadBook();
            downloadBook.execute(fileName);
        }
    }

    private class FilesRetriever extends AsyncTask<Void, Void, List<DropboxAPI.Entry>> {


        @Override
        protected List<DropboxAPI.Entry> doInBackground(Void... params) {
            try {
                return dropboxAPI.search("/", ".epub", 1000, false);
            } catch (DropboxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DropboxAPI.Entry> result) {
            List<String> fileNames = new ArrayList<>();
            for (DropboxAPI.Entry entry : result) {
                fileNames.add(entry.path);
            }

            
            getBooks(fileNames);
        }
    }

    private class DownloadBook extends AsyncTask<String, Void, Book> {

        @Override
        protected Book doInBackground(String... params) {
            try {
                InputStream fileStream = dropboxAPI.getFileStream(params[0], "");
                EpubReader epubReader = new EpubReader();
                return epubReader.readEpub(fileStream);
            } catch (DropboxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Book book) {
            List<Author> authors = book.getMetadata().getAuthors();
            Log.i(TAG, book.getTitle() + "- " + authors.toString());
        }
    }
}
