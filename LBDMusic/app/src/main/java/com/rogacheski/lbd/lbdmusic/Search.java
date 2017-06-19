package com.rogacheski.lbd.lbdmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.rogacheski.lbd.lbdmusic.base.baseActivity;
import com.rogacheski.lbd.lbdmusic.entity.BandEntity;
import com.rogacheski.lbd.lbdmusic.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Search extends baseActivity{

    private EditText searchBar;
    private TextView searchMessage;
    private ArrayList<View> elementosVisiveis;
    private int numberOfOptions;
    private boolean iconesInicializados = false;
    private ImageView options[];
    private int imagesOff[];
    private int imagesOn[];
    private boolean optionsState[];
    //private user userLogado;
    private RelativeLayout rlIcones;
    private RelativeLayout rlBarra;
    private int optionH;
    private Context context;
    private ImageView searchButtonHome;

    public Search(EditText searchBar, ArrayList<View> elementosVisiveis, ImageView options[], int imagesOff[], int imagesOn[],
                  boolean optionsState[], RelativeLayout rlIcones, RelativeLayout rlBarra, int optionH,
                  TextView searchMessage, Context context, ImageView searchButtonHome){
        this.searchBar = searchBar;
        this.elementosVisiveis = elementosVisiveis;
        //this.numberOfOptions = numberOfOptions;
        this.options = options;
        this.numberOfOptions = options.length - 1;
        this.imagesOff = imagesOff;
        this.imagesOn = imagesOn;
        this.optionsState = optionsState;
        //this.userLogado = userLogado;
        this.rlIcones = rlIcones;
        this.rlBarra = rlBarra;
        this.optionH = optionH;
        this.searchMessage = searchMessage;
        this.context = context;
        this.searchButtonHome = searchButtonHome;
        searchButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPesquisaSimples();
            }
        });
    }


    public void changeState(View view){
        ImageView atual = (ImageView) view;
        int option = (int) atual.getTag();
        if(!optionsState[option]) {
            setOn(option);
        }
    }

    public void setOn(int option){
        for(int i=0; i<numberOfOptions-1; i++){
            if(i == option){
                optionsState[i] = true;
                options[i].setImageResource(imagesOn[i]);
            } else{
                optionsState[i] = false;
                options[i].setImageResource(imagesOff[i]);
            }
        }
    }

    public int getSettings(){
        for(int i=0; i<numberOfOptions-1; i++){
            if(optionsState[i]){
                return i;
            }
        }
        return -1;
    }


    public void abrirPesquisaSimples() {


        /** PARTE 1 - Esconder os ícones atuais na tela*/

        for(int i=0; i<elementosVisiveis.size(); i++){
            try{
                elementosVisiveis.get(i).setVisibility(View.INVISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        /** PARTE 2 - Criar/Mostrar o campo de pesquisa */

        /** Caso a barra não exista, é necessario cria-la*/
        if(searchBar == null){

            // Criar o parâmetro "Match_parent" para a largura
            RelativeLayout.LayoutParams mRparams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mRparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            // Margens
            mRparams.bottomMargin = 5;
            mRparams.leftMargin = 10;
            mRparams.rightMargin = 10;

            // Criar caixa de texto
            searchBar = new EditText(context);

            // Utilizar parâmetros já criados
            searchBar.setLayoutParams(mRparams);

            // Tamanho máximo da altura da entrada
            searchBar.setMaxHeight(100);

            // Id do objeto
            searchBar.setId(R.id.searchBar);

            // Mensagem padrão (dica)
            searchBar.setHint("Procurar");

            // Cor da mensagem padrão
            searchBar.setHintTextColor(Color.parseColor("#999999"));

            // Cor do texto inserido
            searchBar.setTextColor(Color.parseColor("#ffffff"));

            // Limitar a somente uma linha
            searchBar.setSingleLine();

            // Modificar o botão de ação para "Search" (Lupa)
            searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

            // Criar método que chama outro método quando o usuário clicar no botão pesquisar
            searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        simpleSearch(getSettings());
                        handled = true;
                    }
                    return handled;
                }
            });

            // Adicionar o objeto ao layout
            rlBarra.addView(searchBar);

            updateSearchBar(searchBar);

        } else {
            /** Caso a barra já exista, não é necessario cria-la, apenas mostra-la*/
            searchBar.setVisibility(View.VISIBLE);
        }

        /** PARTE 3 - Criar/Mostrar opções de pesquisa */

        // Se uma opção não existe, nenhuma opção existe
        if(!iconesInicializados) {
            for (int i = 0; i < numberOfOptions; i++) {
                options[i] = new ImageView(context);
                options[i].setImageResource(imagesOff[i]);
                //int optionW = (int) (getResources().getDimension(R.dimen.OptionW) / getResources().getDisplayMetrics().density);
                //int optionH = (int) (getResources().getDimension(R.dimen.OptionH));
                //options[i].setMaxWidth(optionW);
                //options[i].setMaxHeight(optionH);
                options[i].setTag(i);

                // Cálculo do tamanho da margem
                //int margin = (rl.getWidth() - (R.dimen.OptionW * 3)) / 4;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                int leftMargin = 30;
                int margin = 30;
                lp.leftMargin = leftMargin + (margin * (i % 3) + (200 * (i%3)));

                // Margem superior fixa
                int margemSuperior = 150;

                // Se a opção estiver na primeira linha
                if (i < 3) {
                    lp.topMargin = margemSuperior;
                } else {
                    // Se a opção estiver na segunda linha
                    lp.topMargin = margemSuperior + optionH + margin;
                }

                // Add Listener

                ImageView.OnClickListener listener;
                if(i < 6){
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeState(v);
                        }
                    };
                }else {
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callAdvancedSearch();
                        }
                    };
                }

                options[i].setOnClickListener(listener);

                rlIcones.addView(options[i], lp);
            }
            setOn(0);
            iconesInicializados = true;
        } else {
            for(int i=0; i<numberOfOptions; i++){
                options[i].setVisibility(View.VISIBLE);
            }
        }

        /** PARTE 5 - Criar o texto superior*/
        if(searchMessage == null) {
            searchMessage = new TextView(context);
            String mensagem = "Procurar por:";
            searchMessage.setText(mensagem);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.leftMargin = 150;
            lp.topMargin = 20;
            searchMessage.setTextSize(30);
            searchMessage.setTextColor(Color.parseColor("#000000"));
            rlIcones.addView(searchMessage, lp);
        } else {
            searchMessage.setVisibility(View.VISIBLE);
        }
    }

    public void fecharPesquisaSimples(EditText searchBar){
        // Turn the serach box invisible
        searchBar.setVisibility(View.INVISIBLE);

        // Clear the contents of the search box
        searchBar.getText().clear();

        // Turn the options invisible
        for(int i=0; i<numberOfOptions; i++){
            options[i].setVisibility(View.INVISIBLE);
        }

        searchMessage.setVisibility(View.INVISIBLE);


        for(int i=0; i<elementosVisiveis.size(); i++){
            try{
                elementosVisiveis.get(i).setVisibility(View.VISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void simpleSearch(int option) {

        String pesquisa = searchBar.getText().toString();

        if(pesquisa.equals("")){
            return;
        }

        ShowDialog();

        String requestTerm = "";
        String failText = "";

        switch (option) {

            // Caso nenhuma opção esteja precionada (o que não devia ocorrer)
            case -1: return;

            // NOME
            case 0:  requestTerm = "/musicianrest/search/";
                failText = "Não encontramos nenhum músico ou banda com o nome \"" + pesquisa + "\"";
                break;

            // GÊNERO
            case 1:  requestTerm = "/musicianrest/search/"; //TODO
                failText = "Não encontramos nenhum gênero com o nome \"" + pesquisa + "\"";
                break;

            // LOCAL
            case 2:  requestTerm = "/musicianrest/search/"; //TODO
                failText = "Não encontramos nenhum local com o nome \"" + pesquisa + "\"";
                break;

            // MAIOR PREÇO
            case 3:  requestTerm = "/musicianrest/search/"; //TODO
                failText = "Não encontramos nenhum músico ou banda com o nome \"" + pesquisa + "\"";
                break;

            // MENOR PREÇO
            case 4:  requestTerm = "/musicianrest/search/"; //TODO
                failText = "Não encontramos nenhum músico ou banda com o nome \"" + pesquisa + "\"";
                break;
        }

        final String failTextFinal = failText;
        //Log.d("Info", "Pesquisa: " + pesquisa);
        //Log.d("Info", "option: " + String.valueOf(option));

        DismissDialog();
        WriteLog("Iniciando pesquisaSimples");

        ShowCustomDialog("Searching");
        AsyncHttpClient searchRequest = new AsyncHttpClient();
        searchRequest.get("http://www.lbd.bravioseguros.com.br" + requestTerm + pesquisa, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    // Ler do conteúdo "data" da resposta
                    JSONArray dataJson = (JSONArray) response.get("data");


                    if(dataJson.length() == 0) {
                        DismissDialog();
                        WriteMessage(failTextFinal, "long");
                        // TODO
                        //TransitionLeft(MainActivity.class);
                        return;
                    }
                    int numberOfResults = dataJson.length();

                    BandEntity resultados[] = new BandEntity[numberOfResults];
                    //band resultados[] = new band [numberOfResults];

                    // Para cada valor encontrado
                    for(int i=0; i<numberOfResults; i++){
                        resultados[i] = new BandEntity();
                        //resultados[i] = new band();
                        JSONObject atual = dataJson.getJSONObject(i);

                        String idUsuarioString = atual.get("idUsuario").toString();
                        if(!idUsuarioString.equals("") || idUsuarioString.equals("null")){
                            resultados[i].setIdUsuario(Integer.parseInt(idUsuarioString));
                        }
                        String idAddressString = atual.get("idAddress").toString();
                        if(!(idAddressString.equals("") || idAddressString.equals("null"))){
                            resultados[i].setIdAddress(Integer.parseInt(idAddressString));
                        }
                        resultados[i].setsNomeBanda(atual.get("fantasyName").toString());
                        resultados[i].setFname(atual.get("firstName").toString());
                        resultados[i].setLname(atual.get("lastName").toString());
                        resultados[i].setdImagemBanda(atual.get("profileImage").toString());
                        resultados[i].setdImagemDescBanda(atual.get("backpicture").toString());

                    }
                    DismissDialog();
                    Intent intent = new Intent(context, searchResult.class);
                    intent.putExtra("com.rogacheski.lbd.lbdmusic.MESSAGE", resultados);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } catch (JSONException e) {
                    e.printStackTrace();
                    DismissDialog();
                    //TransitionLeft(LoginActivity.class);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                DismissDialog();
                WriteMessage(failTextFinal, "long");
                //TransitionLeft(MainActivity.class);
            }

            @Override
            public void onFinish() {

            }
        });
    }


    public void callAdvancedSearch(){
        Log.d("Info", "AdvancedSearch");
        /** TODO implementar a busca avançada*/
    }

}
