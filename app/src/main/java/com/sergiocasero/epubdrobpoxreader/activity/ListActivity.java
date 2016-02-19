package com.sergiocasero.epubdrobpoxreader.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.adapter.BooksAdapter;
import com.sergiocasero.epubdrobpoxreader.application.EpubApplication;
import com.sergiocasero.epubdrobpoxreader.interfaces.OnBookLoaded;
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
import nl.siegmann.epublib.epub.EpubReader;

public class ListActivity extends AppCompatActivity implements OnBookLoaded {

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

    @Bind(R.id.progress)
    ContentLoadingProgressBar progressBar;

    @Bind(R.id.container)
    DrawerLayout container;

    Realm realm;

    private int totalBooks;

    private int booksLoaded;

    private boolean linearLayout = false;

    private boolean orderByName = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        initRealm();
        initDropboxApi();
        initUI();
        initBookDownloads();
        registerListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_layout:
                changeLayout(item);
                return true;
            case R.id.change_order:
                changeOrder(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeOrder(MenuItem menuItem) {
        if (orderByName) {
            adapter.orderByName();
            menuItem.setIcon(R.drawable.ic_date_range_white_24dp);

        } else {
            adapter.orderByDate();
            menuItem.setIcon(R.drawable.ic_filter_list_white_24dp);
        }
        orderByName = !orderByName;
    }

    private void changeLayout(MenuItem menuItem) {
        if (linearLayout) {

            bookList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapter.setItemViewType(BooksAdapter.LINEAR_TYPE);
            menuItem.setIcon(R.drawable.ic_dashboard_white_24dp);
        } else {
            int count = container.getWidth() / Util.dpToPx(BooksAdapter.ITEM_GRID_WIDTH_DP, getApplicationContext());
            bookList.setLayoutManager(new GridLayoutManager(this, count));
            adapter.setItemViewType(BooksAdapter.GRID_TYPE);

            menuItem.setIcon(R.drawable.ic_list_white_24dp);
        }
        linearLayout = !linearLayout;
    }

    private void registerListeners() {
        adapter.setOnItemClickListener(new BooksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);

                intent.putExtra(BookModel.PRIMARY_KEY_FIELD, bookModels.get(position).getName());
                Pair<View, String> p1 = Pair.create(v.findViewById(R.id.cover), "coverTransition");
                Pair<View, String> p2 = Pair.create(v.findViewById(R.id.title), "titleTransition");
                Pair<View, String> p3 = Pair.create(findViewById(R.id.toolbar), "toolbarTransition");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ListActivity.this, p1, p2, p3);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    private void initRealm() {
        realm = Realm.getInstance(EpubApplication.getInstance());
    }

    private void initUI() {
        toolbar.setTitle(R.string.my_books);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(toolbar);


        bookModels = new ArrayList<>();
        adapter = new BooksAdapter(bookModels);
        bookList.setAdapter(adapter);
        bookList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initDropboxApi() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String token = preferences.getString(getString(R.string.token_key), EMPTY_TEXT);

        AppKeyPair keys = new AppKeyPair(Util.APP_KEY, Util.APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(keys);
        session.setOAuth2AccessToken(token);

        dropboxAPI = new DropboxAPI<>(session);
    }

    public void initBookDownloads() {
        showMessage(R.string.downloading_message);
        FilesNameRetriever retriever = new FilesNameRetriever();
        retriever.execute();
        progressBar.show();
    }

    public void obtainBooks(List<String> fileNamesWithPath) {
        totalBooks = fileNamesWithPath.size();
        booksLoaded = 0;
        int progressPart = 100 / totalBooks;
        for (int i = 0; i < fileNamesWithPath.size(); i++) {
            String fileName = fileNamesWithPath.get(i);
            BookModel bookModel = realm.where(BookModel.class).equalTo(BookModel.PRIMARY_KEY_FIELD, parseBookName(fileName)).findFirst();
            int progress = progressPart * (i + 1);
            if (bookModel == null) {
                DownloadBook downloadBook = new DownloadBook(this, progress);
                downloadBook.execute(fileName);
            } else {
                adapter.add(bookModel);
                progressBar.setProgress(progress);
                booksLoaded++;
                this.onBookLoaded();
            }
        }
    }

    private String parseBookName(String filePath) {
        String[] pathParts = filePath.split(slash);
        String name = pathParts[pathParts.length - 1];
        return name.replace(getString(R.string.epub_extension), EMPTY_TEXT);
    }


    private void saveBookToRealm(BookModel bookModel) {
        realm.beginTransaction();
        realm.copyToRealm(bookModel);
        realm.commitTransaction();
    }

    private void showMessage(int messageId) {
        Snackbar.make(container, messageId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBookLoaded() {
        if (booksLoaded == totalBooks) {
            progressBar.setProgress(100);
            showMessage(R.string.all_books_ready);
            progressBar.hide();
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
            List<String> fileNamesWithPath = new ArrayList<>();
            for (DropboxAPI.Entry entry : result) {
                fileNamesWithPath.add(entry.path);
            }
            obtainBooks(fileNamesWithPath);
        }
    }

    private class DownloadBook extends AsyncTask<String, String, BookModel> {

        private OnBookLoaded listener;
        private int progress;

        public DownloadBook(OnBookLoaded listener, int progress) {
            this.listener = listener;
            this.progress = progress;
        }

        @Override
        protected BookModel doInBackground(String... params) {
            try {
                String filePath = params[0];
                BookModelMapper bookModelMapper = new BookModelMapper();
                InputStream in = dropboxAPI.getFileStream(filePath, EMPTY_TEXT);
                EpubReader epubReader = new EpubReader();
                BookModel bookModel = bookModelMapper.dataToModel(epubReader.readEpub(in));
                bookModel.setName(parseBookName(filePath));

                return bookModel;

            } catch (IOException | DropboxException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(BookModel bookModel) {
            adapter.add(bookModel);
            saveBookToRealm(bookModel);
            booksLoaded++;
            progressBar.setProgress(progress);
            // TODO: 18/2/16 find better way
            listener.onBookLoaded();
        }
    }


}
