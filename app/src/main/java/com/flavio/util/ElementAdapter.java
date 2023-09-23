package com.flavio.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flavio.dao.DaoGenero;
import com.flavio.dao.DaoHistoria;
import com.flavio.dao.DaoUsuario;
import com.flavio.model.Capitulo;
import com.flavio.model.Genero;
import com.flavio.model.Historia;
import com.flavio.model.Usuario;
import com.flavio.plotdash.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ElementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> itemList;
    Context context;
    // Tipos de ViewHolder
    private static final int VIEW_TYPE_STORY = 1;
    private static final int VIEW_TYPE_CHAPTER = 2;

    public ElementAdapter(List<Object> itemList,Context context) {
        this.itemList = itemList;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_TYPE_STORY) {
            // Inflar diseño de ViewHolder para la historia
            view = inflater.inflate(R.layout.encabezado_layout, parent, false);
            viewHolder = new StoryViewHolder(view);
        } else if (viewType == VIEW_TYPE_CHAPTER) {
            // Inflar diseño de ViewHolder para los capítulos
            view = inflater.inflate(R.layout.capitulo_layout, parent, false);
            viewHolder = new ChapterViewHolder(view);
        }else {
            return null;
        }
        return viewHolder;
    }
    @Override
    public int getItemViewType(int position) {

        if (itemList.get(position) instanceof Historia) {
            return VIEW_TYPE_STORY;
        } else if (itemList.get(position) instanceof Capitulo) {
            return VIEW_TYPE_CHAPTER;
        }

        return -1;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = itemList.get(position);

        if (holder instanceof StoryViewHolder) {
            // Enlace de datos para la historia
            StoryViewHolder storyViewHolder = (StoryViewHolder) holder;
            Historia historia = (Historia) item;
            storyViewHolder.bindData(historia);
        } else if (holder instanceof ChapterViewHolder) {
            // Enlace de datos para los capítulos
            ChapterViewHolder chapterViewHolder = (ChapterViewHolder) holder;
            Capitulo chapter = (Capitulo) item;
            chapterViewHolder.bindData(chapter);
        }
    }
    void obtenerObj(String tipo, int id, StoryViewHolder holder){
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        parametros.put("opcion", "buscar");

        String url= DaoGenero.URL;
        if (tipo.equals("usuario")){
            parametros.put("idUsuario", id+"");
            url=DaoUsuario.URL;
        }else{
            parametros.put("idGenero", id+"");
        }
        client.post(url, parametros, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (statusCode == 200) {
                    if (tipo.equals("usuario")){
                        Usuario u= DaoUsuario.obtenerList(new String(responseBody)).get(0);
                        ((StoryViewHolder) holder).autor.setText("@"+u.getUsuario());
                    }else{
                        Genero u=DaoGenero.obtenerList(new String(responseBody)).get(0);
                        ((StoryViewHolder) holder).genero.setText(u.getGenero());
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloCap, contenido;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            tituloCap = itemView.findViewById(R.id.tituloCap);
            contenido = itemView.findViewById(R.id.contenido);

        }
        public void bindData(Capitulo capitulo) {
            tituloCap.setText(capitulo.getTitulo());
            contenido.setText(capitulo.getContenido());
        }

    }

    public  class StoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView portada,iconoUser,iconoVista,iconoCalif;
        private TextView titulo,genero, descripcion, calificacion, vistas, autor;

        public StoryViewHolder(View itemView) {
            super(itemView);
            portada = itemView.findViewById(R.id.portada);
            iconoCalif = itemView.findViewById(R.id.iconoCalif2);
            iconoVista = itemView.findViewById(R.id.iconoVista2);
            iconoUser = itemView.findViewById(R.id.iconoUser2);
            titulo = itemView.findViewById(R.id.titulohis);
            descripcion = itemView.findViewById(R.id.descripcion);
            calificacion = itemView.findViewById(R.id.calificacion);
            vistas = itemView.findViewById(R.id.vistas);
            autor = itemView.findViewById(R.id.autor);
            genero = itemView.findViewById(R.id.genero);
        }

        void bindData(Historia historia) {
            // Enlazar datos de la historia a las vistas
            String img =  "https://cdn.icon-icons.com/icons2/2073/PNG/512/book_cover_layout_open_page_icon_127124.png";;
            portada.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if (!historia.getPortada().equals("null")
                    || historia.getPortada().isEmpty()) {
                img = historia.getPortada();
                portada.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            Glide.with(itemView)
                    .load(img)
                    .into(portada);
            iconoUser.setImageResource(R.drawable.baseline_person_24);
            iconoVista.setImageResource(R.drawable.baseline_remove_red_eye_24);
            iconoCalif.setImageResource(R.drawable.baseline_star_rate_24);
            titulo.setText(historia.getTitulo());
            descripcion.setText(historia.getDescripcion());
            calificacion.setText(String.format("%.1f", historia.getCalificacion()));
            vistas.setText(historia.getVistas()+"");
            obtenerObj("usuario", historia.getIdUsuario().getIdUsuario());
            obtenerObj("genero", historia.getIdGenero().getIdGenero());

        }

         void obtenerObj(String tipo, int id){
            RequestParams parametros = new RequestParams();
            AsyncHttpClient client = new AsyncHttpClient();

            parametros.put("opcion", "buscar");

            String url= DaoGenero.URL;
            if (tipo.equals("usuario")){
                parametros.put("idUsuario", id+"");
                url=DaoUsuario.URL;
            }else{
                parametros.put("idGenero", id+"");
            }
            client.post(url, parametros, new AsyncHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    if (statusCode == 200) {
                        if (tipo.equals("usuario")){
                            Usuario u= DaoUsuario.obtenerList(new String(responseBody)).get(0);
                            autor.setText("@"+u.getUsuario());
                        }else{
                            Genero u=DaoGenero.obtenerList(new String(responseBody)).get(0);
                            genero.setText(u.getGenero());
                        }
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println(new String(responseBody));
                }
            });
        }

    }

// Agrega más clases ViewHolder para otros tipos de pistas si es necesario

}
