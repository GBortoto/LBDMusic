package com.rogacheski.lbd.lbdmusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

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


public class EditfieldActivity extends baseActivity {

    @Bind(R.id.btn_ok) View _buttonok;
    @Bind(R.id.btn_cancel) View _buttoncancel;
    @Bind(R.id.input_editfield) EditText _inputField;
    @Bind(R.id.input_oldpasswordfield) EditText _inputOldPassword;

    private ActionBar actionBar;

    private String fieldType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfield);

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(false);

        TAG = "EditFieldActivity";
        mContext = EditfieldActivity.this;
        session = new Session(mContext);

        ButterKnife.bind(this);

        fieldType = getIntent().getExtras().getString("fieldType");
        WriteLog(fieldType);
        _inputOldPassword.setVisibility(View.INVISIBLE);
        _inputField.setHint("");


        if(fieldType.equals("name")) {
            actionBar.setTitle("Enter your name");
            _inputField.setInputType(InputType.TYPE_CLASS_TEXT);
            _inputField.setText(getIntent().getExtras().getString("fieldText"));
        } else {
            if(fieldType.equals("fantasyname")) {
                actionBar.setTitle("Enter your Fantasy Name");
                _inputField.setInputType(InputType.TYPE_CLASS_TEXT);
                _inputField.setText(getIntent().getExtras().getString("fieldText"));
            } else {
                if(fieldType.equals("email")) {
                    actionBar.setTitle("Enter your email");
                    _inputOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    _inputField.setHint("Old Password");
                    _inputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    _inputOldPassword.setVisibility(View.VISIBLE);
                    _inputOldPassword.setText(getIntent().getExtras().getString("fieldText"));
                } else {
                    if(fieldType.equals("document")) {
                        actionBar.setTitle("Enter your document");
                        _inputField.setInputType(InputType.TYPE_CLASS_TEXT);
                        _inputField.setText(getIntent().getExtras().getString("fieldText"));
                    } else {
                        actionBar.setTitle("Enter your password");
                        _inputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        _inputField.setHint("Old Password");
                        _inputOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        _inputOldPassword.setVisibility(View.VISIBLE);
                        _inputOldPassword.setHint("New Password");
                    }
                }
            }
        }

        TAG = "EditfieldActivity";
        mContext = EditfieldActivity.this;

        if(!CheckUserLogado()) {
            TransitionRight(LoginActivity.class);
        }

        _buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionLeft(ProfileContractorActivity.class);
            }
        });

        _buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveField();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void saveField() {
        WriteLog("Iniciando saveField");

        _buttoncancel.setEnabled(false);
        _buttonok.setEnabled(false);

        final String fieldText = _inputField.getText().toString().trim();
        final String oldPassword = _inputOldPassword.getText().toString().trim();
        Boolean valid = true;


        if(fieldType.equals("name")) {
            if (fieldText.isEmpty() || fieldText.length() < 3 ) {
                _inputField.setError("enter a name with more than 3 characters");
                valid = false;
            } else {
                _inputField.setError(null);
            }
        } else {
            if(fieldType.equals("fantasyname")) {
                if (fieldText.isEmpty() || fieldText.length() < 3 ) {
                    _inputField.setError("enter a name with more than 3 characters");
                    valid = false;
                } else {
                    _inputField.setError(null);
                }
            } else {
                if(fieldType.equals("email")) {
                    if (oldPassword.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(oldPassword).matches()) {
                        _inputOldPassword.setError("enter a valid email address");
                        valid = false;
                    } else {
                        _inputOldPassword.setError(null);
                    }
                    if (fieldText.isEmpty() || oldPassword.length() < 6 || fieldText.length() > 10) {
                        _inputField.setError("between 6 and 10 alphanumeric characters");
                        valid = false;
                    } else {
                        _inputField.setError(null);
                    }
                } else {
                    if(fieldType.equals("document")) {
                        if (fieldText.isEmpty()) {
                            _inputField.setError("enter a valid document");
                            valid = false;
                        } else {
                            _inputField.setError(null);
                        }
                    } else {
                        if (fieldText.isEmpty() || fieldText.length() < 6) {
                            _inputField.setError("enter a password with more than 6 characters");
                            valid = false;
                        } else {
                            _inputField.setError(null);
                        }
                        if (oldPassword.isEmpty() || oldPassword.length() < 6) {
                            _inputOldPassword.setError("enter a password with more than 6 characters");
                            valid = false;
                        } else {
                            _inputOldPassword.setError(null);
                        }
                    }
                }
            }
        }

        if(!valid) {
            _buttoncancel.setEnabled(true);
            _buttonok.setEnabled(true);
            WriteLog("Field Error Validate");
            return;
        }

        ShowDialog();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("type",fieldType);
        if(fieldType.equals("password") || fieldType.equals("email")) {
            params.put("text", oldPassword);
            params.put("confirm", fieldText);
        } else {
            params.put("text", fieldText);
            params.put("confirm", "");
        }
        client.put("http://www.lbd.bravioseguros.com.br/userrest/"+session.getid(),params,new JsonHttpResponseHandler() {

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
                        _buttoncancel.setEnabled(true);
                        _buttonok.setEnabled(true);
                        if(fieldType.equals("email")) {
                            session.setusename(oldPassword);
                        }
                        WriteMessage("Profile Updated!","long");
                        TransitionLeft(ProfileContractorActivity.class);
                    } else {
                        DismissDialog();
                        WriteMessage("Error!","long");
                        _buttoncancel.setEnabled(true);
                        _buttonok.setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    DismissDialog();
                    WriteMessage("Error!","long");
                    _buttoncancel.setEnabled(true);
                    _buttonok.setEnabled(true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DismissDialog();
                WriteMessage("No Internet Connection!","long");
                _buttoncancel.setEnabled(true);
                _buttonok.setEnabled(true);
            }

            @Override
            public void onFinish() {

            }

        });



    }


}




