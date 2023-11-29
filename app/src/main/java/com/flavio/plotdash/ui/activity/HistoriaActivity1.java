package com.flavio.plotdash.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.flavio.plotdash.R;

public class HistoriaActivity1 extends AppCompatActivity {

    private View slideLayout;
    private RecyclerView recyclerView;
    private Button btnSlide;
    private boolean isSliderOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia1);

        btnSlide = findViewById(R.id.btnSlide);


        btnSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
    }
    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.util_bottom_panel);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    private void showSlider() {
        slideLayout.setVisibility(View.VISIBLE);
        Animation animation = new TranslateAnimation(0, 0, slideLayout.getHeight(), 0);
        animation.setDuration(500);
        slideLayout.startAnimation(animation);
        isSliderOpen = true;
    }

    private void hideSlider() {
        Animation animation = new TranslateAnimation(0, 0, 0, slideLayout.getHeight());
        animation.setDuration(500);
        slideLayout.startAnimation(animation);
        slideLayout.setVisibility(View.GONE);
        isSliderOpen = false;
    }
}