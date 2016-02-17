package com.sergiocasero.epubdrobpoxreader.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.sergiocasero.epubdrobpoxreader.R;
import com.sergiocasero.epubdrobpoxreader.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private DropboxAPI<AndroidAuthSession> dropboxAPI;

    @Bind(R.id.login_button)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initializeDropboxApi();
    }

    protected void onResume() {
        super.onResume();

        if (dropboxAPI.getSession().authenticationSuccessful()) {
            try {
                dropboxAPI.getSession().finishAuthentication();

                SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String accessToken = dropboxAPI.getSession().getOAuth2AccessToken();
                editor.putString(getString(R.string.token_key), accessToken);

                Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                startActivity(intent);

            } catch (IllegalStateException e) {
                Log.e(TAG, "Error authenticating", e);
            }
        }
    }

    private void initializeDropboxApi() {
        AppKeyPair keys = new AppKeyPair(Util.APP_KEY, Util.APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(keys);
        dropboxAPI = new DropboxAPI<>(session);
    }

    @OnClick(R.id.login_button)
    public void login(View v) {
        dropboxAPI.getSession().startOAuth2Authentication(this);
    }


}
