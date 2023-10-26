package com.flavio.plotdash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flavio.model.Usuario;
import com.flavio.util.AdapterViewPage;
import com.flavio.util.BitmapManage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Usuario usuario;
    SharedPreferences sharedPreferences;
    ViewPager2 pagerMain;
    Dialog dialog;
   // public static String URL_BASE = "http://192.168.1.5/plotdashserver/pages/";
    //public final static String URL_BASE = "http://plotdash.free.nf/plotdashserver/pages/";
    //public final static String URL_BASE = "https://plotdash.000webhostapp.com/plotdashserver/pages/";

    public final static String URL_BASE = "http://10.51.21.22/plotdashserver/pages/";
    //public final static String URL_BASE = "http://ingenieria.software.sistemascsc.com/plotdashserver/pages/";
    BottomNavigationView bmNav;
    ArrayList<Fragment> fragArray = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        pagerMain = findViewById(R.id.pagerMain);
        bmNav = findViewById(R.id.bmNav);
        fragArray.add(new HomeFragment());
        fragArray.add(new BibliotecaFragment());
        fragArray.add(new HistoriasFragment());
        AdapterViewPage adapterViewPage = new AdapterViewPage(this, fragArray);
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
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem op = menu.findItem(R.id.profile);

        //String url = "https://img.a.transfermarkt.technology/portrait/big/8198-1685035469.png?lm=1";
        String url = "https://cdn-icons-png.flaticon.com/512/149/149071.png";
        if (!usuario.getFoto().isEmpty()
                && !usuario.getFoto().equals("null")) {
            url = usuario.getFoto();
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
            dialog.setContentView(R.layout.dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button btnPos = dialog.findViewById(R.id.btnPos);
            Button btnNeg = dialog.findViewById(R.id.btnNeg);
            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Cierre de sesion,primero eliminamos las preferencias
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario","");
                    editor.putString("clave", "");
                    editor.putBoolean("estado", false);
                    editor.commit();
                    MainActivity.usuario=new Usuario();
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