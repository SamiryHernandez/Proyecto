package com.uth.proyecto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.uth.proyecto.R;
import com.uth.proyecto.adapters.ViewPagerAdapter;

public class FriendFragment extends Fragment {
    public FriendFragment() {
        // Required empty public constructor
    }


    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewpager);
        TabLayout tabLayout = view.findViewById(R.id.tab);

        // Crear el adaptador de fragmentos
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());

        // Añadir fragmentos al adaptador
        adapter.addFragment(new AmigosFragment(), "Amigos");
        adapter.addFragment(new SolicitudesFragment(), "Solicitudes");
        adapter.addFragment(new SugerenciasFragment(), "Sugerencias");

        // Configurar ViewPager2 con el adaptador
        viewPager.setAdapter(adapter);

        // Conectar el TabLayout y ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getTitle(position))
        ).attach();

        return view;
    }
}
