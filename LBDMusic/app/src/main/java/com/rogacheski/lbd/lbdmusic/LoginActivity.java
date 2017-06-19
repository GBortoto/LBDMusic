package com.rogacheski.lbd.lbdmusic;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rogacheski.lbd.lbdmusic.base.baseActivity;
import com.rogacheski.lbd.lbdmusic.session.Session;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.loopj.android.http.*;
import org.json.*;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends baseActivity {

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.btn_reset_password) Button _resetButton;
    @Bind(R.id.btn_signup) Button _signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TAG = "LoginActivity";
        mContext = LoginActivity.this;
        session = new Session(mContext);

        if(CheckUserLogado()) {
            TransitionLeft(MainActivity.class);
        }

        ButterKnife.bind(this);

        _resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionLeft(ResetActivity.class);
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionLeft(SignupActivity.class);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        ActivityManager.TaskDescription tDesk = new ActivityManager.TaskDescription(getString(R.string.app_name),bm,getResources().getColor(R.color.colorPrimaryDark));
        this.setTaskDescription(tDesk);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }


    public void login() {
        WriteLog("Iniciando Login");

        _loginButton.setEnabled(false);
        _resetButton.setEnabled(false);
        _signupButton.setEnabled(false);

        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        Boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            _passwordText.setError("enter a password with more than 6 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if(!valid) {
            _loginButton.setEnabled(true);
            _resetButton.setEnabled(true);
            _signupButton.setEnabled(true);
            WriteLog("Login Error Validate");
            return;
        }

        ShowDialog();
        AsyncHttpClient client = new AsyncHttpClient();
        //RequestParams params = new RequestParams();
        //params.put("login",email);
        //params.put("password",password);
        client.get("http://www.lbd.bravioseguros.com.br/userrest/check/"+email+"/"+password,new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject dataJson= (JSONObject) response.get("data");
                    String id = (String) dataJson.get("idUsuario");
                    if(!id.equals("false")) {
                        DismissDialog();
                        String login = (String) dataJson.get("email");
                        session.settype((String) response.get("type"));
                        session.setid(id);
                        session.setusename(login);
                        TransitionRight(MainActivity.class);
                    } else {
                        DismissDialog();
                        WriteMessage("Login or Password Incorrect!","long");
                        _loginButton.setEnabled(true);
                        _resetButton.setEnabled(true);
                        _signupButton.setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    DismissDialog();
                    WriteMessage("Login or Password Incorrect!","long");
                    _loginButton.setEnabled(true);
                    _resetButton.setEnabled(true);
                    _signupButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DismissDialog();
                WriteMessage("No Internet Connection!","long");
            }

            @Override
            public void onFinish() {

            }

        });

    }
}
