package com.flavio.dao;

import com.flavio.plotdash.model.Genero;
import com.flavio.plotdash.ui.activity.MainActivity;

import org.json.JSONArray;

import java.util.ArrayList;

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
