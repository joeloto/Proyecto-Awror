package com.example.awror;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class inicio_sesion extends AppCompatActivity {

    Button btnEntrar;
    Button btnNoCuenta;
    EditText txtCorreo;
    EditText txtPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inicio_sesion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnEntrar = findViewById(R.id.btnEntrar);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtPassw = findViewById(R.id.txtPassw);
        btnNoCuenta = findViewById(R.id.btnNoCuenta);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(inicio_sesion.this, principal.class);
                startActivity(i);
            }
        });

        btnNoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(inicio_sesion.this, crear_cuenta.class);
                startActivity(i);
            }
        });
    }


}