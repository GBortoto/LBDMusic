package com.rogacheski.lbd.lbdmusic;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.rogacheski.lbd.lbdmusic.entity.BandEntity;
import com.rogacheski.lbd.lbdmusic.model.band;
import com.rogacheski.lbd.lbdmusic.model.user;

import java.util.ArrayList;
import java.util.Arrays;

public class searchResult extends AppCompatActivity {

    //private RelativeLayout rl = (RelativeLayout) findViewById(R.layout.activity_search_result);

    private String nomes[];
    private BandEntity resultados[];
    //private user userLogado;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        resultados = (BandEntity[]) intent.getSerializableExtra("com.rogacheski.lbd.lbdmusic.MESSAGE");
        //userLogado = (user) intent.getSerializableExtra("com.rogacheski.lbd.lbdmusic.USER");


        //nomes = new String [100];
        String nomes[] = new String [resultados.length];

        for(int i=0; i<resultados.length; i++){
            String name = resultados[i].getsNomeBanda();
            nomes[i] = name;
        }

        final ListView resultList = (ListView) findViewById(R.id.searchResultList);

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultList.getChildAt(position).setBackgroundColor(Color.CYAN);
                openProfile(resultados[position]);
                //resultList.getChildAt(position).setBackgroundColor(Color.WHITE);
            }
        });

        arrayList = new ArrayList<>(Arrays.asList(nomes));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textItem, arrayList);
        resultList.setAdapter(adapter);
    }

    private void openProfile(BandEntity banda){
        banda.setsDescricaoBanda("teste");
        //banda.setDescription("teste");
        /** TODO - Chamar o perfil do músico a partir das informações em "banda" */
    }
}
