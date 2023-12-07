package com.uth.proyecto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.uth.proyecto.R;
import com.uth.proyecto.adapters.AmigosAdapter;
import com.uth.proyecto.customs.Amigo;

import java.util.ArrayList;
import java.util.List;


public class AmigosFragment extends Fragment {

    private ListView lista;
    private AmigosAdapter amigosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);

        // Obtén la referencia de tu ListView
        lista = view.findViewById(R.id.Lista);

        // Crea una lista de amigos (puedes cargarla desde alguna fuente de datos)
        List<Amigo> amigosList = new ArrayList<>();
        amigosList.add(new Amigo(R.drawable.business, "Nombre1", "Apellido1", "País1"));
        amigosList.add(new Amigo(R.drawable.logo_del_proyecto, "Nombre2", "Apellido2", "País2"));
        // ... Agrega más amigos según sea necesario

        // Crea el adaptador personalizado y asigna la lista de amigos
        amigosAdapter = new AmigosAdapter(requireContext(), amigosList);

        // Configura el adaptador en la ListView
        lista.setAdapter(amigosAdapter);

        return view;
    }
}