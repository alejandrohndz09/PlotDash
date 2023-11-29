package com.flavio.plotdash.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.flavio.dao.DaoGenero;
import com.flavio.dao.DaoHistoria;
import com.flavio.dao.DaoUsuario;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.Genero;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.adapter.AdaptadorCapituloList;
import com.flavio.plotdash.ui.adapter.AdaptadorHistoriaList;
import com.flavio.plotdash.ui.util.Alert;
import com.flavio.plotdash.ui.util.CustomScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    CustomScrollView contenedor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_historia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(opcion.equals("registrar") ? "Nueva Historia" : "Editar Historia");
        etTitulo = findViewById(R.id.etTitulo);
        etdescripcion = findViewById(R.id.etDescripcion);
        contenedor = findViewById(R.id.contenedor);
        spGenero = findViewById(R.id.spGenero);
        lvCaps = findViewById(R.id.caps);
        layoutPB = findViewById(R.id.layoutPB2);
        btnAddCap = findViewById(R.id.btnAddCap);
        prepararComponentes();
        autoCompletar();
        llenarCapitulos();
        btnAddCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormCapituloActivity.opcion = "registrar";
                Intent intent = new Intent(getBaseContext(), FormCapituloActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        lvCaps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // El usuario ha tocado el ListView, deshabilita el desplazamiento del ScrollView
                        contenedor.setEnableScrolling(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // El usuario ha levantado el dedo o ha cancelado el toque, vuelve a habilitar el desplazamiento del ScrollView
                        contenedor.setEnableScrolling(true);
                        break;
                }
                return false;
            }
        });


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
            llenarCapitulos();
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
                        generosList = new ArrayList<>();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                // Aquí obtienes el dato enviado desde la Actividad 2
                Bundle parametros = data.getExtras();
                Capitulo c = (Capitulo) parametros.get("capitulo");

                if (historia.getCapitulos() == null) {
                    historia.setCapitulos(new ArrayList<>());
                    c.setNum(1);
                    historia.getCapitulos().add(c);
                } else if (historia.getCapitulos() != null && c.getNum() == 0) {
                    c.setNum(historia.getCapitulos().size() + 1);
                    historia.getCapitulos().add(c);
                } else if (c.getNum() != 0) {
                    for (int i = 0; i < historia.getCapitulos().size(); i++) {
                        Capitulo x = historia.getCapitulos().get(i);
                        if (x.getIdCapitulo() == c.getIdCapitulo()) {
                            // Reemplaza el capítulo existente con el nuevo capítulo
                            historia.getCapitulos().set(i, c);
                            break;  // Puedes salir del bucle ya que has encontrado y reemplazado el capítulo
                        }
                    }
                }
                llenarCapitulos();
            } else if (resultCode == RESULT_CANCELED) {
                // Maneja la cancelación de la Actividad 2 si es necesario
            }
        }
    }

    private void llenarCapitulos() {
        if (historia.getCapitulos() == null) {
            String[] mensaje = {"Sin registros que mostrar"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mensaje);
            lvCaps.setAdapter(adapter);
        } else {
            AdaptadorCapituloList adaptador = new AdaptadorCapituloList(this, R.layout.adapter_historia_list, historia.getCapitulos());
            lvCaps.setAdapter(adaptador);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.opGuardar) {
            if (etTitulo.getText().toString().isEmpty() && etdescripcion.getText().toString().isEmpty()) {
                etTitulo.setError("");
                etdescripcion.setError("");
                Alert.show(getBaseContext(), "error", "Campos vacíos", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
            } else if (etTitulo.getText().toString().isEmpty()) {
                etTitulo.setError("");
                Alert.show(getBaseContext(), "error", "No ha ingresado un título", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
            } else if (etdescripcion.getText().toString().isEmpty()) {
                etdescripcion.setError("");
                Alert.show(getBaseContext(), "error", "No ha redactado una descripción de su historia", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
            } else if (spGenero.getSelectedItem().toString().equals("Seleccione un género")) {
                Alert.show(getBaseContext(), "error", "No ha seleccionado un genero", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
            }else {

                historia.setTitulo(etTitulo.getText().toString());
                historia.setDescripcion(etdescripcion.getText().toString());

                for (int i = 0; i < generosList.size(); i++) {
                    if (generosList.get(i).getGenero().equals(  spGenero.getSelectedItem().toString())) {
                       historia.setIdGenero(generosList.get(i));
                        break;
                    }
                }
                historia.setFecha_creacion(LocalDateTime.now(ZoneId.of("America/El_Salvador")));
                historia.setIdUsuario(MainActivity.usuario);
              if(  DaoHistoria.operarDatos(historia,"registrar")){
                  Alert.show(getBaseContext(), "exito", "Historia creada exitosamente", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                  finish();
                }else{
                  Alert.show(getBaseContext(), "error", "Ha ocurrido un error", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
              }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}