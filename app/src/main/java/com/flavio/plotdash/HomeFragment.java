package com.flavio.plotdash;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flavio.dao.DaoHistoria;
import com.flavio.model.Historia;
import com.flavio.util.Alert;
import com.flavio.util.ComboHistoriaAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.security.auth.callback.Callback;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    RecyclerView rv_parent;
    ArrayList<Map<String, Object>> comboHistoriaList;
    ComboHistoriaAdapter comboHistoriaAdapter;
    ArrayList<Historia> listaH;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_home, container, false);
        rv_parent = vista.findViewById(R.id.rv_parent);
        comboHistoriaList = new ArrayList<>();
        rv_parent.setLayoutManager(new LinearLayoutManager(vista.getContext()));

        llenarCombo("masReciente", "Lo más nuevo");
        llenarCombo("masVistas", "Lo más popular");
        llenarCombo("buscarTodos", "Otros");
        return vista;
    }

    void llenarCombo(String opcion, String titulo) {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        parametros.put("opcion", opcion);

        client.post(DaoHistoria.URL, parametros, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (statusCode == 200) {
                    Map<String, Object> combo = new HashMap<>();

                    combo.put("titulo", titulo);
                    combo.put("historiaList", DaoHistoria.obtenerList(new String(responseBody)));
                    System.out.println(new String(responseBody));
                    comboHistoriaList.add(combo);
                    comboHistoriaAdapter = new ComboHistoriaAdapter(comboHistoriaList, vista.getContext());
                    rv_parent.setAdapter(comboHistoriaAdapter);
                    comboHistoriaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (error instanceof java.net.SocketTimeoutException) {
                    // Maneja el timeout aquí
                    Alert.show(vista.getContext(), "error", "Ha ocurrido un error, revisa tu conexión a internet", Toast.LENGTH_SHORT,
                            (ViewGroup) getLayoutInflater().inflate(R.layout.toast, vista.findViewById(R.id.toastCustom)));
                } else {
                    System.out.println(statusCode+':'+ error.getMessage()/*new String(responseBody)*/);
                }
            }
        });
    }


}