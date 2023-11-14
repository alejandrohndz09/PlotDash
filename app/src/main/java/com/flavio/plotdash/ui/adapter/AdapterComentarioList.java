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
import com.flavio.plotdash.model.Comentario;
import com.flavio.plotdash.model.Comentario;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class AdapterComentarioList extends ArrayAdapter<Comentario> {

    public AdapterComentarioList(@NonNull Context context, int resource, ArrayList<Comentario> objects) {
        super(context, resource,objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Comentario comentario = getItem(position);

        //Recuperar la vista
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_comentario_list,parent,false);
        }
        //Colocar datos en los campos
        ImageView fotoUsuario = convertView.findViewById(R.id.foto);
        TextView nombre = convertView.findViewById(R.id.nombre);
        TextView fecha = convertView.findViewById(R.id.fecha);
        TextView txtComentario = convertView.findViewById(R.id.comentario);

        String img =  "https://cdn.icon-icons.com/icons2/2073/PNG/512/book_cover_layout_open_page_icon_127124.png";;
        fotoUsuario.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (!comentario.getIdUsuario().getFoto().equals("null")
                || comentario.getIdUsuario().getFoto().isEmpty()) {
            img = comentario.getIdUsuario().getFoto();
            fotoUsuario.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(convertView)
                .load(img)
                .into(fotoUsuario);
        ZoneId timeZone = ZoneId.of("America/El_Salvador");
        ZonedDateTime now = ZonedDateTime.now(timeZone);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(comentario.getFecha(), timeZone);
        Duration duration = Duration.between(zonedDateTime, now);
        if (duration.toDays() < 1) {

            fecha.setText("Creado hace "+((Comentario) comentario).getFechaCreacionFormateada());
        }else {

            fecha.setText("Creado el "+((Comentario) comentario).getFechaCreacionFormateada());
        }
//        fecha.setText( comentario.getFecha().toString());
        nombre.setText(comentario.getIdUsuario().getNombre()+' '+comentario.getIdUsuario().getApellido());

        txtComentario.setText(comentario.getComentario());

        return convertView;
    }
}

