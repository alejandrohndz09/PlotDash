package com.flavio.plotdash.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flavio.dao.DaoUsuario;
import com.flavio.plotdash.R;
import com.flavio.plotdash.ui.util.Alert;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {


    EditText usuario, clave;
    TextView lblCrearCuenta;
    CardView alert;
    LinearLayout layoutPB;
    Context context;
    Button btnInicio;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;
        layoutPB = findViewById(R.id.layoutPB2);
        usuario = findViewById(R.id.usuario);
        clave = findViewById(R.id.clave);
        alert = findViewById(R.id.alert);
        btnInicio = findViewById(R.id.btnInicio);
        sharedPreferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        getPreferencias();

        lblCrearCuenta = findViewById(R.id.txtNotieneCuenta);
        // Obtener el extra del intent
        Intent intent = getIntent();
        if (intent != null) {
            String usuarioRegistrado = intent.getStringExtra("usuario");

            // Hacer algo con el nombre de usuario, por ejemplo, establecerlo en el campo de nombre de usuario
            if (usuarioRegistrado != null) {
                usuario.setText(usuarioRegistrado);
            }
        }

        lblCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistroActivity.class));
            }
        });
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usuario.getText().toString().isEmpty() && !clave.getText().toString().isEmpty()) {
                    iniciar(usuario.getText().toString(), clave.getText().toString());
                } else {
                    Alert.show(getBaseContext(), "error", "Campos vacíos", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                }
            }
        });
    }
    private void setPreferencias() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", MainActivity.usuario.getUsuario());
        editor.putString("clave", MainActivity.usuario.getClave());
        editor.putBoolean("estado", true);
        editor.commit();
    }

    private void getPreferencias() {
        if (!sharedPreferences.getString("usuario", "").isEmpty()) {
            iniciar(sharedPreferences.getString("usuario", ""), sharedPreferences.getString("clave", ""));
        } else {
            layoutPB.setVisibility(View.GONE);
        }
    }

    private void iniciar(String usuario, String clave) {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        parametros.put("opcion", "inicioSesion");
        parametros.put("usuario", usuario);
        parametros.put("clave", clave);
        client.setTimeout(3000);

        client.post(DaoUsuario.URL, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                alert.setVisibility(View.VISIBLE);
               Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                alert.startAnimation(fadeInAnimation);
                alert.requestFocus();
            }

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    if (DaoUsuario.obtenerList(new String(responseBody)).size() > 0) {
                        MainActivity.usuario = DaoUsuario.obtenerList(new String(responseBody)).get(0);
                        setPreferencias();
                        Alert.show(getBaseContext(), "exito", "¡Hola! @" + MainActivity.usuario.getUsuario(), Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getBaseContext().startActivity(intent);
                        finish();
                    } else {
                        Alert.show(getBaseContext(), "error", "Credenciales Incorrectas", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                    }
                }
            }

            @Override
            public void onFinish() {
                AnimationUtilas.animateFadeOut(alert);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (error instanceof java.net.SocketTimeoutException) {
                    // Maneja el timeout aquí
                    Alert.confirmDialog(context, "Ha ocurrido un error, revisa tu conexión a internet", "info",
                            new Alert.DialogCustumizer() {
                        @Override
                        public void custumizer(Dialog dialog) {
                            dialog.setCancelable(false);
                            ((Button) dialog.findViewById(R.id.btnNeg)).setText("Cerrar");
                            ((Button) dialog.findViewById(R.id.btnPos)).setText("Reintentar");
                        }
                        @Override
                        public void onPositiveClick(Dialog dialog) {
                            getPreferencias();
                            dialog.dismiss();
                        }
                        @Override
                        public void onNegativeClick(Dialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
                } else {
                    System.out.println(error.getMessage()/*new String(responseBody)*/);
                }
            }
        });
    }

    public static class AnimationUtilas {
        public static void animateFadeOut(final View view) {
            Animation fadeOutAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_out);
            fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(fadeOutAnimation);
        }
    }

}
