package com.flavio.plotdash.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class RegistroActivity extends AppCompatActivity {

    EditText usuario, clave, claveR, email, etNombre, etFotoPerfil;
    TextView tieneCuenta;
    CardView alert;
    LinearLayout layoutPB;
    Context context;
    Button btnInicio;
    Usuario usu;
    private FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    ImageView imagePreview;
    Button btnSelectImage;

    // Constante para identificar la solicitud de selección de imagen
    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        context = this;

        layoutPB = findViewById(R.id.layoutPB2);

        // inicialisacion de variables
        usuario = findViewById(R.id.usuario);
        email = findViewById(R.id.email);
        claveR = findViewById(R.id.claveR);
        clave = findViewById(R.id.clave);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        alert = findViewById(R.id.alert);
        btnInicio = findViewById(R.id.btnInicio);
        imagePreview = findViewById(R.id.imagePreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lanzar la actividad de selección de imagen
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
            }
        });

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
                    // Cambio: Obtener la imagen como una cadena Base64
                    String fotoPerfil = convertImageToBase64();
                    registrar(
                            usuario.getText().toString(),
                            clave.getText().toString(),
                            claveR.getText().toString(),
                            email.getText().toString(),
                            fotoPerfil
                    );
                } else {
                    Alert.show(getBaseContext(), "error", "Campos vacíos", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                }
            }
        });
    }

    private String convertImageToBase64() {
        Bitmap bitmap = ((BitmapDrawable) imagePreview.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    // Manejar el resultado de la selección de imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtener la Uri de la imagen seleccionada
            Uri selectedImageUri = data.getData();

            try {
                // Convertir la Uri a Bitmap y mostrarlo en el ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imagePreview.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            registrar(sharedPreferences.getString("usuario", ""), sharedPreferences.getString("clave", ""), sharedPreferences.getString("email", ""), sharedPreferences.getString("claveR", ""), sharedPreferences.getString("fotoPerfil", ""));
        } else {
            layoutPB.setVisibility(View.GONE);
        }
    }

    private void registrar(String usuario, String clave, String claveR, String email, String fotoPerfil) {
        verificarDisponibilidad(usuario, email, clave, fotoPerfil);
    }

    private void abrirActividadPrincipal(String usuario, String email, String fotoPerfil) {
        // Guardar información del usuario en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("email", email);
        editor.putString("fotoPerfil", fotoPerfil);
        editor.apply();

        // Abrir la actividad principal
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();  // Cierra la actividad actual para que el usuario no pueda volver atrás con el botón de retorno
    }
    private void verificarDisponibilidad(String usuario, String email, String clave, String fotoPerfil) {
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
                        registrarYVerificarCorreo(email, clave,usuario,fotoPerfil);
                        registrarEnServidor(usuario, email, clave, fotoPerfil);
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

    private void registrarEnServidor(String usuario, String email, String clave, String fotoPerfil) {
        RequestParams params = new RequestParams();
        params.put("opcion", "registrar");
        params.put("usu", usuario);
        params.put("email", email);
        params.put("clave", clave);
        params.put("fotoPerfil", fotoPerfil); // Cambio: Agregar la URL de la foto de perfil a los parámetros
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

    private void registrarYVerificarCorreo(String correoElectronico, String contraseña, String nombre, String fotoPerfil) {
        auth.createUserWithEmailAndPassword(correoElectronico, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();

                            // Agrega el nombre y la foto de perfil a la base de datos de Firebase
                            DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference();
                            String userId = user.getUid();

                            Map<String, Object> map = new HashMap<>();
                            map.put("idUser", userId);
                            map.put("name", nombre);
                            map.put("foto", fotoPerfil);

                            Usuario nuevoUsuario = new Usuario(fotoPerfil, nombre, correoElectronico);
                            usuariosRef.child(userId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()) {
                                        // Iniciar sesión después del registro exitoso
                                        abrirActividadPrincipal(nombre, correoElectronico, fotoPerfil);
                                    } else {
                                        Alert.show(getBaseContext(), "error", "Error al registrar Datos Firebase", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                                    }
                                }
                            });

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
