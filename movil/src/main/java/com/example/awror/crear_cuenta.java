package com.example.awror;

import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
=======
>>>>>>> 4828f2f50272f2b8459f3ab68eed1074009fb916

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class crear_cuenta extends AppCompatActivity {

<<<<<<< HEAD
    Button btnCuenta;
    Button btnCrear;
    ApiRest api;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtCorreo;
    EditText txtPassw;
=======
>>>>>>> 4828f2f50272f2b8459f3ab68eed1074009fb916
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
<<<<<<< HEAD

        btnCrear = findViewById(R.id.btnCrear);
        btnCuenta = findViewById(R.id.btnCuenta);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtPassw = findViewById(R.id.txtPassw);
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

                Intent i = new Intent(crear_cuenta.this, principal.class);
                startActivity(i);
            }
        });


=======
>>>>>>> 4828f2f50272f2b8459f3ab68eed1074009fb916
    }
}