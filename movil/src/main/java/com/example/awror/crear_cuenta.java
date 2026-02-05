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

public class crear_cuenta extends AppCompatActivity {

    Button btnCuenta;
    Button btnCrear;
    ApiRest api;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtCorreo;
    EditText txtPassw;
    EditText txtUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.crear_cuenta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCrear = findViewById(R.id.btnCrear);
        btnCuenta = findViewById(R.id.btnCuenta);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCorreo = findViewById(R.id.txtCo);
        txtPassw = findViewById(R.id.txtPassw);
        txtUsuario = findViewById(R.id.txtUsuario);
        api = new ApiRest();

        btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(crear_cuenta.this, inicio_sesion.class);
                startActivity(i);
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = txtUsuario.getText().toString();
                String real_name = txtNombre.getText().toString();
                String real_surname = txtApellido.getText().toString();
                String email = txtCorreo.getText().toString();
                String password = txtPassw.getText().toString();
                api.subirUsuario(user_name,real_name,real_surname,email,password);
                Intent i = new Intent(crear_cuenta.this, inicio_sesion.class);
                startActivity(i);
            }
        });
    }
}