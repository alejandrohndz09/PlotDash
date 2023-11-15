package com.flavio.plotdash.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flavio.plotdash.model.Historia;
import com.flavio.plotdash.R;

import java.util.List;
import java.util.Map;

public class AdapterHistoriaCombo extends RecyclerView.Adapter<AdapterHistoriaCombo.ViewHolder> {


    List<Map<String, Object>> comboHistoriaList;
    Context context;


    public AdapterHistoriaCombo(List<Map<String, Object>> comboHistoriaList, Context context) {
        this.comboHistoriaList = comboHistoriaList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterHistoriaCombo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_historia_combo, null, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistoriaCombo.ViewHolder holder, int position) {

        String titulo = (String) comboHistoriaList.get(position).get("titulo");

        holder.title_rv.setText(titulo);
        if (titulo.equals("Otros")) {
            holder.rv_child.setLayoutManager(new GridLayoutManager(context, 3));
        } else {
            holder.rv_child.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        }


        AdapterHistoriaCard historiaAdapter = new AdapterHistoriaCard((List<Historia>) comboHistoriaList.get(position).get("historiaList"), context);
        holder.rv_child.setAdapter(historiaAdapter);
        historiaAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return comboHistoriaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rv_child;
        TextView title_rv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv_child = itemView.findViewById(R.id.rv_child);
            title_rv = itemView.findViewById(R.id.title_rv);
        }

    }


}
