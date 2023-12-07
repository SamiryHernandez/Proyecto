package com.uth.proyecto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uth.proyecto.R;
import com.uth.proyecto.customs.Amigo;

import java.util.List;

public class AmigosAdapter extends BaseAdapter {

    private Context context;
    private List<Amigo> amigosList;

    public AmigosAdapter(Context context, List<Amigo> amigosList) {
        this.context = context;
        this.amigosList = amigosList;
    }

    @Override
    public int getCount() {
        return amigosList.size();
    }

    @Override
    public Object getItem(int position) {
        return amigosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_amigo, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView nombreTextView = convertView.findViewById(R.id.nombreTextView);
        TextView apellidoTextView = convertView.findViewById(R.id.apellidoTextView);
        TextView paisTextView = convertView.findViewById(R.id.paisTextView);

        Amigo amigo = amigosList.get(position);

        // Aquí debes cargar la imagen en el ImageView usando alguna biblioteca como Glide o Picasso

        imageView.setImageResource(amigo.getFoto());
        nombreTextView.setText(amigo.getNombre());
        apellidoTextView.setText(amigo.getApellido());
        paisTextView.setText(amigo.getPais());

        return convertView;
    }
}