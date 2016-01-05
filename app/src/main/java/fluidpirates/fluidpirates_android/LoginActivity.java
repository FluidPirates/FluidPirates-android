package fluidpirates.fluidpirates_android;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;


import android.app.Activity;

import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import models.Group;
import models.Params;
import utils.GetJsonArrayAsync;
import utils.PostJsonObjectAsync;

public class LoginActivity extends Activity {
    private static final String LOGIN_URL = "http://fluidpirates.com/api/sessions";
    private static final String REGISTER_URL = "http://fluidpirates.com/api/users";
    private static final String TAG = "LoginActivity";
    public static final String PREFS_NAME = "FluidPiratesPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
            }
        });

        Button registerButton = (Button) findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegisterRequest();
            }
        });
    }

    protected void onLoginSuccess() {
        Intent intent = new Intent(this, GroupsActivity.class);
        startActivity(intent);
    }

    private void sendLoginRequest() {
        Params params = new Params();
        params.put("session[email]", ((EditText) findViewById(R.id.login_email_field)).getText().toString());
        params.put("session[password]", ((EditText) findViewById(R.id.login_password_field)).getText().toString());

        (new FetchToken(this, params)).execute(LOGIN_URL);
    }

    private void sendRegisterRequest() {
        Params params = new Params();
        params.put("user[email]", ((EditText) findViewById(R.id.login_email_field)).getText().toString());
        params.put("user[password]", ((EditText) findViewById(R.id.login_password_field)).getText().toString());

        (new FetchToken(this, params)).execute(REGISTER_URL);
    }

    private class FetchToken extends PostJsonObjectAsync {
        public FetchToken(Context context, Params params) {
            super(context, params);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                Log.d(TAG, json.toString());
                String token = json.getString("token");
                if (token.length() > 0) {
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("token", json.getString("token"));
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }
}

