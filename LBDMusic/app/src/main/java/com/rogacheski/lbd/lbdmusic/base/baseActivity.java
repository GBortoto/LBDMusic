package com.rogacheski.lbd.lbdmusic.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.rogacheski.lbd.lbdmusic.R;
import com.rogacheski.lbd.lbdmusic.session.Session;

/**
 * Created by aplicacoes on 6/12/17.
 */

public abstract class baseActivity extends AppCompatActivity {

    protected static Context mContext;
    protected Session session;
    protected static String TAG;
    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Iniatilizing Globals

    }

    public boolean CheckUserLogado() {
        if(session.getusename() != "") {
            return true;
        } else {
            return false;
        }
    }

    public void ShowDialog() {
        progressDialog = new ProgressDialog(mContext,R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public void ShowCustomDialog(String text) {
        progressDialog = new ProgressDialog(mContext,R.style.AppTheme_Dark_Dialog);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(text);
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public void DismissDialog() {
        progressDialog.dismiss();
    }

    public void WriteMessage(String message,String length) {
        if(length == "long") {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void WriteLog(String log) {
        Log.w(TAG,log);
    }

    public void TransitionLeft(Class nextActivity) {
        Intent intent = new Intent(mContext,nextActivity);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public void TransitionRight(Class nextActivity) {
        Intent intent = new Intent(mContext,nextActivity);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void TransitionRightExtraField(Class nextActivity, String extraName, String extraValue, String extraVName, String extraVValue) {
        Intent intent = new Intent(mContext,nextActivity);
        intent.putExtra(extraName,extraValue);
        intent.putExtra(extraVName,extraVValue);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

}
