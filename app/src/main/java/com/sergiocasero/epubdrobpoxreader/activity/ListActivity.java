package com.sergiocasero.epubdrobpoxreader.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.util.Util;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private DropboxAPI<AndroidAuthSession> dropboxAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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
            for (DropboxAPI.Entry entry : result) {
                Log.i(TAG, entry.fileName());
            }
        }
    }
}
