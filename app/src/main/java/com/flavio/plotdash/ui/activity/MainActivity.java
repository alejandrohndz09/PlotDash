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
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.activity.LoginActivity;
import com.flavio.plotdash.ui.fragment.BibliotecaFragment;
import com.flavio.plotdash.ui.fragment.HistoriasFragment;
import com.flavio.plotdash.ui.fragment.HomeFragment;
import com.flavio.plotdash.ui.adapter.AdapterVistaPrincipal;
import com.flavio.plotdash.ui.fragment.SettingFragment;
import com.flavio.plotdash.ui.util.BitmapManage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // public static String URL_BASE = "http://192.168.1.5/plotdashserver/pages/";
    //public final static String URL_BASE = "http://plotdash.free.nf/plotdashserver/pages/";
    //public final static String URL_BASE = "https://plotdash.000webhostapp.com/plotdashserver/pages/";

    public final static String URL_BASE = "http://ingenieria.software.sistemascsc.com/plotdashserver/pages/";
    //public final static String URL_BASE = "http://ingenieria.software.sistemascsc.com/plotdashserver/pages/";
    public static Usuario usuario;
    SharedPreferences sharedPreferences;
    ViewPager2 pagerMain;
    Dialog dialog;
    BottomNavigationView bmNav;
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

        // Obtiene la instancia actual de FirebaseAuth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Obtiene el usuario actualmente autenticado
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // El usuario está autenticado, puedes acceder a la información del usuario
            String userEmail = currentUser.getEmail();
            String userId = currentUser.getUid();

            // Asegúrate de manejar la información del usuario según tus necesidades
            // Por ejemplo, puedes mostrar el correo electrónico en la barra de acción:
            getSupportActionBar().setTitle(userEmail);
        } else {
            // El usuario no está autenticado, puedes redirigirlo al LoginActivity u otra lógica
            getSupportActionBar().setTitle("Usuario no autenticado");
        }
    }

    // Resto del código...

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
}
