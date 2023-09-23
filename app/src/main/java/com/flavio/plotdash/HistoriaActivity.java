package com.flavio.plotdash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.flavio.dao.DaoCapitulo;
import com.flavio.dao.DaoCapitulo;
import com.flavio.dao.DaoHistoria;
import com.flavio.dao.DaoHistoria;
import com.flavio.model.Capitulo;
import com.flavio.model.Historia;
import com.flavio.model.Historia;
import com.flavio.util.ComboHistoriaAdapter;
import com.flavio.util.ElementAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class HistoriaActivity extends AppCompatActivity {


    RecyclerView rvHistoria;
    ElementAdapter elementAdapter;
    LinearLayout layoutPB;
    List<Object> itemList ;
    Historia historia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutPB=findViewById(R.id.layoutPB);
        rvHistoria=findViewById(R.id.rv_historia);
        rvHistoria.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        Bundle parametros=this.getIntent().getExtras();
        historia= (Historia) parametros.get("idHistoria");
        itemList.add(historia);
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
                    elementAdapter = new ElementAdapter (itemList,getBaseContext());
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