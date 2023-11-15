package com.flavio.plotdash.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flavio.dao.DaoHistoria;
import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.adapter.AdapterHistoriaCombo;
import com.flavio.plotdash.ui.util.Alert;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;



    public SettingFragment() {
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
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        vista = inflater.inflate(R.layout.fragment_setting, container, false);
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
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (error instanceof java.net.SocketTimeoutException) {
                    // Maneja el timeout aquí
                    Alert.show(vista.getContext(), "error", "Ha ocurrido un error, revisa tu conexión a internet", Toast.LENGTH_SHORT,
                            (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, vista.findViewById(R.id.toastCustom)));
                } else {
                    System.out.println(statusCode+':'+ error.getMessage()/*new String(responseBody)*/);
                }
            }
        });
    }


}