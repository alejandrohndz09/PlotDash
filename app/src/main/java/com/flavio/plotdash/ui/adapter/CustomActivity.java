package com.flavio.plotdash.ui.adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.flavio.plotdash.R;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adapter_historia_list);
    }
}