package com.example.awror;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class principal extends AppCompatActivity {

    ArrayList<Publicacion> publicaciones;
    RecyclerView rv;
    PublicacionAdapter adapter;
    BottomNavigationView btnv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        publicaciones = new ArrayList<>();
        adapter = new PublicacionAdapter(publicaciones);
        rv = findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager((this)));
        rv.setAdapter(adapter);

        btnv = findViewById(R.id.bottomNavigationView);
        btnv.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.publicar) {
                Publicacion p = new Publicacion("joeloto", 0, 0, "", R.drawable.awror5);
                adapter.publicar(p);
                return true;
            }
            else if (id == R.id.principal) {
                //adapter.publicar(new Publicacion("", 0, 0, "", 0));
                return true;
            }
            else if (id == R.id.perfil){
                Intent i = new Intent(principal.this,perfil.class);

                startActivity(i);
            }
            else {

            }
            return false;
        });
    }
}