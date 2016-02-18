package com.sergiocasero.epubdrobpoxreader.application;

import android.app.Application;

import com.sergiocasero.epubdrobpoxreader.model.AuthorModel;
import com.sergiocasero.epubdrobpoxreader.model.BookModel;
import com.sergiocasero.epubdrobpoxreader.model.DescriptionModel;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;

/**
 * Created by sergiocasero on 18/2/16.
 */
@RealmModule(classes = {BookModel.class, AuthorModel.class, DescriptionModel.class})
public class EpubApplication extends Application {

    private static EpubApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).setModules(new EpubApplication()).build();
        Realm.setDefaultConfiguration(config);
    }

    public static EpubApplication getInstance() {
        return instance;
    }
}
