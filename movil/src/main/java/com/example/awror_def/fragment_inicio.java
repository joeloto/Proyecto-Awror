package com.example.awror_def;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class fragment_inicio extends Fragment {

    RecyclerView rvPublicaciones;
    FloatingActionButton fabCrearPublicacion;
    PostAdapter adapter;
    ApiRest api;
    Uri imagenSeleccionada;
    ImageView ivPreviewDialog;
    ArrayList<Post> listaPublicacionesGlobal = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        rvPublicaciones = view.findViewById(R.id.rvPosts);
        fabCrearPublicacion = view.findViewById(R.id.fabCrearPost);

        rvPublicaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PostAdapter(listaPublicacionesGlobal);
        adapter.setListener(new PostAdapter.PostListener() {
            @Override
            public void onDelete(Post post) {
                api.eliminarPost(post.getId(), getActivity());
                listaPublicacionesGlobal.remove(post);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onEdit(Post post) {
                mostrarDialogoEditar(post);
            }
        });
        rvPublicaciones.setAdapter(adapter);
        api = new ApiRest();
        api.obtenerPosts(ApiRest.userId, adapter, getActivity());
        fabCrearPublicacion.setOnClickListener(v -> mostrarDialogoCrearPost());
        return view;
    }

    private void mostrarDialogoCrearPost() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_crear_post, null);

        EditText etContenido = dialogView.findViewById(R.id.etContenido);
        Button btnGaleria = dialogView.findViewById(R.id.btnGaleria);
        ivPreviewDialog = dialogView.findViewById(R.id.ivPreview);

        btnGaleria.setOnClickListener(v -> abrirGaleria());

        new AlertDialog.Builder(getContext())
                .setTitle("Nueva publicación")
                .setView(dialogView)
                .setPositiveButton("Publicar", (dialog, which) -> {
                    String contenido = etContenido.getText().toString();

                    Post nuevo = new Post();
                    nuevo.setUser_id(ApiRest.userId);
                    nuevo.setUsername(ApiRest.userName);
                    nuevo.setContenido(contenido);

                    if (imagenSeleccionada != null) {
                        String imagenBase64 = api.convertirImagenBase64(imagenSeleccionada, getActivity());
                        nuevo.setImagen(imagenBase64);
                    }

                    listaPublicacionesGlobal.add(0, nuevo);
                    adapter.notifyItemInserted(0);
                    rvPublicaciones.scrollToPosition(0);

                    api.crearPost(ApiRest.userId, contenido, imagenSeleccionada, getActivity());

                    imagenSeleccionada = null;
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == getActivity().RESULT_OK && data != null) {
            imagenSeleccionada = data.getData();

            if (ivPreviewDialog != null) {
                ivPreviewDialog.setImageURI(imagenSeleccionada);
                ivPreviewDialog.setVisibility(View.VISIBLE);
            }
        }
    }

    private void mostrarDialogoEditar(Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_crear_post, null);

        EditText etContenido = view.findViewById(R.id.etContenido);
        etContenido.setText(post.getContenido());

        builder.setView(view)
                .setTitle("Editar publicación")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevoTexto = etContenido.getText().toString();
                    post.setContenido(nuevoTexto);
                    api.editarPost(post.getId(), nuevoTexto, getActivity());
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}