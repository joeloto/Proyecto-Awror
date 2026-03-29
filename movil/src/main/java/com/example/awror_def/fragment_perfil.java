package com.example.awror_def;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_perfil extends Fragment {

    EditText etNombre;
    EditText etApellido;
    EditText etUsuario;
    EditText etEmail;
    EditText etPassword;
    Button btnActualizar;
    Button btnEliminar;
    Button btnCerrarSesion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        etNombre = view.findViewById(R.id.etNombrePerfil);
        etApellido = view.findViewById(R.id.etApellidoPerfil);
        etUsuario = view.findViewById(R.id.etUsuarioPerfil);
        etEmail = view.findViewById(R.id.etEmailPerfil);
        etPassword = view.findViewById(R.id.etPasswordPerfil);
        btnActualizar = view.findViewById(R.id.btnActualizarPerfil);
        btnEliminar = view.findViewById(R.id.btnEliminarPerfil);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        etNombre.setText(ApiRest.realName);
        etApellido.setText(ApiRest.realSurname);
        etUsuario.setText(ApiRest.userName);
        etEmail.setText(ApiRest.emailUser);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = etNombre.getText().toString();
                String apellido = etApellido.getText().toString();
                String usuario = etUsuario.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                ApiRest api = new ApiRest();
                api.updateUser(ApiRest.userId, usuario, nombre, apellido, email, password);
                Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Eliminar cuenta")
                        .setMessage("¿Seguro que quieres eliminar tu cuenta?")
                        .setPositiveButton("Sí", (dialog, which) -> {

                            ApiRest api = new ApiRest();
                            api.deleteUser(ApiRest.userId, getActivity());

                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        btnCerrarSesion.setOnClickListener(v -> {
            ApiRest.userId = 0;
            ApiRest.userName = null;
            ApiRest.realName = null;
            ApiRest.realSurname = null;
            ApiRest.emailUser = null;
            Intent intent = new Intent(getActivity(), inicio_sesion.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}