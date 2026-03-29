package com.example.awror_def;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class fragment_citas extends Fragment {

    RecyclerView rvCitas;
    FloatingActionButton fabAgregarCita;
    List<String> listaCitas = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_citas, container, false);

        rvCitas = view.findViewById(R.id.rvCitas);
        fabAgregarCita = view.findViewById(R.id.fabAgregarCita);

        rvCitas.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAgregarCita.setOnClickListener(v -> {
            listaCitas.add("Cita ejemplo - " + (listaCitas.size() + 1));
            Toast.makeText(getContext(), "Cita agregada", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}