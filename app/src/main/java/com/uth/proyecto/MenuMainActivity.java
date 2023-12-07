package com.uth.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.NavigableMap;

public class MenuMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);
        BottomNavigationView navView = findViewById(R.id.bottomnavigationView);

        navView.setItemIconTintList(null);
        Navegacion();
    }


    private void Navegacion(){

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_hostfragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navHostFragment.getNavController());

    }
    private void init(){

    }

}