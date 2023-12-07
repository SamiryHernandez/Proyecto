package com.uth.proyecto.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uth.proyecto.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment  implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Polyline routePolyline;
    private Marker currentLocationMarker;
    private float distanciaTotal = 0;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private TextView distanciaTextView,TdistanciaTextView;
    private boolean simulacionEnCurso = false;
    private List<LatLng> puntosSeleccionados = new ArrayList<>();
    public InicioFragment() {
        // Required empty public constructor
    }

    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        distanciaTextView = view.findViewById(R.id.distanceTextView);
        TdistanciaTextView = view.findViewById(R.id.TdistanceTextView);

        // Obtener el mapa programáticamente
        SupportMapFragment mapFragment = new SupportMapFragment();

        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment, "mapFragment")
                    .commit();
        } else {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("mapFragment");
        }

        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        // Botón de inicio de simulación
        Button startSimulationButton = view.findViewById(R.id.startSimulationButton);
        startSimulationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSimulacion();
            }
        });

        Button borrar = view.findViewById(R.id.Borrar);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                distanciaTotal = 0;
                distanciaTextView.setText("Distancia: 0.00 km");

                // Borrar el trazado antiguo en el mapa
                puntosSeleccionados.clear();

                PolylineOptions polylineOptions = new PolylineOptions()
                        .color(Color.argb(0,255,255,255))
                        .width(6);
                googleMap.addPolyline(polylineOptions);
                // Mostrar un mensaje indicando que se ha borrado
                Toast.makeText(requireContext(), "Distancia y trazado borrados", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("mapFragment");
        if (mapFragment != null) {
            getChildFragmentManager().beginTransaction().remove(mapFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Centrar el mapa en el punto A
        List<LatLng> routePoints = obtenerPuntosDeSimulacion();
        LatLng puntoA = routePoints.get(0);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoA, 15));

        // Agrega el listener para manejar los clics en el mapa
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (simulacionEnCurso) {
                    // Si la simulación está en curso, detenerla
                    detenerSimulacion();
                } else {
                    // Si no hay simulación en curso, agregar el punto al conjunto de puntos para la simulación
                    agregarPuntoSeleccionado(latLng);
                }
            }
        });
    }

    private void detenerSimulacion() {
        // Detener la simulación y realizar cualquier limpieza necesaria
        simulacionEnCurso = false;
        // ... (puedes agregar más acciones si es necesario)
    }

    private void agregarPuntoSeleccionado(LatLng punto) {
        // Agregar el punto al conjunto de puntos para la simulación
        puntosSeleccionados.add(punto);

        // Dibujar la línea entre los puntos seleccionados
        dibujarLineaEntrePuntos();

        // Si se han seleccionado dos puntos, iniciar la simulación
        if (puntosSeleccionados.size() == 2) {
            iniciarSimulacion();
        }
    }


    private void dibujarLineaEntrePuntos() {
        // Dibujar la línea entre los puntos seleccionados en el mapa
        if (puntosSeleccionados.size() == 2) {
            LatLng puntoA = puntosSeleccionados.get(0);
            LatLng puntoB = puntosSeleccionados.get(1);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(puntoA, puntoB)
                    .color(Color.BLUE)
                    .width(6);
            googleMap.addPolyline(polylineOptions);
        }
    }

    private void iniciarSimulacion() {
        // Verificar que haya al menos dos puntos seleccionados
        if (puntosSeleccionados.size() < 2) {
            Toast.makeText(requireContext(), "Seleccione dos puntos en el mapa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dibujar la ruta en el mapa entre los puntos seleccionados
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(puntosSeleccionados)
                .color(Color.BLUE)
                .width(5);
        routePolyline = googleMap.addPolyline(polylineOptions);

        // Centrar el mapa en el primer punto seleccionado
        LatLng puntoA = puntosSeleccionados.get(0);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(puntoA, 15));

        // Iniciar la simulación
        simularRecorrido(puntosSeleccionados);
    }

    private List<LatLng> obtenerPuntosDeSimulacion() {

        // Ejemplo de puntos de simulación
        List<LatLng> routePoints = new ArrayList<>();
        routePoints.add(new LatLng(37.4122754897, -122.134793859));  // PuntoA
        routePoints.add(new LatLng(37.3773622787 ,-122.063382726));  // PuntoB
        return routePoints;
    }


    private void simularRecorrido(List<LatLng> routePoints) {
        if (routePolyline != null) {
            routePolyline.remove();
        }

        // Verificar que haya al menos un punto en la lista antes de continuar
        if (routePoints.isEmpty()) {
            return;
        }

        for (int i = 0; i < routePoints.size(); i++) {
            final int index = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Verificar que el índice sea válido antes de acceder a la lista
                    if (index < routePoints.size()) {
                        LatLng point = routePoints.get(index);
                        calcularDistancia(point);
                        actualizarUbicacionSimulada(point);

                        // Agregar el punto a la Polyline
                        if (index > 0) {
                            LatLng previousPoint = routePoints.get(index - 1);
                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .add(previousPoint, point)
                                    .color(Color.BLUE)
                                    .width(5);
                            routePolyline = googleMap.addPolyline(polylineOptions);
                        }

                        // Si es el último punto, calcular la distancia total
                        if (index == routePoints.size() - 1) {
                            calcularDistanciaTotal();
                        }
                    }
                }
            }, i * 2000); // Simular cada 2 segundos
        }
    }
    private void actualizarUbicacionSimulada(LatLng point) {
        // Actualizar la ubicación en el mapa
        if (googleMap != null) {
            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }

            // Agregar un marcador en la nueva ubicación
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(point)
                    .title("Ubicación Actual");
            currentLocationMarker = googleMap.addMarker(markerOptions);

            googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));

        }
    }

    private void calcularDistancia(LatLng nuevoPunto) {
        if (currentLocationMarker != null) {
            Location locationAnterior = new Location("anterior");
            locationAnterior.setLatitude(currentLocationMarker.getPosition().latitude);
            locationAnterior.setLongitude(currentLocationMarker.getPosition().longitude);

            Location locationNuevo = new Location("nuevo");
            locationNuevo.setLatitude(nuevoPunto.latitude);
            locationNuevo.setLongitude(nuevoPunto.longitude);

            float distanciaEntrePuntos = locationAnterior.distanceTo(locationNuevo) / 1000; // Convertir a kilómetros

            distanciaTotal += distanciaEntrePuntos;

            // Mostrar la distancia total en un TextView
            String distanciaFormateada = decimalFormat.format(distanciaTotal);
            TdistanciaTextView.setText("Distancia Total: " + distanciaFormateada + " km");
        }
    }

    private void calcularDistanciaTotal() {
        // Calcular la distancia total entre los dos puntos seleccionados
        if (puntosSeleccionados.size() == 2) {
            LatLng puntoA = puntosSeleccionados.get(0);
            LatLng puntoB = puntosSeleccionados.get(1);

            Location locationA = new Location("puntoA");
            locationA.setLatitude(puntoA.latitude);
            locationA.setLongitude(puntoA.longitude);

            Location locationB = new Location("puntoB");
            locationB.setLatitude(puntoB.latitude);
            locationB.setLongitude(puntoB.longitude);

            float distanciaTotalEntrePuntos = locationA.distanceTo(locationB) / 1000; // Convertir a kilómetros

            // Mostrar la distancia total en un TextView o donde desees
            String distanciaFormateada = decimalFormat.format(distanciaTotalEntrePuntos);
            distanciaTextView.setText("Distancia: " + distanciaFormateada + " km");
        }
    }
}