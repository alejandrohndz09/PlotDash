package com.flavio.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public abstract class DaoCapitulo {
    public static String URL = MainActivity.URL_BASE+"DAOCapitulos.php";
    public static String msj = "";

    public static ArrayList<Capitulo> obtenerList(String response) {
        ArrayList<Capitulo> objetos=new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(response);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < jsonArray.length(); i++) {
                Capitulo p = new Capitulo(
                        jsonArray.getJSONObject(i).getInt("idCapitulo"),
                        new Historia(jsonArray.getJSONObject(i).getInt("idHistoria")),
                        LocalDateTime.parse(jsonArray.getJSONObject(i).getString("fecha_creado"), formatter),
                        jsonArray.getJSONObject(i).getString("titulo"),
                        jsonArray.getJSONObject(i).getString("contenido"),
                        jsonArray.getJSONObject(i).getInt("num")

                );
                objetos.add(p);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return objetos;
    }

    public static boolean operarDatos(Capitulo obj, String opcion) {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        int[] re = {0};
        parametros.put("opcion", opcion);
        parametros.put("idCapitulo", obj.getIdCapitulo());

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
                System.out.println("--------------holaaaa------------"+msj);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                msj = new String(responseBody);
                System.out.println("---hosla---"+msj+"-" + error.getMessage());
            }
        });
        return re[0] == 0;
    }
}
