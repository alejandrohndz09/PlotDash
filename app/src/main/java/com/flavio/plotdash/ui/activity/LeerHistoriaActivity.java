package com.flavio.plotdash.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.flavio.dao.DaoCapitulo;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.adapter.AdapterVistaHistoria;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LeerHistoriaActivity extends AppCompatActivity {


    RecyclerView rvHistoria;
    AdapterVistaHistoria elementAdapter;
    LinearLayout layoutPB;
    List<Object> itemList ;
    Historia historia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_leer_historia);
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutPB=findViewById(R.id.layoutPB1);
        rvHistoria=findViewById(R.id.rv_historia1);
        rvHistoria.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        Bundle parametros=this.getIntent().getExtras();historia= (Historia) parametros.get("idHistoria");
//        itemList.add(historia);
        setTitle(historia.getTitulo());
        obtenerObj("capitulos",(historia.getIdHistoria()));
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