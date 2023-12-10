package com.flavio.plotdash.ui.activity;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.flavio.dao.DaoUsuario;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.util.Alert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth auth;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        context = this;
        layoutPB = findViewById(R.id.layoutPB2);
        usuario = findViewById(R.id.usuario);
        email = findViewById(R.id.email);
        claveR = findViewById(R.id.claveR);
        clave = findViewById(R.id.clave);
        alert = findViewById(R.id.alert);
        btnInicio = findViewById(R.id.btnInicio);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

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
                    updateRegisterButtonState();
                }
            }
        });
        usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateRegisterButtonState();
                }
            }
        });
        clave.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateStrength(clave.getText().toString());
                    updateRegisterButtonState();
                }
            }
        });
        claveR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    verificarContraseñas(clave.getText().toString(), claveR.getText().toString());
                    updateRegisterButtonState();
                }
            }
        });

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usuario.getText().toString().isEmpty() && !clave.getText().toString().isEmpty() && !claveR.getText().toString().isEmpty() && !email.getText().toString().isEmpty()) {
                    registrar(usuario.getText().toString(), clave.getText().toString(), claveR.getText().toString(), email.getText().toString());
                } else {
                    Alert.show(getBaseContext(), "error", "Campos vacíos", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                }
            }
        });
    }

    private void updateRegisterButtonState() {
        String usuarioStr = usuario.getText().toString();
        String emailStr = email.getText().toString();
        String claveStr = clave.getText().toString();
        String claveRStr = claveR.getText().toString();

        boolean isNotEmpty = !usuarioStr.isEmpty() && !emailStr.isEmpty() && !claveStr.isEmpty() && !claveRStr.isEmpty();
        boolean isStrongEnough = calculateStrength(claveStr) >= 70;
        btnInicio.setEnabled(isNotEmpty && isStrongEnough);
    }

    private void verificarContraseñas(String contraseña, String repetirContraseña) {
        if (!contraseña.equals(repetirContraseña)) {
            Alert.show(getBaseContext(), "error", "Las contraseñas no coinciden", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
        } else {
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

        if (password.length() >= 8) {
            score += 10;
        }

        if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) {
            score += 20;
        }

        if (password.matches(".*\\d.*")) {
            score += 20;
        }

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
        verificarDisponibilidad(usuario, email, clave);
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
                        registrarYVerificarCorreo(email, clave);
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
                    String response = new String(responseBody, "UTF-8");
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean disponible = jsonResponse.getBoolean("disponible");
                    if (disponible) {
                        Alert.show(getBaseContext(), "exito", "Usuario Creado Correctamente", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);
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

    private void registrarYVerificarCorreo(String correoElectronico, String contraseña) {
        auth.createUserWithEmailAndPassword(correoElectronico, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Alert.show(getBaseContext(), "exito", "Correo de verificación enviado", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                                            } else {
                                                Alert.show(getBaseContext(), "exito", "Error al enviar el correo de verificación", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                                            }
                                        }
                                    });
                        } else {
                            Alert.show(getBaseContext(), "exito", "Error al registrar al usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                        }
                    }
                });
    }

}