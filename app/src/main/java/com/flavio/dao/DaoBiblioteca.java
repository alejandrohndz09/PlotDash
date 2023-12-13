package com.flavio.dao;

import com.flavio.plotdash.model.Biblioteca;

import com.flavio.plotdash.model.Capitulo;
import com.flavio.plotdash.model.DetalleBiblioteca;
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

public abstract class DaoBiblioteca {
    public static String URL = MainActivity.URL_BASE+"DAOBibliotecas.php";
    public static String msj = "";


    public static boolean operarDatos(Biblioteca obj, String opcion) {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        int[] re = {0};
        parametros.put("opcion", opcion);
        parametros.put("idUsuario", obj.getIdUsuario());


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

    public static ArrayList<Biblioteca> obtenerList(String response) {
        ArrayList<Biblioteca> objetos=new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(response);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < jsonArray.length(); i++) {
                Biblioteca b = new Biblioteca(
                        jsonArray.getJSONObject(i).getInt("idBiblioteca"),
                        jsonArray.getJSONObject(i).getInt("idUsuario"),
                        jsonArray.getJSONObject(i).getString("nombre"),
                        LocalDateTime.parse(jsonArray.getJSONObject(i).getString("fecha_creacion"), formatter),
                        jsonArray.getJSONObject(i).getInt("tipo")
                );
                if (jsonArray.getJSONObject(i).has("detalles") && !jsonArray.getJSONObject(i).isNull("detalles")) {
                    JSONArray detallesArray = jsonArray.getJSONObject(i).getJSONArray("detalles");
                    ArrayList<DetalleBiblioteca> detalles = new ArrayList<>();
                    for (int j = 0; j < detallesArray.length(); j++) {
                        DetalleBiblioteca detalle = new DetalleBiblioteca(
                                detallesArray.getJSONObject(j).getInt("idDetalle"),
                                detallesArray.getJSONObject(j).getInt("idBiblioteca"),
                                LocalDateTime.parse(detallesArray.getJSONObject(j).getString("fecha_creado"), formatter)
                        );
                        
                        if (detallesArray.getJSONObject(j).has("historia") && !detallesArray.getJSONObject(j).isNull("historia")) {
                            JSONArray HistoriaArray = detallesArray.getJSONObject(j).getJSONArray("historia");
                            ArrayList<Historia> historias = new ArrayList<>();
                            for (int k = 0; k < HistoriaArray.length(); k++) {
                                Historia historia = new Historia(
                                        HistoriaArray.getJSONObject(k).getInt("idHistoria"),
                                        HistoriaArray.getJSONObject(k).getString("titulo"),
                                        HistoriaArray.getJSONObject(k).getString("descripcion"),
                                        new Genero(HistoriaArray.getJSONObject(k).getInt("idGenero")),
                                        HistoriaArray.getJSONObject(k).getString("portada"),
                                        LocalDateTime.parse(HistoriaArray.getJSONObject(k).getString("fecha_creacion"), formatter),
                                        new Usuario(HistoriaArray.getJSONObject(k).getInt("idUsuario")),
                                        HistoriaArray.getJSONObject(k).getInt("vistas"),
                                        HistoriaArray.getJSONObject(k).getInt("estado"),
                                        HistoriaArray.getJSONObject(k).getDouble("calificacion")
                                );
                                if (HistoriaArray.getJSONObject(k).has("capitulos") && !HistoriaArray.getJSONObject(k).isNull("capitulos")) {
                                    JSONArray capitulosArray = HistoriaArray.getJSONObject(k).getJSONArray("capitulos");
                                    ArrayList<Capitulo> capitulos = new ArrayList<>();
                                    for (int l = 0; l < capitulosArray.length(); l++) {
                                        Capitulo capitulo = new Capitulo(
                                                capitulosArray.getJSONObject(l).getInt("idCapitulo"),
                                                historia,
                                                LocalDateTime.parse(capitulosArray.getJSONObject(l).getString("fecha_creado"), formatter),
                                                capitulosArray.getJSONObject(l).getString("titulo"),
                                                capitulosArray.getJSONObject(l).getString("contenido"),
                                                capitulosArray.getJSONObject(l).getInt("num")
                                        );
                                        capitulos.add(capitulo);
                                    }
                                    historia.setCapitulos(capitulos);
                                }
                                historias.add(historia);
                            }
                            detalle.setIdHistoria(historias.get(0));
                        }
                        detalles.add(detalle);
                    }
                    b.setDetalles(detalles);
                }
                objetos.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return objetos;
    }
}