package com.example.awror_def;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class inicio_sesion extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btnCrearCuenta;
    ApiRest api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        api = new ApiRest();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            api.loginUser(email, password);

            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

            int codigo = api.getLoginCodigo();

            if (codigo == 200){
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (codigo == 401){
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

        btnCrearCuenta.setOnClickListener(v -> {
            startActivity(new Intent(this, crear_cuenta.class));
        });
    }
}