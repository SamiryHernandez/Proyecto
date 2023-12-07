package com.uth.proyecto.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uth.proyecto.R;
import com.uth.proyecto.adapters.SugerenciasAdapter;
import com.uth.proyecto.classes.Utils;
import com.uth.proyecto.config.ApiConfig;
import com.uth.proyecto.customs.Sugerencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SugerenciasFragment extends Fragment {

    private ListView lista;
    private SugerenciasAdapter sugerenciasAdapter;
    private Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sugerencias, container, false);

        utils = new Utils();

        // Obtén la referencia de tu ListView
        lista = view.findViewById(R.id.lista);

        List<Sugerencias> sugerenciasList = new ArrayList<>();
        sugerenciasList.add(new Sugerencias(R.drawable.business, "Nombre1", "Apellido1", "País1"));
        sugerenciasList.add(new Sugerencias(R.drawable.logo_del_proyecto, "Nombre2", "Apellido2", "País2"));

        // Verifica que utils no sea nulo antes de llamar al método getToken
      /*  if (utils != null) {
            String token = utils.getToken(requireContext());

            // Resto del código...
            sugerenciasAdapter = new SugerenciasAdapter(requireContext(), new ArrayList<>());

            // Configura el adaptador en la ListView
            lista.setAdapter(sugerenciasAdapter);

            // Realiza la solicitud para obtener amigos potenciales
            obtenerAmigosPotenciales();
        } else {
            Log.e("SugerenciasFragment", "El objeto Utils es nulo.");
        } */


        sugerenciasAdapter = new SugerenciasAdapter(requireContext(), sugerenciasList);
        lista.setAdapter(sugerenciasAdapter);

        return view;
    }

 /*   private void obtenerAmigosPotenciales() {
        // Obtener la URL del endpoint para amigos potenciales
        String url = ApiConfig.endpointPotencial;

        // Crear una solicitud JSON para obtener amigos potenciales
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Manejar la respuesta JSON
                        JSONArray potencialFriendsArray = response.getJSONArray("data");
                        List<Sugerencias> potencialFriendsList = new ArrayList<>();

                        for (int i = 0; i < potencialFriendsArray.length(); i++) {
                            JSONObject friendObject = potencialFriendsArray.getJSONObject(i);

                            // Parsear la información del amigo potencial
                            int id = friendObject.getInt("id");
                            String firstName = friendObject.getString("first_name");
                            String lastName = friendObject.getString("last_name");
                            String photo = friendObject.getString("photo");

                            // Crear un objeto Sugerencias y agregarlo a la lista
                            potencialFriendsList.add(new Sugerencias(id, firstName, lastName, photo));
                        }

                        // Actualizar el adaptador con la lista de amigos potenciales
                        sugerenciasAdapter.actualizarLista(potencialFriendsList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Manejar errores de la solicitud
                    Log.e("SolicitudError", "Error al obtener amigos potenciales: " + error.getMessage());
                });

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);
    }*/
}