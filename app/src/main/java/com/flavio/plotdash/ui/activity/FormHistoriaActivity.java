package com.flavio.plotdash.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flavio.dao.DaoCapitulo;
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
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class FormHistoriaActivity extends AppCompatActivity {

    EditText etTitulo, etdescripcion;
    Spinner spGenero;
    ListView lvCaps;
    AdaptadorCapituloList adaptador;
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
        llenarCapitulos();
        btnAddCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormCapituloActivity.opcion = "registrar";
                Intent intent = new Intent(getBaseContext(), FormCapituloActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        registerForContextMenu(lvCaps);
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
        lvCaps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FormCapituloActivity.opcion = "modificar";
                Intent intent = new Intent(getBaseContext(), FormCapituloActivity.class);
                intent.putExtra("capituloM",(Capitulo) adapterView.getItemAtPosition(i));
                startActivityForResult(intent, 2);


            }
        });

        lvCaps.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
              showPopupMenu(view,(Capitulo) adapterView.getItemAtPosition(i));
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
                        // Después de actualizar generosList
                        adapter.notifyDataSetChanged();

                        spGenero.setAdapter(adapter);
                        layoutPB.setVisibility(View.GONE);
                        autoCompletar();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

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

    @Override
    protected void onResume() {
        llenarCapitulos();
        super.onResume();
    }

    private void llenarCapitulos() {
        if (historia==null || historia.getCapitulos() == null) {
            String[] mensaje = {"Sin registros que mostrar"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mensaje);
            lvCaps.setAdapter(adapter);
        } else {
            ArrayList<Capitulo> listaCaps=new ArrayList<>(historia.getCapitulos());
            listaCaps.removeIf(capitulo -> capitulo.getNum() == -1);
            adaptador = new AdaptadorCapituloList(this, R.layout.adapter_historia_list, listaCaps);
            lvCaps.setAdapter(adaptador);
        }
    }

    //Configuracion de menu de opciones en la barra superior
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
                if (opcion.equals("registrar")) {
                    historia.setFecha_creacion(LocalDateTime.now(ZoneId.of("America/El_Salvador")));
                    historia.setIdUsuario(MainActivity.usuario);
                }

              if(  DaoHistoria.operarDatos(historia,"registrar")){
                  if (opcion.equals("registrar")) {
                      Alert.show(getBaseContext(), "exito", "Historia creada exitosamente", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                      finish();
                  }else{
                      Alert.show(getBaseContext(), "exito", "Historia editada exitosamente", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                      finish();
                      opcion="registrar";
                  }
                }else{
                  Alert.show(getBaseContext(), "error", "Ha ocurrido un error", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
              }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Configuracion de menu contextual en lista de capitulos
    private void showPopupMenu(View view, Capitulo capitulo) {

        // Crear una instancia de PopupMenu
        PopupMenu popupMenu = new PopupMenu(getBaseContext(), view);

        // Inflar el menú desde un recurso XML
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.capitulo_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Lógica para manejar clics en elementos del menú
                switch (item.getItemId()) {
                    case R.id.opModificarC:
                        FormCapituloActivity.opcion = "modificar";
                        Intent intent = new Intent(getBaseContext(), FormCapituloActivity.class);
                        intent.putExtra("capituloM",capitulo);
                        startActivityForResult(intent, 2);


                        return true;
                    case R.id.opEliminarC:
                        Dialog dialog = new Dialog(view.getContext());
                        dialog.setContentView(R.layout.util_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView msjD = dialog.findViewById(R.id.msjD);
                        ImageView iconD = dialog.findViewById(R.id.iconDialog);
                        Button btnPos = dialog.findViewById(R.id.btnPos);
                        Button btnNeg = dialog.findViewById(R.id.btnNeg);
                        msjD.setText("Esta a punto de eliminar este " +
                                "capítulo ¿Desea continuar?");
                        iconD.setImageResource(R.drawable.outline_warning_amber_24);
                        btnPos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                eliminarYReordenar(capitulo);
                                dialog.dismiss();
                            }


                        });
                        btnNeg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                        return true;
                }
                return true;
            }
        });

        // Mostrar el menú emergente
        popupMenu.show();
    }
    private void eliminarYReordenar(Capitulo capitulo) {
        for (int i = 0; i < historia.getCapitulos().size(); i++) {
            Capitulo capituloActual = historia.getCapitulos().get(i);

            // Suponiendo que tienes un método equals() en la clase Capitulo
            if (capituloActual.equals(capitulo)) {
                historia.getCapitulos().get(i).setNum(-1);
                // Reordenar los números de los capítulos restantes
                for (int j = i + 1; j <  historia.getCapitulos().size(); j++) {
                    Capitulo capituloRestante =  historia.getCapitulos().get(j);
                    capituloRestante.setNum(capituloRestante.getNum() - 1);
                }

                // Romper el bucle después de marcar y reordenar
                break;
            }
        }
        llenarCapitulos();
    }

    @Override
    public void onBackPressed() {
        opcion="registrar";
        super.onBackPressed();
    }
}