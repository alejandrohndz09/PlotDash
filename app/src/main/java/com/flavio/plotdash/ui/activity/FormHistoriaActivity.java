package com.flavio.plotdash.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.flavio.dao.DaoGenero;
import com.flavio.dao.DaoUsuario;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Genero;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.util.Alert;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FormHistoriaActivity extends AppCompatActivity {

    EditText etTitulo, etdescripcion;
    Spinner spGenero;
    ListView lvCaps;
    Button btnAddCap;
    public static String opcion = "registrar";
    Historia historia;
    ArrayList<Genero> generosList;
    LinearLayout layoutPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_historia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(opcion.equals("registrar") ? "Nueva Historia" : "Editar Historia");
        etTitulo=findViewById(R.id.etTitulo);
        etdescripcion=findViewById(R.id.etDescripcion);
        spGenero=findViewById(R.id.spGenero);
        lvCaps=findViewById(R.id.caps);
        layoutPB=findViewById(R.id.layoutPB2);
        btnAddCap=findViewById(R.id.btnAddCap);
        prepararComponentes();
        autoCompletar();

    }

    private void autoCompletar() {
        if (!opcion.equals("registrar")) {
            Bundle parametros = this.getIntent().getExtras();
            historia = (Historia) parametros.get("historia");

            etTitulo.setText(historia.getTitulo());
            etdescripcion.setText(historia.getDescripcion());

            for (int i = 0; i < generosList.size(); i++) {
                if (generosList.get(i).getGenero().equals(historia.getIdGenero().getGenero())) {
                    spGenero.setSelection(i);
                    break;
                }
            }
        } else {
            historia = new Historia();
        }
    }

    private void prepararComponentes() {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        parametros.put("opcion", "buscarTodos");
        client.setTimeout(3000);

        client.post(DaoGenero.URL, parametros, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    if (DaoGenero.obtenerList(new String(responseBody)).size() > 0) {
                        generosList=new ArrayList<>();
                        generosList = DaoGenero.obtenerList(new String(responseBody));
                        generosList.add(0, new Genero(-1, "Seleccione un género"));
                        ArrayAdapter<Genero> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, generosList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spGenero.setAdapter(adapter);
                        layoutPB.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_historia_menu, menu);
        MenuItem op = menu.findItem(R.id.opGuardar);
        op.setTooltipText(opcion.equals("registrar") ? "Guardar" : "Modificar");
        op.setIcon(opcion.equals("registrar") ? R.drawable.baseline_save_24 : R.drawable.baseline_save_as_24);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}