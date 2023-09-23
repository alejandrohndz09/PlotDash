package com.flavio.dao;

import com.flavio.model.Genero;
import com.flavio.model.Genero;
import com.flavio.model.Usuario;
import com.flavio.plotdash.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public abstract class DaoGenero {
    public static String URL = MainActivity.URL_BASE+"DAOGeneros.php";
    public static String msj = "";


    public static ArrayList<Genero> obtenerList(String response) {
        ArrayList<Genero> objetos=new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                Genero p = new Genero(
                        jsonArray.getJSONObject(i).getInt("idGenero"),
                        jsonArray.getJSONObject(i).getString("genero")
                );
                objetos.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return objetos;
    }
}
