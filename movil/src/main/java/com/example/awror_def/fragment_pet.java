package com.example.awror_def;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class fragment_pet extends Fragment {

    RecyclerView rvMascotas;
    FloatingActionButton fabAñadirMascota;
    PetAdapter adapter;
    ApiRest api;
    Uri imagenSeleccionada;
    ImageView ivPreviewDialog;

    static ArrayList<Pet> listaMascotasGlobal = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pet, container, false);

        rvMascotas = view.findViewById(R.id.rvMascotas);
        fabAñadirMascota = view.findViewById(R.id.fabAñadirMascota);

        rvMascotas.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PetAdapter(listaMascotasGlobal);
        rvMascotas.setAdapter(adapter);

        api = new ApiRest();

        if (listaMascotasGlobal.isEmpty()) {
            api.obtenerMascotas(ApiRest.userId, adapter, getActivity());
        }

        adapter.setOnPetDeleteListener((pet, position) -> {

            new AlertDialog.Builder(getContext())
                    .setTitle("Eliminar mascota")
                    .setMessage("¿Seguro que quieres eliminar esta mascota?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        api.eliminarMascota(pet.getId(), getActivity());

                        listaMascotasGlobal.remove(position);
                        adapter.notifyItemRemoved(position);

                        Toast.makeText(getContext(), "Mascota eliminada", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        adapter.setOnPetUpdateListener((pet, position) ->
                mostrarDialogoActualizarMascota(pet, position)
        );

        fabAñadirMascota.setOnClickListener(v -> mostrarDialogoAnadirMascota());

        return view;
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 102);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102 && resultCode == getActivity().RESULT_OK && data != null) {
            imagenSeleccionada = data.getData();

            if (ivPreviewDialog != null) {
                ivPreviewDialog.setImageURI(imagenSeleccionada);
                ivPreviewDialog.setVisibility(View.VISIBLE);
            }
        }
    }

    private void mostrarDialogoAnadirMascota() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_crear_pet, null);

        EditText etNombre = dialogView.findViewById(R.id.etNombreMascota);
        EditText etTipo = dialogView.findViewById(R.id.etTipoAnimal);
        Button btnGaleria = dialogView.findViewById(R.id.btnGaleriaMascota);
        ivPreviewDialog = dialogView.findViewById(R.id.ivPreviewMascota);

        btnGaleria.setOnClickListener(v -> abrirGaleria());

        new AlertDialog.Builder(getContext())
                .setTitle("Añadir mascota")
                .setView(dialogView)
                .setPositiveButton("Añadir", (dialog, which) -> {

                    String nombre = etNombre.getText().toString().trim();
                    String tipo = etTipo.getText().toString().trim();

                    if (nombre.isEmpty() || tipo.isEmpty()) {
                        Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (imagenSeleccionada == null) {
                        Toast.makeText(getContext(), "Selecciona una foto", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String imagenBase64 = api.convertirImagenBase64(imagenSeleccionada, getActivity());

                    Pet nuevaMascota = new Pet();
                    nuevaMascota.setUser_id(ApiRest.userId);
                    nuevaMascota.setNombre(nombre);
                    nuevaMascota.setTipo("Animal: " + tipo);
                    nuevaMascota.setImagen(imagenBase64);

                    listaMascotasGlobal.add(0, nuevaMascota);
                    adapter.notifyItemInserted(0);
                    rvMascotas.scrollToPosition(0);
                    api.crearMascota(ApiRest.userId, nombre, tipo, imagenSeleccionada, getActivity());
                    imagenSeleccionada = null;
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoActualizarMascota(Pet mascota, int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_crear_pet, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombreMascota);
        EditText etTipo = dialogView.findViewById(R.id.etTipoAnimal);
        Button btnGaleria = dialogView.findViewById(R.id.btnGaleriaMascota);
        ivPreviewDialog = dialogView.findViewById(R.id.ivPreviewMascota);
        etNombre.setText(mascota.getNombre());
        etTipo.setText(mascota.getTipo());
        if (mascota.getImagen() != null && !mascota.getImagen().isEmpty()) {
            byte[] decoded = android.util.Base64.decode(mascota.getImagen(), android.util.Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            ivPreviewDialog.setImageBitmap(bitmap);
            ivPreviewDialog.setVisibility(View.VISIBLE);
        }
        btnGaleria.setOnClickListener(v -> abrirGaleria());
        new AlertDialog.Builder(getContext())
                .setTitle("Actualizar mascota")
                .setView(dialogView)
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    String nuevoNombre = etNombre.getText().toString().trim();
                    String nuevoTipo = etTipo.getText().toString().trim();
                    if (nuevoNombre.isEmpty() || nuevoTipo.isEmpty()) {
                        Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mascota.setNombre(nuevoNombre);
                    mascota.setTipo(nuevoTipo);
                    if (imagenSeleccionada != null) {
                        String nuevaImagenBase64 = api.convertirImagenBase64(imagenSeleccionada, getActivity());
                        mascota.setImagen(nuevaImagenBase64);
                    }
                    api.actualizarMascota(mascota, getActivity());
                    adapter.notifyItemChanged(position);
                    imagenSeleccionada = null;
                    Toast.makeText(getContext(), "Mascota actualizada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
