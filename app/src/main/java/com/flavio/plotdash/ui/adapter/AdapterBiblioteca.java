package com.flavio.plotdash.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Biblioteca;
import com.flavio.plotdash.ui.fragment.BibliotecaFragment;

import java.util.ArrayList;
import java.util.Collections;

public class AdapterBiblioteca extends ArrayAdapter<Biblioteca> {

    public AdapterBiblioteca(@NonNull Context context, int resource, ArrayList<Biblioteca> objects) {
        super(context, resource, (objects));
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Biblioteca persona = getItem(position);

        //Recuperar la vista
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_biblioteca_list,parent,false);
        }
        //Colocar datos en los campos
        TextView nombre = convertView.findViewById(R.id.nombre_biblioteca);
        TextView fecha = convertView.findViewById(R.id.fecha_creacion);
        TextView tipo = convertView.findViewById(R.id.tipo_biblioteca);


        nombre.setText(((Biblioteca) persona).getNombre()+" ("+ persona.getDetalles().size()+" historias)");
        fecha.setText(((Biblioteca) persona).getFecha_creacion().toString());


        return convertView;
    }
}