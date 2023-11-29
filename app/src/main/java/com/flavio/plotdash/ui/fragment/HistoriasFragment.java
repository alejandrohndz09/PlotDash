package com.flavio.plotdash.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.flavio.dao.DaoHistoria;
import com.flavio.plotdash.ui.activity.MainActivity;
import com.flavio.plotdash.ui.adapter.AdaptadorHistoriaList;
import com.flavio.plotdash.ui.activity.FormHistoriaActivity;
import com.flavio.plotdash.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoriasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoriasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btnAddHis;
    ListView hist;
    AdaptadorHistoriaList adaptador;

    SwipeRefreshLayout srVista;
    View view;

    public HistoriasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoriasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoriasFragment newInstance(String param1, String param2) {
        HistoriasFragment fragment = new HistoriasFragment();
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
        view = inflater.inflate(R.layout.fragment_historias, container, false);
        btnAddHis = view.findViewById(R.id.btnAddHis);
        hist = view.findViewById(R.id.hist);
        //srVista=view.findViewById(R.id.srVista);
        llenarLista();


        btnAddHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FormHistoriaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
       registerForContextMenu(hist);
        hist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  showPopupMenu(view);
                return false;
            }
        });
        return view;
    }

  /*  private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.mis_historias_menu, popupMenu.getMenu()); // Define tu archivo de menú emergente aquí

        // Define el escuchador de clic para los elementos del menú emergente
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option1:
                        // Maneja la opción 1
                        return true;
                    case R.id.option2:
                        // Maneja la opción 2
                        return true;
                    // Agrega más casos para otras opciones si es necesario
                    default:
                        return false;
                }
                return false;
            }
        });
        popupMenu.show();
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        llenarLista();    }

    private void llenarLista() {
        RequestParams parametros = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();


        parametros.put("opcion", "buscarPorUsuario");
        parametros.put("idUsuario", MainActivity.usuario.getIdUsuario());

        RequestHandle post = client.post(DaoHistoria.URL, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    adaptador = new AdaptadorHistoriaList(view.getContext(), R.layout.adapter_historia_list, DaoHistoria.obtenerList(new String(responseBody)));
                    //adapter=new ArrayAdapter<Persona>(getBaseContext(), android.R.layout.simple_list_item_1,personas);
                    hist.setAdapter(adaptador);
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