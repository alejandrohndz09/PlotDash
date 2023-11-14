package com.flavio.dao;

import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.Comentario;
import com.flavio.plotdash.model.Genero;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public abstract class DaoHistoria {
    public static String URL = MainActivity.URL_BASE+"DAOHistorias.php";
    public static String msj = "";


    public static boolean operarDatos(Historia obj, String opcion) {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        int[] re = {0};
        parametros.put("opcion", opcion);
        parametros.put("titulo", obj.getTitulo());
        parametros.put("descripcion", obj.getDescripcion());
        parametros.put("idGenero", obj.getIdGenero().getIdGenero());
        parametros.put("portada", obj.getPortada());
        parametros.put("fecha_creacion", obj.getFecha_creacion());
        parametros.put("idUsuario", obj.getIdUsuario().getIdUsuario());
        if (obj.getIdHistoria() > 0) {
            parametros.put("idHistoria", obj.getIdHistoria());
        }
        client.post(URL, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    msj = new String(responseBody);
                    re[0] = 1;
                } else {
                    msj = new String(responseBody);
                    re[0] = 0;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("---hola---" + new String(responseBody));
            }
        });
        return re[0] == 0;
    }

    public static boolean devolucionDatos(String opcion, ArrayList<Historia> array) {

        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        int[] re = {0};
        parametros.put("opcion", opcion);

        client.post(URL, parametros, new AsyncHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("HOLA---------------" + new String(responseBody));

                if (statusCode == 200) {
                    System.out.println(new String(responseBody));
                    obtenerList(new String(responseBody));
                    re[0] = 1;
                } else {
                    msj = new String(responseBody);
                    re[0] = 0;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(new String(responseBody));
            }
        });

        return re[0] == 0;
    }

    public static ArrayList<Historia> obtenerList(String response) {
        ArrayList<Historia> objetos=new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(response);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < jsonArray.length(); i++) {
                Historia historia = new Historia(
                        jsonArray.getJSONObject(i).getInt("idHistoria"),
                        jsonArray.getJSONObject(i).getString("titulo"),
                        jsonArray.getJSONObject(i).getString("descripcion"),
                        new Genero(jsonArray.getJSONObject(i).getInt("idGenero")),
                        jsonArray.getJSONObject(i).getString("portada"),
                        LocalDateTime.parse(jsonArray.getJSONObject(i).getString("fecha_creacion"), formatter),
                        new Usuario(jsonArray.getJSONObject(i).getInt("idUsuario")),
                        jsonArray.getJSONObject(i).getInt("vistas"),
                        jsonArray.getJSONObject(i).getInt("estado"),
                        jsonArray.getJSONObject(i).getDouble("calificacion")
                );
                if (jsonArray.getJSONObject(i).has("capitulos") && !jsonArray.getJSONObject(i).isNull("capitulos")) {
                    JSONArray capitulosArray = jsonArray.getJSONObject(i).getJSONArray("capitulos");
                    ArrayList<Capitulo> capitulos = new ArrayList<>();
                    for (int j = 0; j < capitulosArray.length(); j++) {
                        Capitulo capitulo = new Capitulo(
                                capitulosArray.getJSONObject(j).getInt("idCapitulo"),
                                historia,
                                LocalDateTime.parse(capitulosArray.getJSONObject(i).getString("fecha_creado"), formatter),
                                capitulosArray.getJSONObject(j).getString("titulo"),
                                capitulosArray.getJSONObject(j).getString("contenido"),
                                capitulosArray.getJSONObject(j).getInt("num")
                        );
                        capitulos.add(capitulo);
                    }
                    historia.setCapitulos(capitulos);
                }

                if (jsonArray.getJSONObject(i).has("comentarios") && !jsonArray.getJSONObject(i).isNull("comentarios")) {
                    JSONArray comentariosArray = jsonArray.getJSONObject(i).getJSONArray("comentarios");
                    ArrayList<Comentario> comentarios = new ArrayList<>();
                    for (int j = 0; j < comentariosArray.length(); j++) {
                        JSONArray usuarioArray = jsonArray.getJSONObject(j).getJSONArray("usuario");
                        Usuario usuario = new Usuario();
                        String pattern = "yyyy-MM-dd";
                        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                        for (int k = 0; k < jsonArray.length(); k++) {
                            usuario = new Usuario(
                                    usuarioArray.getJSONObject(k).getInt("idUsuario"),
                                    usuarioArray.getJSONObject(k).getString("foto"),
                                    usuarioArray.getJSONObject(k).getString("nombre"),
                                    usuarioArray.getJSONObject(k).getString("apellidos"),
                                    usuarioArray.getJSONObject(k).getString("sexo"),
                                    dateFormat.parse(usuarioArray.getJSONObject(k).getString("fecha_nacimiento")),
                                    usuarioArray.getJSONObject(k).getString("correo"),
                                    usuarioArray.getJSONObject(k).getString("usuario"),
                                    usuarioArray.getJSONObject(k).getString("clave")


                            );
                        }
                        Comentario comentario = new Comentario(
                                comentariosArray.getJSONObject(j).getInt("idComentario"),
                                usuario,
                                historia,
                                LocalDateTime.parse(comentariosArray.getJSONObject(i).getString("fecha"), formatter),
                                comentariosArray.getJSONObject(j).getString("comentario")
                        );
                        comentarios.add(comentario);
                    }
                    historia.setComentarios(comentarios);
                }
                objetos.add(historia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objetos;
    }
}
