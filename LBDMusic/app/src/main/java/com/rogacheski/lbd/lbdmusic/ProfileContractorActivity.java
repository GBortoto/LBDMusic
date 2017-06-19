package com.rogacheski.lbd.lbdmusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rogacheski.lbd.lbdmusic.base.ImagePicker;
import com.rogacheski.lbd.lbdmusic.base.baseActivity;
import com.rogacheski.lbd.lbdmusic.model.user;
import com.rogacheski.lbd.lbdmusic.session.Session;
import com.rogacheski.lbd.lbdmusic.singleton.PicassoSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class ProfileContractorActivity extends baseActivity implements PicassoSingleton.PicassoCallbacksInterface {


    @Bind(R.id.loadPicture) ImageView _loadPicture;
    @Bind(R.id.profilePicture) ImageView _profilePicture;
    @Bind(R.id.profile_progress) ProgressBar _progressPicture;
    @Bind(R.id.profileFantasyName) View _layoutFantasyName;
    @Bind(R.id.profileName) View _layoutName;
    @Bind(R.id.profileEmail) View _layoutEmail;
    @Bind(R.id.profileDocument) View _layoutDocument;
    @Bind(R.id.profilePassword) View _layoutPassword;

    private static final int PICK_IMAGE_ID = 234;

    private ActionBar actionBar;

    private user userLogado = new user();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilecontractor);

        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        TAG = "ProfileContractorActivity";
        mContext = ProfileContractorActivity.this;
        session = new Session(mContext);
        actionBar.setTitle("Your Profile");

        ButterKnife.bind(this);

        if(!CheckUserLogado()) {
            TransitionRight(LoginActivity.class);
        } else {
            loadUser();
        }


        _loadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });

        _profilePicture.setVisibility(View.INVISIBLE);
        _progressPicture.setVisibility(View.VISIBLE);
        _progressPicture.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white),android.graphics.PorterDuff.Mode.MULTIPLY);

        ImageView _editFantasyName = (ImageView) _layoutFantasyName.findViewById(R.id.fieldButton);
        ImageView _editName = (ImageView) _layoutName.findViewById(R.id.fieldButton);
        ImageView _editEmail = (ImageView) _layoutEmail.findViewById(R.id.fieldButton);
        ImageView _editDocument = (ImageView) _layoutDocument.findViewById(R.id.fieldButton);
        ImageView _editPassword = (ImageView) _layoutPassword.findViewById(R.id.fieldButton);

        _editFantasyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionRightExtraField(EditfieldActivity.class,"fieldType","fantasyname","fieldText",userLogado.getFantasyName());
            }
        });

        _editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userLogado.getFName() != "null") {
                    TransitionRightExtraField(EditfieldActivity.class, "fieldType", "name", "fieldText", userLogado.getFName() + " " + userLogado.getLName());
                } else {
                    TransitionRightExtraField(EditfieldActivity.class, "fieldType", "name", "fieldText", "");
                }
            }
        });

        _editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionRightExtraField(EditfieldActivity.class,"fieldType","email","fieldText",userLogado.getEmail());
            }
        });

        _editDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userLogado.getFName() != "null") {
                    TransitionRightExtraField(EditfieldActivity.class, "fieldType", "document", "fieldText", userLogado.getDocument());
                } else {
                    TransitionRightExtraField(EditfieldActivity.class, "fieldType", "document", "fieldText", "");
                }
            }
        });

        _editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionRightExtraField(EditfieldActivity.class,"fieldType","password","fieldText","123");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            TransitionLeft(MainActivity.class);
        }
        return super.onOptionsItemSelected(item);

    }

    public void onPickImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this,"profile");
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] profileData = baos.toByteArray();

                    WriteLog("Upload Start");
                    _profilePicture.setVisibility(View.INVISIBLE);
                    _progressPicture.setVisibility(View.VISIBLE);
                    _progressPicture.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("picture", bitmap);
                    client.post("http://www.lbd.bravioseguros.com.br/userrest/picture/" + session.getusename(), params, new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // called when response HTTP status is "200 OK"
                            try {
                                JSONObject dataJson = (JSONObject) response.get("data");
                                String id = (String) dataJson.get("idUsuario");
                                if (!id.equals("falso")) {
                                    _profilePicture.setVisibility(View.VISIBLE);
                                    _progressPicture.setVisibility(View.GONE);
                                    WriteMessage("Updated!", "Long");
                                    PicassoSingleton.getInstance(new WeakReference<>(mContext), new WeakReference<PicassoSingleton.PicassoCallbacksInterface>(ProfileContractorActivity.this))
                                            .setProfilePictureAsync(_profilePicture, (String) dataJson.get("picture"), getDrawable(R.drawable.ic_account_circle_white));
                                } else {
                                    WriteMessage("Error!", "Long");
                                    _profilePicture.setVisibility(View.VISIBLE);
                                    _progressPicture.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                WriteMessage("Error!", "Long");
                                _profilePicture.setVisibility(View.VISIBLE);
                                _progressPicture.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            DismissDialog();
                            WriteMessage("No Internet Connection!", "long");
                            _profilePicture.setVisibility(View.VISIBLE);
                            _progressPicture.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFinish() {

                        }

                    });
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    public void loadUser() {
        WriteLog("Iniciando UserLogado");

        ShowDialog();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://www.lbd.bravioseguros.com.br/userrest/email/"+session.getusename(),new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject dataJson= (JSONObject) response.get("data");
                    String id = dataJson.get("idUsuario").toString();
                    String type = (String) response.get("type");
                    String email = (String) response.get("email");
                    if(!id.equals("false")) {
                        DismissDialog();
                        userLogado.setEmail(email);
                        userLogado.setFantasyName(dataJson.get("fantasyName").toString());
                        userLogado.setFName(dataJson.get("firstName").toString());
                        userLogado.setLName(dataJson.get("lastName").toString());
                        userLogado.setPicture(dataJson.get("profileImage").toString());
                        userLogado.setType(type);
                        if(type.equals("musician")) {
                            userLogado.setBackpicture(dataJson.get("backpicture").toString());
                        } else {
                            userLogado.setDocument(dataJson.get("identDocument").toString());
                        }
                        if(userLogado.getPicture() == null) {
                            _profilePicture.setImageDrawable(getDrawable(R.drawable.ic_account_circle_white));
                            _progressPicture.setVisibility(View.GONE);
                            _profilePicture.setVisibility(View.VISIBLE);
                        } else {
                            PicassoSingleton.getInstance(new WeakReference<>(mContext), new WeakReference<PicassoSingleton.PicassoCallbacksInterface>(ProfileContractorActivity.this))
                                    .setProfilePictureAsyncLoading(_profilePicture, userLogado.getPicture(), _progressPicture,getDrawable(R.drawable.ic_account_circle_white));
                            _profilePicture.setVisibility(View.VISIBLE);
                        }
                        if(userLogado.getFantasyName() == null) {
                            TextView mFantasyName = (TextView) _layoutFantasyName.findViewById(R.id.fieldTextView);
                            mFantasyName.setText("Fantasy Name");
                        } else {
                            TextView mFantasyName = (TextView) _layoutFantasyName.findViewById(R.id.fieldTextView);
                            mFantasyName.setText(userLogado.getFantasyName());
                        }
                        if(userLogado.getFName() == "null") {
                            TextView mName = (TextView) _layoutName.findViewById(R.id.fieldTextView);
                            mName.setText("Full Name");
                        } else {
                            TextView mName = (TextView) _layoutName.findViewById(R.id.fieldTextView);
                            mName.setText(userLogado.getFName() + " " + userLogado.getLName());
                        }
                        if(userLogado.getEmail() == null) {
                            TextView mEmail = (TextView) _layoutEmail.findViewById(R.id.fieldTextView);
                            mEmail.setText("Email(Login)");
                        } else {
                            TextView mEmail = (TextView) _layoutEmail.findViewById(R.id.fieldTextView);
                            mEmail.setText(userLogado.getEmail());
                        }
                        if(userLogado.getDocument() == null || userLogado.getDocument() == "null") {
                            TextView mDocument = (TextView) _layoutDocument.findViewById(R.id.fieldTextView);
                            mDocument.setText("Document");
                        } else {
                            TextView mDocument = (TextView) _layoutDocument.findViewById(R.id.fieldTextView);
                            mDocument.setText(userLogado.getDocument());
                        }
                        TextView mPassword = (TextView) _layoutPassword.findViewById(R.id.fieldTextView);
                        mPassword.setText("Password");
                    } else {
                        DismissDialog();
                        session.setusename("");
                        TransitionLeft(LoginActivity.class);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    DismissDialog();
                    session.setusename("");
                    TransitionLeft(LoginActivity.class);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DismissDialog();
                WriteMessage("No Internet Connection!","long");
                session.setusename("");
                TransitionLeft(LoginActivity.class);
            }

            @Override
            public void onFinish() {

            }

        });

    }

    @Override
    public void onPicassoSuccessCallback() {
    }

    @Override
    public void onPicassoErrorCallback() {
        Log.e("TAG", "error");
    }
}




