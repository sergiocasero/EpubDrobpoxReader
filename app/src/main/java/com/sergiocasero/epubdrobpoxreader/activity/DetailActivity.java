package com.sergiocasero.epubdrobpoxreader.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.adapter.AuthorAdapter;
import com.sergiocasero.epubdrobpoxreader.application.EpubApplication;
import com.sergiocasero.epubdrobpoxreader.model.BookModel;
import com.sergiocasero.epubdrobpoxreader.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {

    Realm realm;

    BookModel bookModel;

    @Bind(R.id.cover)
    ImageView cover;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.authors)
    RecyclerView authors;

    @Bind(R.id.authors_bar)
    Toolbar authorsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String bookName = getIntent().getExtras().getString(BookModel.PRIMARY_KEY_FIELD);

        initRealm(bookName);
        initUI();

    }

    private void initUI() {
        cover.setImageBitmap(Util.stringToBitMap(bookModel.getCover()));
        title.setText(bookModel.getTitle());

        authorsBar.setTitle(R.string.authors);
        authorsBar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
        AuthorAdapter authorAdapter = new AuthorAdapter(bookModel.getAuthors());
        authors.setAdapter(authorAdapter);
        authors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initRealm(String name) {
        Realm realm = Realm.getInstance(EpubApplication.getInstance());
        bookModel = realm.where(BookModel.class).equalTo(BookModel.PRIMARY_KEY_FIELD, name).findFirst();
    }


}
