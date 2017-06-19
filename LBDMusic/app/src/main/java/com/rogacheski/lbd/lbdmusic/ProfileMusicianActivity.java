package com.rogacheski.lbd.lbdmusic;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.rogacheski.lbd.lbdmusic.adapter.TabAdapterBandaView;
import com.rogacheski.lbd.lbdmusic.base.baseActivity;
import com.rogacheski.lbd.lbdmusic.controllers.BandController;
import com.rogacheski.lbd.lbdmusic.entity.BandEntity;
import com.rogacheski.lbd.lbdmusic.entity.TagEntity;
import com.rogacheski.lbd.lbdmusic.session.Session;
import com.rogacheski.lbd.lbdmusic.model.user;
import com.rogacheski.lbd.lbdmusic.singleton.PicassoSingleton;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileMusicianActivity extends baseActivity
        implements NavigationView.OnNavigationItemSelectedListener , PicassoSingleton.PicassoCallbacksInterface {

    private TextView mUsernameEdit;
    private ImageView mUserPicture;

    public user userLogado = new user();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilemusician);

        ButterKnife.bind(this);

        TAG = "ProfileMusicianActivity";
        mContext = ProfileMusicianActivity.this;
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
            if(!session.gettype().equals("musician")) {
                TransitionRight(MainActivity.class);
            } else {
                loadUser();
            }
        }


        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        ActivityManager.TaskDescription tDesk = new ActivityManager.TaskDescription(getString(R.string.app_name),bm,getResources().getColor(R.color.colorPrimaryDark));
        this.setTaskDescription(tDesk);
        getWindow().setBackgroundDrawableResource(R.color.white);

        jbInit();
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

        if (id == R.id.nav_logout) {
            session.setusename("");
            TransitionLeft(LoginActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                        PicassoSingleton.getInstance(new WeakReference<>(mContext), new WeakReference<PicassoSingleton.PicassoCallbacksInterface>(ProfileMusicianActivity.this))
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

    private void jbInit() {
        //esconde barra do app
        //ActionBar actionBar = getSupportActionBar();
        //if(actionBar != null) {
        //    actionBar.hide();
        //}


        int id_banda = 0; //TODO ainda não sei como receber o id como parâmetro

        //seta as tags particulares da banda x
        Random r = new Random();

        id_banda = r.nextInt(2);
        BandEntity banda = BandController.buscarBandaPorId(id_banda, this);

        //setBandaImage(banda);
        setNomeBanda(banda.getsNomeBanda());
        DisplayTags(banda.getTags());
        //setando a nota média na tela
        RatingBar nota= (RatingBar)findViewById(R.id.ratingStars);
        nota.setRating((float)banda.getAverageRating());

        //cria gerenciador dos fragmentos (views do tab layout)
        ViewPager viewPager = (ViewPager) findViewById(R.id.Pager);
        PagerAdapter pagerAdapter = new TabAdapterBandaView(getSupportFragmentManager() , this , banda);
        viewPager.setAdapter(pagerAdapter);

        TabLayout barraViewsBanda = (TabLayout)findViewById(R.id.LayoutTabBandaHomePage);

        barraViewsBanda.setupWithViewPager(viewPager);
    }

    private List<TextView> DisplayTags(List<TagEntity> listaTags) {
        ArrayList<TextView> listaTextosTag = new ArrayList<TextView>();
        ImageView backgroundImageBanda = (ImageView) findViewById(R.id.imageViewBanda);
        LinearLayout layoutLinearTags = (LinearLayout) findViewById(R.id.lLTags);
        listaTextosTag = new ArrayList<TextView>();

        //Posicionando as tags

        int iterador =  listaTags.size();
        if(listaTags.size() > 4){
            iterador = 4;
        }

        for (int i = 0; i < iterador; i++) {
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textViewParams.setMargins(50,0,0,30);
            TextView tag = new TextView(this);
            tag.setText(listaTags.get(i).getGenre());
            tag.setTextSize(15);
            tag.setId(i);
            tag.setTextColor(getResources().getColor(R.color.white));
            layoutLinearTags.addView(tag, textViewParams);
            listaTextosTag.add(tag);
        }
        return listaTextosTag;
    }
    public void setNomeBanda(String nomeBanda){
        TextView telaNomeBanda = (TextView)findViewById(R.id.tvTituloDaBanda);
        telaNomeBanda.setText(nomeBanda);
    }
    public void setBandaImage(BandEntity banda) {
        //seta imagem de fundo da banda
        ImageView backgroundImageBanda = (ImageView) findViewById(R.id.imageViewBanda);
        //backgroundImageBanda.setImageDrawable(banda.getdImagemBanda());
        backgroundImageBanda.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //backgroundImageBanda.setImageAlpha(200);
        backgroundImageBanda.setColorFilter(R.color.green_500);
    }
}
