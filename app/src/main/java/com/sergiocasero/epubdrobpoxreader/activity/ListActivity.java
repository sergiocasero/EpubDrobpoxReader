package com.sergiocasero.epubdrobpoxreader.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.adapter.BooksAdapter;
import com.sergiocasero.epubdrobpoxreader.mapper.BookModelMapper;
import com.sergiocasero.epubdrobpoxreader.model.BookModel;
import com.sergiocasero.epubdrobpoxreader.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";

    private static final String EMPTY_TEXT = "";

    private static final String slash = "/";

    private DropboxAPI<AndroidAuthSession> dropboxAPI;

    private List<BookModel> bookModels;

    private BooksAdapter adapter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation)
    NavigationView navigationView;

    @Bind(R.id.book_list)
    RecyclerView bookList;

    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        initRealm();
        initUI();
        initDropboxApi();
        initEpubDownloads();
    }

    private void initRealm() {
        realm = Realm.getInstance(this);
    }

    private void initUI() {
        toolbar.setTitle(R.string.my_books);
        setSupportActionBar(toolbar);


        bookModels = new ArrayList<>();
        adapter = new BooksAdapter(bookModels);
        bookList.setAdapter(adapter);
        bookList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initDropboxApi() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String token = preferences.getString(getString(R.string.token_key), "");

        AppKeyPair keys = new AppKeyPair(Util.APP_KEY, Util.APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(keys);
        session.setOAuth2AccessToken(token);

        dropboxAPI = new DropboxAPI<>(session);
        Log.i(TAG, dropboxAPI.getSession().toString());
    }

    public void initEpubDownloads() {
        FilesNameRetriever retriever = new FilesNameRetriever();
        retriever.execute();
    }

    public void getBooks(List<String> fileNames) {
        DownloadBook downloadBook;
        for (String fileName : fileNames) {
            downloadBook = new DownloadBook();
            downloadBook.execute(fileName);
        }
    }

    private class FilesNameRetriever extends AsyncTask<Void, Void, List<DropboxAPI.Entry>> {


        @Override
        protected List<DropboxAPI.Entry> doInBackground(Void... params) {
            try {
                return dropboxAPI.search(slash, getString(R.string.epub_extension), 1000, false);
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

    private class DownloadBook extends AsyncTask<String, String, Book> {

        @Override
        protected Book doInBackground(String... params) {
            try {
                String filePath = params[0];
                InputStream in = dropboxAPI.getFileStream(filePath, EMPTY_TEXT);
                EpubReader epubReader = new EpubReader();


                return epubReader.readEpub(in);

            } catch (IOException | DropboxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {

        }

        @Override
        protected void onPostExecute(Book book) {
            BookModelMapper bookModelMapper = new BookModelMapper();
            try {
                BookModel bookModel = bookModelMapper.dataToModel(book);
                adapter.add(bookModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
