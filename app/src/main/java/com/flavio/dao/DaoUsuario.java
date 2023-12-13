package com.flavio.dao;

import com.flavio.plotdash.model.Usuario;
import com.flavio.plotdash.ui.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public abstract class DaoUsuario {

    public static String URL = MainActivity.URL_BASE+"DAOUsuarios1.php";
    public static String msj = "";
    public static ArrayList<Usuario> obtenerList(String response) {
        ArrayList<Usuario> objetos=new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            for (int i = 0; i < jsonArray.length(); i++) {
                Usuario p = new Usuario(
                        jsonArray.getJSONObject(i).getInt("idUsuario"));
                objetos.add(p);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return objetos;
    }
    public static boolean operarDatos(Usuario usu, String opcion) {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = URL;
        int[] re = {0};
        parametros.put("opcion", opcion);
        parametros.put("usu", usu.getUsuario());
        parametros.put("email", usu.getCorreo());
        parametros.put("clave", usu.getClave());
        if (usu.getIdUsuario()>0) {
            parametros.put("id", usu.getIdUsuario());
        }
        client.post(url, parametros, new AsyncHttpResponseHandler() {
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

            }
        });
        return re[0] == 0;
    }

}
