package com.flavio.dao;

import com.flavio.plotdash.model.Genero;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;

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
                Historia p = new Historia(
                        jsonArray.getJSONObject(i).getInt("idHistoria"),
                        jsonArray.getJSONObject(i).getString("titulo"),
                        jsonArray.getJSONObject(i).getString("descripcion"),
                        new Genero(jsonArray.getJSONObject(i).getInt("idGenero")),
                        jsonArray.getJSONObject(i).getString("portada"),
                        LocalDateTime.parse(jsonArray.getJSONObject(i).getString("fecha_creacion"), formatter),
                        new Usuario(jsonArray.getJSONObject(i).getInt("idUsuario")),
                        jsonArray.getJSONObject(i).getInt("vistas"),
                        jsonArray.getJSONObject(i).getDouble("calificacion")

                );
                objetos.add(p);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return objetos;
    }
}
