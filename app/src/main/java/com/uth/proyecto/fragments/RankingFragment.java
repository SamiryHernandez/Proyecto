package com.uth.proyecto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uth.proyecto.R;
import com.uth.proyecto.adapters.ListAdapter;
import com.uth.proyecto.classes.DatabaseHelper;
import com.uth.proyecto.classes.ListElement;

import java.util.List;

public class RankingFragment extends Fragment {

    private List<ListElement> elements;
    private DatabaseHelper dbHelper;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(requireContext());
        elements = dbHelper.getAllUsers();

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerLista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ListAdapter listAdapter = new ListAdapter(elements, requireContext());
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }



}