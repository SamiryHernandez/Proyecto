package com.uth.proyecto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.uth.proyecto.R;
import com.uth.proyecto.adapters.SolicitudesAdapter;
import com.uth.proyecto.customs.Solicitudes;

import java.util.ArrayList;
import java.util.List;


public class SolicitudesFragment extends Fragment {

    private ListView lista;
    private SolicitudesAdapter solicitudesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitudes, container, false);

        // Obtén la referencia de tu ListView
        lista = view.findViewById(R.id.lista);

        // Crea una lista de amigos (puedes cargarla desde alguna fuente de datos)
        List<Solicitudes> solicitudesList = new ArrayList<>();
        solicitudesList.add(new Solicitudes(R.drawable.business, "Nombre1", "Apellido1", "País1"));
        solicitudesList.add(new Solicitudes(R.drawable.logo_del_proyecto, "Nombre2", "Apellido2", "País2"));
        // ... Agrega más amigos según sea necesario

        // Crea el adaptador personalizado y asigna la lista de amigos
        solicitudesAdapter = new SolicitudesAdapter(requireContext(), solicitudesList);

        // Configura el adaptador en la ListView
        lista.setAdapter(solicitudesAdapter);

        return view;
    }
}