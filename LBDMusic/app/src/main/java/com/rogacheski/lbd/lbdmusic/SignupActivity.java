package com.rogacheski.lbd.lbdmusic;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rogacheski.lbd.lbdmusic.base.baseActivity;
import com.rogacheski.lbd.lbdmusic.session.Session;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SignupActivity extends baseActivity {

    @Bind(R.id.signup_name) EditText _nameText;
    @Bind(R.id.signup_email) EditText _emailText;
    @Bind(R.id.signup_password) EditText _passwordText;
    @Bind(R.id.signup_type) Spinner _typeText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.btn_signup) Button _signupButton;

    String userType[] = {"Musician", "Contractor"};
    ArrayAdapter<String> adapterUserType;
    String typeChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TAG = "SignupActivity";
        mContext = SignupActivity.this;
        session = new Session(mContext);

        if(CheckUserLogado()) {
            TransitionLeft(MainActivity.class);
        }

        ButterKnife.bind(this);

        typeChoose = "Musician";

        adapterUserType = new ArrayAdapter<String>(this, R.layout.spinner_item,userType);
        adapterUserType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _typeText.setAdapter(adapterUserType);
        _typeText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeChoose = parent.getItemAtPosition(position).toString();
                WriteLog(typeChoose);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionLeft(LoginActivity.class);
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ActivityManager.TaskDescription tDesk = new ActivityManager.TaskDescription(getString(R.string.app_name),bm,getResources().getColor(R.color.colorPrimaryDark));
        this.setTaskDescription(tDesk);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public void signup() {
        WriteLog("Iniciando Signup");

        _loginButton.setEnabled(false);
        _signupButton.setEnabled(false);

        final String name = _nameText.getText().toString().trim();
        final String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        Boolean valid = true;

        if (name.isEmpty() || name.length() < 3 ) {
            _nameText.setError("enter a name with more than 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

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
            _signupButton.setEnabled(true);
            WriteLog("Signup Error Validate");
            return;
        }

        ShowDialog();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email",email);
        params.put("password",password);
        params.put("fantasyname",name);
        params.put("type",typeChoose);
        client.post("http://www.lbd.bravioseguros.com.br/userrest",params,new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject dataJson= (JSONObject) response.get("data");
                    String id = dataJson.get("idUsuario").toString();
                    String type = dataJson.get("type").toString();
                    if(!id.equals("false")) {
                        if(!id.equals("exist")) {
                            DismissDialog();
                            //String login = (String) dataJson.get("email");
                            String login = email;
                            session.setusename(login);
                            session.settype(type);
                            TransitionRight(MainActivity.class);
                        } else {
                            DismissDialog();
                            WriteMessage("Email already resgitered!","long");
                            _loginButton.setEnabled(true);
                            _signupButton.setEnabled(true);
                        }
                    } else {
                        DismissDialog();
                        WriteMessage("Error!","long");
                        _loginButton.setEnabled(true);
                        _signupButton.setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    DismissDialog();
                    WriteMessage("Error!","long");
                    _loginButton.setEnabled(true);
                    _signupButton.setEnabled(true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DismissDialog();
                WriteMessage("No Internet Connection!","long");
                _loginButton.setEnabled(true);
                _signupButton.setEnabled(true);
            }

            @Override
            public void onFinish() {

            }

        });


    }

}

