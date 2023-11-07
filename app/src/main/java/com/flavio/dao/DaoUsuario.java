package com.flavio.dao;

import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.activity.MainActivity;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public abstract class DaoUsuario {
    public static String URL = MainActivity.URL_BASE+"DAOUsuarios.php";
    public static String msj = "";
    public static ArrayList<Usuario> obtenerList(String response) {
        ArrayList<Usuario> objetos=new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(response);
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            for (int i = 0; i < jsonArray.length(); i++) {
                Usuario p = new Usuario(
                        jsonArray.getJSONObject(i).getInt("idUsuario"),
                        jsonArray.getJSONObject(i).getString("foto"),
                        jsonArray.getJSONObject(i).getString("nombre"),
                        jsonArray.getJSONObject(i).getString("apellidos"),
                        jsonArray.getJSONObject(i).getString("sexo"),
                        dateFormat.parse(jsonArray.getJSONObject(i).getString("fecha_nacimiento")),
                        jsonArray.getJSONObject(i).getString("correo"),
                        jsonArray.getJSONObject(i).getString("usuario"),
                        jsonArray.getJSONObject(i).getString("clave")

                );
                objetos.add(p);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return objetos;
    }
}
