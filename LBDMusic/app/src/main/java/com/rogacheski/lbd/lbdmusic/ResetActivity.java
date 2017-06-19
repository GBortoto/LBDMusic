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

public class ResetActivity extends baseActivity {

    @Bind(R.id.reset_password_email) EditText _emailText;
    @Bind(R.id.btn_back) Button _backButton;
    @Bind(R.id.btn_reset_password) Button _resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        TAG = "ResetActivity";
        mContext = ResetActivity.this;
        session = new Session(mContext);

        if(CheckUserLogado()) {
            TransitionLeft(MainActivity.class);
        }

        ButterKnife.bind(this);

        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        _resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        ActivityManager.TaskDescription tDesk = new ActivityManager.TaskDescription(getString(R.string.app_name),bm,getResources().getColor(R.color.colorPrimaryDark));
        this.setTaskDescription(tDesk);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public void reset() {
        WriteLog("Iniciando Reset");

        _backButton.setEnabled(false);
        _resetButton.setEnabled(false);

        String email = _emailText.getText().toString().trim();
        Boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if(!valid) {
            _backButton.setEnabled(false);
            _resetButton.setEnabled(true);
            WriteLog("Reset Error Validate");
            return;
        }

        ShowDialog();


    }
}

