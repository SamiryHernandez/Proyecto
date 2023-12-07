package com.uth.proyecto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uth.proyecto.R;
import com.uth.proyecto.customs.Sugerencias;

import java.util.List;

public class SugerenciasAdapter extends BaseAdapter {

    private Context context;
    private List<Sugerencias> SugerenciasList;

    public SugerenciasAdapter(Context context, List<Sugerencias> sugerenciasList) {
        this.context = context;
        this.SugerenciasList = sugerenciasList;
    }

    @Override
    public int getCount() {
        return SugerenciasList.size();
    }

    @Override
    public Object getItem(int position) {
        return SugerenciasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sugerencias, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView nombreTextView = convertView.findViewById(R.id.nombreTextView);
        TextView apellidoTextView = convertView.findViewById(R.id.apellidoTextView);
        TextView paisTextView = convertView.findViewById(R.id.paisTextView);

        Sugerencias sugerencias = SugerenciasList.get(position);

        // Aquí debes cargar la imagen en el ImageView usando alguna biblioteca como Glide o Picasso

        imageView.setImageResource(sugerencias.getFoto());
        nombreTextView.setText(sugerencias.getNombre());
        apellidoTextView.setText(sugerencias.getApellido());
        paisTextView.setText(sugerencias.getPais());

        return convertView;

    }

    public void actualizarLista(List<Sugerencias> nuevaLista) {
        // Limpiar la lista actual
        SugerenciasList.clear();

        // Agregar los nuevos elementos a la lista
        SugerenciasList.addAll(nuevaLista);

        // Notificar al adaptador que los datos han cambiado
        notifyDataSetChanged();
    }

}
