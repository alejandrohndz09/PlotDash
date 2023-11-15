package com.flavio.plotdash.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.flavio.dao.DaoBiblioteca;

import com.flavio.plotdash.R;

import com.flavio.plotdash.ui.adapter.AdapterBiblioteca;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BibliotecaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BibliotecaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    ListView rv_biblioteca;
    AdapterBiblioteca adaptador;

    View view;

    RecyclerView recyclerView;

    public BibliotecaFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BibliotecaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BibliotecaFragment newInstance(String param1, String param2) {
        BibliotecaFragment fragment = new BibliotecaFragment();
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
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_biblioteca, container, false);
        rv_biblioteca=view.findViewById(R.id.rv_biblioteca);
        llenarLista();
        return view;
    }

    private void llenarLista() {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();


        parametros.put("opcion", "buscar");

        RequestHandle post = client.post(DaoBiblioteca.URL, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    System.out.println("Sioiii"+new String(responseBody));
                    adaptador = new AdapterBiblioteca(view.getContext(), R.layout.adapter_biblioteca_list, DaoBiblioteca.obtenerList(new String(responseBody)));
                    rv_biblioteca.setAdapter((ListAdapter) adaptador);
                } else {
                    Toast.makeText(view.getContext(), "Error al encontrar registros", Toast.LENGTH_SHORT).show();
                }
                // progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        client.cancelAllRequests(true);


        //progressDialog.dismiss();
    }
}