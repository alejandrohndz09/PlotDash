package com.flavio.plotdash.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.model.Usuario;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class FormCapituloActivity extends AppCompatActivity {
    public static String opcion = "registrar";
    Dialog dialog;
    EditText etTitulo, etCapitulo;
    Capitulo capitulo;
    int isDialogShow=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_capitulo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(opcion.equals("registrar") ? "Nuevo Capítulo" : "Editar Capítulo");
        dialog = new Dialog(this);
        etTitulo = findViewById(R.id.etTitulo);
        etCapitulo = findViewById(R.id.etContenido);
        autoCompletar();
    }
    private void autoCompletar() {
        if (!opcion.equals("registrar")) {
            Bundle parametros = this.getIntent().getExtras();
            capitulo = (Capitulo) parametros.get("capituloM");
            etTitulo.setText(capitulo.getTitulo());
            etCapitulo.setText(capitulo.getContenido());
        } else {
            capitulo = new Capitulo();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
          mensajeConfirmacion();
        }
        return true;
    }

    void mensajeConfirmacion(){
        if (!etTitulo.getText().toString().isEmpty() || !etCapitulo.getText().toString().isEmpty() || !opcion.equals("registrar")) {
            dialog.setContentView(R.layout.util_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView msjD = dialog.findViewById(R.id.msjD);
            ImageView iconD = dialog.findViewById(R.id.iconDialog);
            Button btnPos = dialog.findViewById(R.id.btnPos);
            Button btnNeg = dialog.findViewById(R.id.btnNeg);
            msjD.setText("¿Desea guardar cambios?");
            iconD.setImageResource(R.drawable.outline_info_24);
            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    capitulo.setTitulo(etTitulo.getText().toString());
                    capitulo.setContenido(etCapitulo.getText().toString());
                    if (opcion.equals("registrar")) {
                        capitulo.setFecha_creado(LocalDateTime.now(ZoneId.of("America/El_Salvador")));
                    }
                    Intent intent = new Intent();
                    intent.putExtra("capitulo", capitulo);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            btnNeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    onBackPressed();
                }
            });
            dialog.show();
            isDialogShow=1;
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    isDialogShow=0;
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        opcion="registrar";
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (isDialogShow==0) {
            mensajeConfirmacion();
        }else{
            opcion="registrar";
            super.onBackPressed();
        }
    }
}

