package com.flavio.plotdash.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flavio.plotdash.R;
import com.flavio.plotdash.model.Historia;

import java.util.ArrayList;
import java.util.Collections;

public class AdaptadorHistoriaList extends ArrayAdapter<Object> {

    public AdaptadorHistoriaList(@NonNull Context context, int resource, ArrayList<Historia> objects) {
        super(context, resource, Collections.singletonList(objects));
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Object persona = getItem(position);

        //Recuperar la vista
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_historia_list,parent,false);
        }
        //Colocar datos en los campos
        TextView nombre = convertView.findViewById(R.id.nombre);
        TextView apellido = convertView.findViewById(R.id.apellido);
        TextView sexo_edad = convertView.findViewById(R.id.sexo_edad);
        TextView telefono = convertView.findViewById(R.id.tel);
        Button btnEditar =convertView.findViewById(R.id.btnEdit);

        nombre.setText(((Historia) persona).getTitulo());
        sexo_edad.setText(((Historia) persona).getIdGenero().getGenero());
       // sexo_edad.setText(((Historia) persona).getSexo()+", "+((Historia) persona).getEdad()+" años");
        //telefono.setText(((Historia) persona).getTelefono());
        View finalConvertView = convertView;
       /* btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.opcion="modificar";

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("contacto",(Historia) persona);
                getContext().startActivity(intent);
            }
        });*/

        return convertView;
    }
}
