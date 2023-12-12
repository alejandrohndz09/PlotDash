package com.flavio.plotdash.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.flavio.dao.DaoUsuario;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.util.Alert;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText usuario, clave;
    TextView lblCrearCuenta, forgotPassword;
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
        context = this;
        layoutPB = findViewById(R.id.layoutPB2);
        usuario = findViewById(R.id.usuario);
        clave = findViewById(R.id.clave);
        alert = findViewById(R.id.alert);
        btnInicio = findViewById(R.id.btnInicio);
        sharedPreferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        getPreferencias();

        lblCrearCuenta = findViewById(R.id.txtNotieneCuenta);
        forgotPassword = findViewById(R.id.forgotPassword);

        Intent intent = getIntent();
        if (intent != null) {
            String usuarioRegistrado = intent.getStringExtra("usuario");
            if (usuarioRegistrado != null) {
                usuario.setText(usuarioRegistrado);
            }
        }

        lblCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(usuario.getText().toString()) && !TextUtils.isEmpty(clave.getText().toString())) {
                    iniciarSesionConCorreoYClave(usuario.getText().toString(), clave.getText().toString());
                } else {
                    Alert.show(getBaseContext(), "error", "Campos vacíos", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoRecuperarContrasena();
            }
        });
    }

    private void mostrarDialogoRecuperarContrasena() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar Contraseña");
        View viewInflated = getLayoutInflater().inflate(R.layout.dialog_recuperar_contrasena, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputEmail = viewInflated.findViewById(R.id.inputEmailRecuperar);
        builder.setView(viewInflated);

        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = inputEmail.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    resetearContrasena(email);
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void resetearContrasena(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Alert.show(getBaseContext(), "exito", "Correo de restablecimiento enviado", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                    } else {
                        Alert.show(getBaseContext(), "error", "Error al enviar el correo de restablecimiento", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                    }
                });
    }

    private void iniciarSesionConCorreoYClave(String correo, String clave) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            verificarYContinuar(user,clave);
                        } else {
                            Alert.show(getBaseContext(), "error", "Error al obtener información del usuario", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                        }
                    } else {
                        Alert.show(getBaseContext(), "error", "Credenciales Incorrectas", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                    }
                });
    }
    private void verificarYContinuar(FirebaseUser user,String pass) {
        if (correoElectronicoVerificado()) {

            Alert.show(getBaseContext(), "exito", "¡Hola! @" + user.getEmail(), Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));


            guardarPreferencias(user.getEmail(),pass);
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getBaseContext().startActivity(intent);
            finish();
        } else {
            Alert.confirmDialog(context, "Tu correo electrónico aún no ha sido verificado. ¿Deseas reenviar el correo de verificación?",
                    "info",
                    new Alert.DialogCustumizer() {
                        @Override
                        public void custumizer(Dialog dialog) {
                            // Configurar el diálogo según tus necesidades
                        }

                        @Override
                        public void onPositiveClick(Dialog dialog) {
                            reenviarCorreoVerificacion();
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegativeClick(Dialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }



    private void reenviarCorreoVerificacion() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Alert.show(getBaseContext(), "exito", "Correo de verificación enviado", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                        } else {
                            Alert.show(getBaseContext(), "error", "Error al enviar el correo de verificación", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                        }
                    });
        }
    }

    private boolean correoElectronicoVerificado() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null && user.isEmailVerified();
    }

    private void getPreferencias() {
        if (!TextUtils.isEmpty(sharedPreferences.getString("usuario", ""))) {
            iniciarSesionConCorreoYClave(sharedPreferences.getString("usuario", ""), sharedPreferences.getString("clave", ""));
        } else {
            layoutPB.setVisibility(View.GONE);
        }
    }
    private void guardarPreferencias(String usuario, String clave) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("clave", clave);
        editor.apply();
    }
}
