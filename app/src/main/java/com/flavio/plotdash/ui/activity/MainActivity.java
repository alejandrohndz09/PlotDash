package com.flavio.plotdash.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.flavio.dao.DaoUsuario;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.activity.LoginActivity;
import com.flavio.plotdash.ui.fragment.BibliotecaFragment;
import com.flavio.plotdash.ui.fragment.HistoriasFragment;
import com.flavio.plotdash.ui.fragment.HomeFragment;
import com.flavio.plotdash.ui.adapter.AdapterVistaPrincipal;
import com.flavio.plotdash.ui.fragment.SettingFragment;
import com.flavio.plotdash.ui.util.Alert;
import com.flavio.plotdash.ui.util.BitmapManage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    // public static String URL_BASE = "http://192.168.1.5/plotdashserver/pages/";
    //public final static String URL_BASE = "http://plotdash.free.nf/plotdashserver/pages/";
    //public final static String URL_BASE = "https://plotdash.000webhostapp.com/plotdashserver/pages/";
    public static String url = null;
    public final static String URL_BASE = "http://ingenieria.software.sistemascsc.com/plotdashserver/pages/";
    //public final static String URL_BASE = "http://ingenieria.software.sistemascsc.com/plotdashserver/pages/";
    public static Usuario usuario;
    SharedPreferences sharedPreferences;
    ViewPager2 pagerMain;
    Dialog dialog;
    BottomNavigationView bmNav;
    DatabaseReference mData;
    String nombre;
    String foto;
    ArrayList<Fragment> fragArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        pagerMain = findViewById(R.id.pagerMain);
        bmNav = findViewById(R.id.bmNav);
        fragArray.add(new HomeFragment());
        fragArray.add(new BibliotecaFragment());
        fragArray.add(new HistoriasFragment());
        fragArray.add(new SettingFragment());

        AdapterVistaPrincipal adapterViewPage = new AdapterVistaPrincipal(this, fragArray);
        pagerMain.setAdapter(adapterViewPage);

        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bmNav.setSelectedItemId(R.id.opHome);
                        break;
                    case 1:
                        bmNav.setSelectedItemId(R.id.opLibrary);
                        break;
                    case 2:
                        bmNav.setSelectedItemId(R.id.opWrite);
                        break;
                    case 3:
                        bmNav.setSelectedItemId(R.id.opAjustes);
                        break;
                }
                super.onPageSelected(position);
            }
        });


        bmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.opHome:
                        pagerMain.setCurrentItem(0);
                        break;
                    case R.id.opLibrary:
                        pagerMain.setCurrentItem(1);
                        break;
                    case R.id.opWrite:
                        pagerMain.setCurrentItem(2);
                        break;

                    case R.id.opAjustes:
                        pagerMain.setCurrentItem(3);
                        break;
                }
                return true;
            }


        });




        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        mData= FirebaseDatabase.getInstance().getReference();

        if (currentUser != null) {
            // El usuario está autenticado, puedes acceder a la información del usuario
            String userEmail = currentUser.getEmail();
            String userId = currentUser.getUid();
            iniciar(userEmail);
           mData.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nom = snapshot.child("name").getValue().toString();
                     foto = snapshot.child("foto").getValue().toString();
                }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem op = menu.findItem(R.id.profile);
        //String url = "https://img.a.transfermarkt.technology/portrait/big/8198-1685035469.png?lm=1";
         url = "https://cdn-icons-png.flaticon.com/512/149/149071.png";

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        mData= FirebaseDatabase.getInstance().getReference();

        if (currentUser != null) {
            // El usuario está autenticado, puedes acceder a la información del usuario
            String userEmail = currentUser.getEmail();
            String userId = currentUser.getUid();
            iniciar(userEmail);
            mData.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String nom = snapshot.child("name").getValue().toString();
                        String fotos = snapshot.child("foto").getValue().toString();
                        if (!fotos.isEmpty() ) {
                            url = fotos;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        BitmapManage.convertGlideImageToDrawableOrBitmap(this, url, new BitmapManage.ImageConversionCallback() {
            @Override
            public void onImageConverted(Drawable drawable) {
                op.setIcon(drawable);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.subMenuOP1) {
            dialog.setContentView(R.layout.util_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button btnPos = dialog.findViewById(R.id.btnPos);
            Button btnNeg = dialog.findViewById(R.id.btnNeg);
            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Cierre de sesion,primero eliminamos las preferencias
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario", "");
                    editor.putString("clave", "");
                    editor.putBoolean("estado", false);
                    editor.commit();
                    MainActivity.usuario = new Usuario();
                    //Luego mandamos la vista del login
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(intent);
                    finish();
                }
            });
            btnNeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        return true;
    }

    private void iniciar(String correo) {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        parametros.put("opcion", "inicioSesion");
        parametros.put("correo", correo);

        client.setTimeout(3000);

        client.post(DaoUsuario.URL, parametros, new AsyncHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    if (DaoUsuario.obtenerList(new String(responseBody)).size() > 0) {
                        MainActivity.usuario = DaoUsuario.obtenerList(new String(responseBody)).get(0);


                    } else {
                        Alert.show(getBaseContext(), "error", "Credenciales Incorrectas",Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, findViewById(R.id.toastCustom)));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }


        });
    }
}
