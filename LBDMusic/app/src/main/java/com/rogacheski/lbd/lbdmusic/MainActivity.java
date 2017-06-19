package com.rogacheski.lbd.lbdmusic;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rogacheski.lbd.lbdmusic.base.baseActivity;
import com.rogacheski.lbd.lbdmusic.entity.BandEntity;
import com.rogacheski.lbd.lbdmusic.session.Session;
import com.rogacheski.lbd.lbdmusic.model.user;
import com.rogacheski.lbd.lbdmusic.model.band;
import com.rogacheski.lbd.lbdmusic.singleton.PicassoSingleton;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends baseActivity
        implements NavigationView.OnNavigationItemSelectedListener , PicassoSingleton.PicassoCallbacksInterface {

    private TextView mUsernameEdit;
    private ImageView mUserPicture;

    /** Texto superior - "Pesquisar por:"*/
    private TextView searchMessage;

    /** ImageView -> Ícone de pesquisa*/
    private ImageView seach_button_icon;

    /** searchBar -> Barra de pesquisa*/
    private EditText searchBar;

    /** Número de opções de pesquisa*/
    private int numberOfOptions = 6;

    /** Opções de pesquisa*/
    private ImageView option1;
    private ImageView option2;
    private ImageView option3;
    private ImageView option4;
    private ImageView option5;
    private ImageView option6;

    /** Os ícones estão inicializados?*/
    private boolean inicializado = false;

    /** Lista de ícones de pesquisa*/
    private ImageView options[] = {option1, option2, option3, option4, option5, option6};

    /** Lista de imagens para o estado OFF desses icones*/
    int imagesOff[] = {R.drawable.option1off, R.drawable.option2off, R.drawable.option3off, R.drawable.option4off, R.drawable.option5off, R.drawable.option6};

    /** Lista de imagens para o estado ON desses icones*/
    int imagesOn[] = {R.drawable.option1on, R.drawable.option2on, R.drawable.option3on, R.drawable.option4on, R.drawable.option5on, R.drawable.option6};

    /** Estados dos ícones (Pressionado/Solto)*/
    private boolean optionsState[] = {false, false, false, false, false};

    public user userLogado = new user();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        TAG = "MainActivity";
        mContext = MainActivity.this;
        session = new Session(mContext);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        mUsernameEdit = (TextView)header.findViewById(R.id.username);
        mUserPicture = (ImageView)header.findViewById(R.id.drawer_profilePicture);

        if(!CheckUserLogado()) {
            TransitionRight(LoginActivity.class);
        } else {
            if(!session.gettype().equals("contractor")) {
                TransitionRight(ProfileMusicianActivity.class);
            } else {
                loadContratante();
            }
        }


        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        ActivityManager.TaskDescription tDesk = new ActivityManager.TaskDescription(getString(R.string.app_name),bm,getResources().getColor(R.color.colorPrimaryDark));
        this.setTaskDescription(tDesk);
        getWindow().setBackgroundDrawableResource(R.color.white);
    }

    /** A caixa de busca existe? Se sim, o foco está nela?*/
    public boolean checkFocus(int focus, EditText searchBar) {
        if(searchBar != null) {
            if (focus == searchBar.getId()) {
                return true;
            }
                return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            if(userLogado.getType() == "Musician") {

            } else {
                TransitionRight(ProfileContractorActivity.class);
            }
        } else if (id == R.id.nav_logout) {
            session.setusename("");
            TransitionLeft(LoginActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadContratante() {
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
                    WriteLog(type);
                    if(!id.equals("false")) {
                        DismissDialog();
                        userLogado.setEmail(email);
                        userLogado.setFantasyName(dataJson.get("fantasyName").toString());
                        userLogado.setFName(dataJson.get("firstName").toString());
                        userLogado.setLName(dataJson.get("lastName").toString());
                        userLogado.setPicture(dataJson.get("profileImage").toString());
                        userLogado.setType(type);
                        session.settype(type);
                        if(type.equals("musician")) {
                            userLogado.setBackpicture(dataJson.get("backpicture").toString());
                        } else {
                            userLogado.setDocument(dataJson.get("identDocument").toString());
                        }
                        mUsernameEdit.setText(userLogado.getFantasyName());
                        PicassoSingleton.getInstance(new WeakReference<>(mContext), new WeakReference<PicassoSingleton.PicassoCallbacksInterface>(MainActivity.this))
                                .setProfilePictureAsync(mUserPicture, userLogado.getPicture(),getDrawable(R.drawable.ic_account_circle_white));
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


                Intent intent = new Intent(MainActivity.this, ContractorActivity.class);
                intent.putExtra("com.rogacheski.lbd.lbdmusic.USER", userLogado);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

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