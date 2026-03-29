package com.example.awror_def;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class crear_cuenta extends AppCompatActivity {

    EditText etNombre;
    EditText etApellido;
    EditText etUsuario;
    EditText etEmail;
    EditText etPassword;
    Button btnCrearCuenta;
    Button btnYaTengoCuenta;
    ApiRest api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_cuenta);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etUsuario = findViewById(R.id.etUsuario);
        etEmail = findViewById(R.id.etEmailRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);

        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnYaTengoCuenta = findViewById(R.id.btnYaTengoCuenta);

        api = new ApiRest();

        btnCrearCuenta.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String usuario = etUsuario.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() ||
                    email.isEmpty() || password.isEmpty()) {

                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            api.subirUsuario(usuario, nombre, apellido, email, password, this);

            Toast.makeText(this, "Creando usuario...", Toast.LENGTH_SHORT).show();
        });

        btnYaTengoCuenta.setOnClickListener(v -> {
            startActivity(new Intent(this, inicio_sesion.class));
        });
    }
}
