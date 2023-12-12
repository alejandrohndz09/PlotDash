package com.flavio.plotdash.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.flavio.dao.DaoHistoria;
import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.ui.activity.MainActivity;
import com.flavio.plotdash.ui.adapter.AdaptadorHistoriaList;
import com.flavio.plotdash.ui.activity.FormHistoriaActivity;
import com.flavio.plotdash.R;
import com.flavio.plotdash.ui.util.Alert;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
                showPopupMenu(view,(Historia) adapterView.getItemAtPosition(i));
                return false;
            }
        });

        return view;
    }

    private void showPopupMenu(View view, Historia historia) {

        // Crear una instancia de PopupMenu
        PopupMenu popupMenu = new PopupMenu(getContext(), view);

        // Inflar el menú desde un recurso XML
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.mis_historias_menu, popupMenu.getMenu());

        // Obtener referencia al elemento de menú por su ID
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.opPublicar);

        // Cambiar dinámicamente el título del elemento de menú
        menuItem.setTitle(historia.getEstado()==0?"Publicar":"Bajar Publicación");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Lógica para manejar clics en elementos del menú
                switch (item.getItemId()) {
                    case R.id.opBorrar:
                        Dialog dialog = new Dialog(view.getContext());
                        dialog.setContentView(R.layout.util_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView msjD = dialog.findViewById(R.id.msjD);
                        ImageView iconD = dialog.findViewById(R.id.iconDialog);
                        Button btnPos = dialog.findViewById(R.id.btnPos);
                        Button btnNeg = dialog.findViewById(R.id.btnNeg);
                        msjD.setText("Esta a punto de mover a papelera esta " +
                                "historia ¿Desea continuar?");
                        iconD.setImageResource(R.drawable.outline_warning_amber_24);
                        btnPos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                historia.setEstado(2);
                                if( DaoHistoria.operarDatos(historia,"registrar")){
                                    Alert.show(getContext(), "info", "Se ha removido la historia.", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, view.findViewById(R.id.toastCustom)));
                                    llenarLista();
                                }else{
                                    Alert.show(getContext(), "error", "Ha ocurrido un error.", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, view.findViewById(R.id.toastCustom)));
                                }
                                dialog.dismiss();
                            }

                        });
                        btnNeg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                        return true;
                    case R.id.opPublicar:
                        String tipoIcono;
                        if (historia.getEstado()==0){
                            historia.setEstado(1);
                            tipoIcono="exito";
                        }else {
                            historia.setEstado(0);
                            tipoIcono="info";
                        }
                       if( DaoHistoria.operarDatos(historia,"registrar")){
                           Alert.show(getContext(), tipoIcono, "Estado de historia cambiado exitosamente", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, view.findViewById(R.id.toastCustom)));
                            llenarLista();
                       }else{
                           Alert.show(getContext(), "error", "Ha ocurrido un error", Toast.LENGTH_SHORT, (ViewGroup) getLayoutInflater().inflate(R.layout.util_toast, view.findViewById(R.id.toastCustom)));
                       }
                        return true;
                }
                return true;
            }
        });

        // Mostrar el menú emergente
        popupMenu.show();
    }
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