package com.flavio.plotdash.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.flavio.dao.DaoCapitulo;
import com.flavio.dao.DaoGenero;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.Comentario;
import com.flavio.plotdash.model.Genero;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.adapter.AdapterComentarioList;
import com.flavio.plotdash.ui.adapter.AdapterVistaHistoria;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HistoriaActivity extends AppCompatActivity {


    RecyclerView rvHistoria, rvHistoria2;
    AdapterVistaHistoria elementAdapter;
    ListView listaComentario;
    LinearLayout layoutPB;
    List<Object> itemList ;
    Historia historia;

    Button btnleer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutPB=findViewById(R.id.layoutPB);
        rvHistoria=findViewById(R.id.rv_historia);
        rvHistoria.setLayoutManager(new LinearLayoutManager(this));
        listaComentario=findViewById(R.id.listaComentario);
//        rvHistoria2=findViewById(R.id.rv_historia2);
//        rvHistoria2.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        btnleer=findViewById(R.id.btnleer);
        Bundle parametros=this.getIntent().getExtras();
        historia= (Historia) parametros.get("idHistoria");
      itemList.add(historia);
        elementAdapter = new AdapterVistaHistoria(itemList,getBaseContext());
        rvHistoria.setAdapter(elementAdapter);
//        rvHistoria2.setAdapter(elementAdapter);
        elementAdapter.notifyDataSetChanged();
        layoutPB.setVisibility(View.GONE);
       //obtenerObj("capitulos",(historia.getIdHistoria()));

        btnleer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getBaseContext(), LeerHistoriaActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idHistoria", historia);
                startActivity(intent);
            }
        });
        llenarComentarios();



   }

    private void llenarComentarios() {
        if (historia.getComentarios() != null){
            AdapterComentarioList adapter = new AdapterComentarioList(getBaseContext(), R.layout.adapter_comentario_list, historia.getComentarios());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            listaComentario.setAdapter(adapter);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void obtenerObj(String tipo, int id){

        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        parametros.put("opcion", "buscarPor");
        parametros.put("idHistoria", id+"");
        String url= DaoCapitulo.URL;



        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                layoutPB.setVisibility(View.VISIBLE);
            }

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    ArrayList<Capitulo> caps=DaoCapitulo.obtenerList(new String(responseBody));
                    for (Capitulo c:caps) {
                        itemList.add(c);
                    }
                    elementAdapter = new AdapterVistaHistoria(itemList,getBaseContext());
                    rvHistoria.setAdapter(elementAdapter);
//                    rvHistoria2.setAdapter(elementAdapter);
                    elementAdapter.notifyDataSetChanged();
                    layoutPB.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }
}