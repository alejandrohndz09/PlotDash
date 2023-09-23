package com.flavio.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flavio.model.Usuario;
import com.flavio.plotdash.LoginActivity;
import com.flavio.plotdash.MainActivity;
import com.flavio.plotdash.R;

import org.checkerframework.common.returnsreceiver.qual.This;

public  class Alert  {

    public static void show(Context context, String tipo, String texto, int duracion,ViewGroup v) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = v;
        TextView msj = view.findViewById(R.id.msj);
        ImageView iconAlert = view.findViewById(R.id.iconAlert);

        if (tipo.equals("error")) {
            iconAlert.setImageResource(R.drawable.baseline_error_outline_24);
        } else if (tipo.equals("exito")) {
            iconAlert.setImageResource(R.drawable.baseline_check_circle_outline_24);
        }else if (tipo.equals("info")) {
            iconAlert.setImageResource(R.drawable.outline_info_24);
        }else if (tipo.equals("warning")) {
            iconAlert.setImageResource(R.drawable.outline_warning_amber_24);
        }
        msj.setText(texto);
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(duracion);
        toast.show();
    }

    public static Dialog confirmDialog(Context context, String mensaje, String tipo,  DialogCustumizer dialogCustumizer){
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ((TextView) dialog.findViewById(R.id.msjD)).setText(mensaje);
        if (tipo.equals("error")) {
            ((ImageView) dialog.findViewById(R.id.iconDialog)).setImageResource(R.drawable.baseline_error_outline_24);
        } else if (tipo.equals("exito")) {
            ((ImageView) dialog.findViewById(R.id.iconDialog)).setImageResource(R.drawable.baseline_check_circle_outline_24);
        }else if (tipo.equals("info")) {
            ((ImageView) dialog.findViewById(R.id.iconDialog)).setImageResource(R.drawable.outline_info_24);
        }else if (tipo.equals("warning")) {
            ((ImageView) dialog.findViewById(R.id.iconDialog)).setImageResource(R.drawable.outline_warning_amber_24);
        }
        dialogCustumizer.custumizer(dialog);
        Button btnPos = dialog.findViewById(R.id.btnPos);
        Button btnNeg = dialog.findViewById(R.id.btnNeg);
        btnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustumizer.onPositiveClick(dialog);
            }
        });
        btnNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustumizer.onNegativeClick(dialog);
            }
        });
        return dialog;
    }
    public interface DialogCustumizer {
        void custumizer(Dialog dialog);
        void onPositiveClick(Dialog dialog);
        void onNegativeClick(Dialog dialog);
    }
}
