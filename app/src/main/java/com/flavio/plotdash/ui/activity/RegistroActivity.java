package com.flavio.plotdash.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.flavio.dao.DaoUsuario;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.util.Alert;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends AppCompatActivity {

    EditText usuario, clave, claveR, email;
    TextView tieneCuenta;
    CardView alert;
    LinearLayout layoutPB;
    Context context;
    Button btnInicio;
    public static String opcion = "registrar";
    Usuario usu;
    AlertDialog.Builder alertDialog;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        context = this;
        alertDialog = new AlertDialog.Builder(this);
        layoutPB = findViewById(R.id.layoutPB2);
        usuario = findViewById(R.id.usuario);
        email = findViewById(R.id.email);
        claveR = findViewById(R.id.claveR);
        clave = findViewById(R.id.clave);
        alert = findViewById(R.id.alert);
        btnInicio = findViewById(R.id.btnInicio);

        sharedPreferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        getPreferencias();
        tieneCuenta = findViewById(R.id.txtyaTieneCuenta);
        tieneCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // El campo de texto ha perdido el foco, se ejecuta la lógica de fortaleza aquí
                    updateRegisterButtonState();
                }
            }
        });
        usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // El campo de texto de usuario ha perdido el foco, se ejecuta la lógica de validación aquí

                    updateRegisterButtonState();
                }
            }
        });
        clave.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // El campo de texto ha perdido el foco, se ejecuta la lógica de fortaleza aquí
                    updateStrength(clave.getText().toString());
                    updateRegisterButtonState();
                }
            }
        });
        claveR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // El campo de texto ha perdido el foco, se ejecuta la lógica de verificación de contraseñas aquí
                    verificarContraseñas(clave.getText().toString(), claveR.getText().toString());
                    updateRegisterButtonState();
                }
            }
        });


        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usuario.getText().toString().isEmpty() && !clave.getText().toString().isEmpty() && !claveR.getText().toString().isEmpty() && !email.getText().toString().isEmpty()) {
                    // Registrar en el servidor y verificar disponibilidad
                    registrar(usuario.getText().toString(), clave.getText().toString(), claveR.getText().toString(), email.getText().toString());
                } else {
                    Alert.show(getBaseContext(), "error", "Campos vacíos", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                }
            }
        });
    }

    private void updateRegisterButtonState() {
        // Obtener los valores de los campos
        String usuarioStr = usuario.getText().toString();
        String emailStr = email.getText().toString();
        String claveStr = clave.getText().toString();
        String claveRStr = claveR.getText().toString();

        // Validar que ningún campo esté vacío y que la fortaleza de la contraseña sea suficiente
        boolean isNotEmpty = !usuarioStr.isEmpty() && !emailStr.isEmpty() && !claveStr.isEmpty() && !claveRStr.isEmpty();
        boolean isStrongEnough = calculateStrength(claveStr) >= 70;
        // Habilitar el botón si se cumplen las condiciones
        btnInicio.setEnabled(isNotEmpty && isStrongEnough);
    }
    private void verificarContraseñas(String contraseña, String repetirContraseña) {
        if (!contraseña.equals(repetirContraseña)) {
            // Las contraseñas no coinciden, muestra un mensaje de error
            Alert.show(getBaseContext(), "error", "Las contraseñas no coinciden", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
        }else{
            Alert.show(getBaseContext(), "exito", "Contraseña Confirmada", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));

        }
    }
    private void updateStrength(String password) {
        int strength = calculateStrength(password);

        if (strength < 30) {
            Alert.show(getBaseContext(), "error", "Contraseña débil", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
        } else if (strength < 70) {
            Alert.show(getBaseContext(), "medio", "Contraseña moderada", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
        } else {
            Alert.show(getBaseContext(), "exito", "Contraseña fuerte", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
        }
    }

    private int calculateStrength(String password) {
        int score = 0;

        // Puntuar la longitud de la contraseña
        if (password.length() >= 8) {
            score += 10;
        }

        // Puntuar letras mayúsculas y minúsculas
        if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) {
            score += 20;
        }

        // Puntuar números
        if (password.matches(".*\\d.*")) {
            score += 20;
        }

        // Puntuar caracteres especiales
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            score += 30;
        }

        return score;
    }

    private void getPreferencias() {
        if (!sharedPreferences.getString("usuario", "").isEmpty()) {
            registrar(sharedPreferences.getString("usuario", ""), sharedPreferences.getString("clave", ""), sharedPreferences.getString("email", ""), sharedPreferences.getString("claveR", ""));
        } else {
            layoutPB.setVisibility(View.GONE);
        }
    }

    private void registrar(String usuario, String clave, String claveR, String email) {
        verificarDisponibilidad(usuario, email,clave);
    }

    private void verificarDisponibilidad(String usuario, String email, String clave) {
        RequestParams params = new RequestParams();
        params.put("opcion", "verificarDisponibilidad");
        params.put("usu", usuario);
        params.put("email", email);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(DaoUsuario.URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonResponse = new JSONObject(new String(responseBody));
                    boolean disponible = jsonResponse.optBoolean("disponible", false);

                    if (disponible) {
                        registrarEnServidor(usuario, email, clave);
                    } else {
                        String mensaje = jsonResponse.optString("mensaje", "Usuario o correo no disponibles");
                        Alert.show(getBaseContext(), "error", mensaje, Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Alert.show(getBaseContext(), "error", "Error en el formato de la respuesta del servidor", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Alert.show(getBaseContext(), "error", "Error en la solicitud HTTP", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
            }
        });
    }

    private void registrarEnServidor(String usuario, String email, String clave) {
        RequestParams params = new RequestParams();
        params.put("opcion", "registrar");
        params.put("usu", usuario);
        params.put("email", email);
        params.put("clave", clave);
        params.put("id", "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(DaoUsuario.URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                layoutPB.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    // Convertir la respuesta a una cadena
                    String response = new String(responseBody, "UTF-8");

                    // Parsear la respuesta JSON
                    JSONObject jsonResponse = new JSONObject(response);

                    // Verificar si el usuario se registró correctamente
                    boolean disponible = jsonResponse.getBoolean("disponible");
                    if (disponible) {
                        Alert.show(getBaseContext(), "exito", "Usuario Creado Correctamente", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                        // Crear un Intent para navegar a LoginActivity
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);

                        // Agregar datos del usuario a la intención
                        intent.putExtra("usuario", usuario);


                        // Iniciar la actividad de inicio de sesión
                        startActivity(intent);

                        // Finalizar la actividad actual
                        finish();

                    } else {
                        String mensaje = jsonResponse.getString("mensaje");
                        Alert.show(getBaseContext(), "error", mensaje, Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                    }
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Alert.show(getBaseContext(), "error", "Error al procesar la respuesta", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                }
            }

            @Override
            public void onFinish() {
                layoutPB.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Alert.show(getBaseContext(), "error", "Error en la solicitud HTTP", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
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
