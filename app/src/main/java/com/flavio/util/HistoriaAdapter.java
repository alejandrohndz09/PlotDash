package com.flavio.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flavio.model.Historia;
import com.flavio.plotdash.BibliotecaFragment;
import com.flavio.plotdash.HistoriaActivity;
import com.flavio.plotdash.R;

import java.util.List;

public class HistoriaAdapter extends RecyclerView.Adapter<HistoriaAdapter.ViewHolder> {

    List<Historia> historiaList;
    Context context;
    RecyclerView rv_parent;

    public HistoriaAdapter(List<Historia> historiaList, Context context) {
        this.historiaList = historiaList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.historia_layout, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriaAdapter.ViewHolder holder, int position) {
        holder.iconoVista.setImageResource(R.drawable.baseline_remove_red_eye_24);
        holder.iconoCalif.setImageResource(R.drawable.baseline_star_rate_24);
        holder.portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        String img =  "https://cdn.icon-icons.com/icons2/2073/PNG/512/book_cover_layout_open_page_icon_127124.png";;
        System.out.println(historiaList.get(position).getPortada());
        if (!historiaList.get(position).getPortada().equals("null")
                || historiaList.get(position).getPortada().isEmpty()) {
            img = historiaList.get(position).getPortada();
            holder.portada.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        Glide.with(context)
                .load(img)
                .into(holder.portada);
        holder.titulo.setText(historiaList.get(position).getTitulo());
        holder.vistas.setText(historiaList.get(position).getVistas() + "");
        holder.calificacion.setText(String.format("%.1f", historiaList.get(position).getCalificacion()));
        holder.historiaItem.setOnClickListener(e -> {
            Historia historia = historiaList.get(position);
            Intent intent = new Intent(context, HistoriaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             intent.putExtra("idHistoria", historia);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historiaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView portada, iconoCalif, iconoVista;
        TextView vistas, calificacion, titulo;

        CardView historiaItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historiaItem = itemView.findViewById(R.id.historiaItem);
            portada = itemView.findViewById(R.id.portada);
            titulo = itemView.findViewById(R.id.titulo);
            calificacion = itemView.findViewById(R.id.calificacion);
            vistas = itemView.findViewById(R.id.vistas);
            iconoCalif = itemView.findViewById(R.id.iconoCalif);
            iconoVista = itemView.findViewById(R.id.iconoVista);
        }


    }
}
