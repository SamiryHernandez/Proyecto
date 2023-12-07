package com.uth.proyecto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uth.proyecto.R;
import com.uth.proyecto.customs.Solicitudes;

import java.util.List;

public class SolicitudesAdapter extends BaseAdapter {

    private Context context;
    private List<Solicitudes> SolicitudesList;

    public SolicitudesAdapter(Context context, List<Solicitudes> solicitudesList) {
        this.context = context;
        this.SolicitudesList = solicitudesList;
    }

    @Override
    public int getCount() {
        return SolicitudesList.size();
    }

    @Override
    public Object getItem(int position) {
        return SolicitudesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_solicitudes, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView nombreTextView = convertView.findViewById(R.id.nombreTextView);
        TextView apellidoTextView = convertView.findViewById(R.id.apellidoTextView);
        TextView paisTextView = convertView.findViewById(R.id.paisTextView);

        Solicitudes solicitud = SolicitudesList.get(position);

        // Aquí debes cargar la imagen en el ImageView usando alguna biblioteca como Glide o Picasso

        imageView.setImageResource(solicitud.getFoto());
        nombreTextView.setText(solicitud.getNombre());
        apellidoTextView.setText(solicitud.getApellido());
        paisTextView.setText(solicitud.getPais());

        return convertView;
    }
}