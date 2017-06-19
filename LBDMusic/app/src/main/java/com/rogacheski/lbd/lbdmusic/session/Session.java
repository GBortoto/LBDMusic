package com.rogacheski.lbd.lbdmusic.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).apply();
        //prefsCommit();
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }

    public void setid(String id) {
        prefs.edit().putString("id", id).apply();
        //prefsCommit();
    }

    public String getid() {
        String id = prefs.getString("id","");
        return id;
    }

    public void settype(String type) {
        prefs.edit().putString("type", type).apply();
        //prefsCommit();
    }

    public String gettype() {
        String type = prefs.getString("type","");
        return type;
    }
}
