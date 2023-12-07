package com.uth.proyecto.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uth.proyecto.models.CountryResponseModel;

import java.util.List;

public class CountryAdapter extends ArrayAdapter<CountryResponseModel.Country> {
    public CountryAdapter(Context context, List<CountryResponseModel.Country> countries) {
        super(context, android.R.layout.simple_dropdown_item_1line, countries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getName());

        return view;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getName());
        return view;
    }

    @Override
    public long getItemId(int position) {
        // Devuelve el ID asociado al objeto en la posición dada.
        return getItem(position).getId();
    }
}
