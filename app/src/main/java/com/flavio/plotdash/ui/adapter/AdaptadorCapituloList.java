package com.flavio.plotdash.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Capitulo;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class AdaptadorCapituloList extends ArrayAdapter<Capitulo> {

    public AdaptadorCapituloList(@NonNull Context context, int resource, ArrayList<Capitulo> objects) {
        super(context, resource,objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Capitulo capitulo = getItem(position);
        //Recuperar la vista
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_capitulo_list,parent,false);
        }
        //Colocar datos en los campos
        ImageView iconoFecha=convertView.findViewById(R.id.iconoFecha);
        TextView nombre = convertView.findViewById(R.id.titulo);
        TextView fecha = convertView.findViewById(R.id.fecha);



        ZoneId timeZone = ZoneId.of("America/El_Salvador");
        ZonedDateTime now = ZonedDateTime.now(timeZone);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(capitulo.getFecha_creado(), timeZone);
        Duration duration = Duration.between(zonedDateTime, now);
        if (duration.toDays() < 1) {
            iconoFecha.setImageResource(R.drawable.baseline_access_time_24);
            fecha.setText("Creado hace "+((Capitulo) capitulo).getFechaCreacionFormateada());
        }else {
            iconoFecha.setImageResource(R.drawable.baseline_calendar_today_24);
            fecha.setText("Creado el "+((Capitulo) capitulo).getFechaCreacionFormateada());
        }
        nombre.setText(capitulo.getTitulo().isEmpty()?"Capitulo "+capitulo.getNum()+": [Sin Título]":"Capitulo "+capitulo.getNum()+": "+capitulo.getTitulo());

        return convertView;
    }
}
