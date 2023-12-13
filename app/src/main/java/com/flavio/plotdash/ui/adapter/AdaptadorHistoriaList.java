package com.flavio.plotdash.ui.adapter;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.flavio.dao.DaoHistoria;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.activity.FormHistoriaActivity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class AdaptadorHistoriaList extends ArrayAdapter<Historia> {

    private static final float NORMAL_SCALE = 1.0f;
    private static final float PRESSED_SCALE = 0.9f;
    public AdaptadorHistoriaList(@NonNull Context context, int resource, ArrayList<Historia> objects) {
        super(context, resource, objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Historia historia = getItem(position);

        //Recuperar la vista
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_historia_list, parent, false);
        }
        //Colocar datos en los campos
        ImageView portada = convertView.findViewById(R.id.portada);
        ImageView iconoFecha = convertView.findViewById(R.id.iconoFecha);
        TextView nombre = convertView.findViewById(R.id.nombre);
        TextView fecha = convertView.findViewById(R.id.fecha);
        TextView estado = convertView.findViewById(R.id.estado);

        String img = "https://cdn.icon-icons.com/icons2/2073/PNG/512/book_cover_layout_open_page_icon_127124.png";
        ;
        portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (!historia.getPortada().equals("null")
                || historia.getPortada().isEmpty()) {
            img = historia.getPortada();
            portada.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(convertView)
                .load(img)
                .into(portada);
        ZoneId timeZone = ZoneId.of("America/El_Salvador");
        ZonedDateTime now = ZonedDateTime.now(timeZone);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(historia.getFecha_creacion(), timeZone);
        Duration duration = Duration.between(zonedDateTime, now);
        if (duration.toDays() < 1) {
            iconoFecha.setImageResource(R.drawable.baseline_access_time_24);
            fecha.setText("Creado hace " + ((Historia) historia).getFechaCreacionFormateada());
        } else {
            iconoFecha.setImageResource(R.drawable.baseline_calendar_today_24);
            fecha.setText("Creado el " + ((Historia) historia).getFechaCreacionFormateada());
        }
        nombre.setText(((Historia) historia).getTitulo());
        estado.setText(historia.getEstado() == 0 ? "Sin publicar" : "Publicada");
        int badgeDrawable = (historia.getEstado() == 0) ? R.drawable.badge_simple : R.drawable.badge_success;
        estado.setBackgroundResource(badgeDrawable);
        int textColor = (historia.getEstado() == 0) ? convertView.getResources().getColor(R.color.badgeSimpleTextColor) : convertView.getResources().getColor(R.color.badgeSuccessTextColor);
        estado.setTextColor(textColor);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormHistoriaActivity.opcion = "modificar";
                Intent intent = new Intent(getContext(), FormHistoriaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("historia", historia);
                getContext().startActivity(intent);
            }
        });

            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startScaleAnimation(view, PRESSED_SCALE);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            startScaleAnimation(view, NORMAL_SCALE);
                            break;
                    }
                    return false;
                }
            });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        return convertView;
    }
    private void startScaleAnimation(final View view, float targetScale) {
        ValueAnimator animator = ValueAnimator.ofFloat(view.getScaleX(), targetScale);
        animator.setDuration(300); // Ajusta la duración según sea necesario
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        animator.start();
    }

}