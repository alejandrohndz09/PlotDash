package com.flavio.dao;

import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.activity.MainActivity;

import org.json.JSONArray;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
}
