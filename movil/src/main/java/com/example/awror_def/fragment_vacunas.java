package com.example.awror_def;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class fragment_vacunas extends Fragment {

    Spinner spinnerMascotas;
    RecyclerView rvVacunas;
    FloatingActionButton fabAgregarVacuna;
    VaccineAdapter adapter;
    ArrayList<Vaccine> listaVacunas = new ArrayList<>();
    ArrayList<Pet> listaMascotas = fragment_pet.listaMascotasGlobal;
    ApiRest api;
    int petIdSeleccionada = -1;
    EditText etNombre;
    EditText etFecha;
    EditText etProxima;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vacunas, container, false);

        spinnerMascotas = view.findViewById(R.id.spinnerMascotas);
        rvVacunas = view.findViewById(R.id.rvVacunas);
        fabAgregarVacuna = view.findViewById(R.id.fabAgregarVacuna);

        api = new ApiRest();

        rvVacunas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VaccineAdapter(listaVacunas);
        rvVacunas.setAdapter(adapter);

        cargarSpinnerMascotas();

        fabAgregarVacuna.setOnClickListener(v -> mostrarDialogoAgregarVacuna());

        adapter.setOnVaccineEditListener((vacuna, position) ->
                mostrarDialogoEditarVacuna(vacuna, position)
        );

        adapter.setOnVaccineDeleteListener((vacuna, position) ->
                mostrarDialogoEliminarVacuna(vacuna, position)
        );

        return view;
    }

    private void cargarSpinnerMascotas() {

        ArrayList<String> nombres = new ArrayList<>();
        for (Pet p : listaMascotas) {
            nombres.add(p.getNombre());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                nombres
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMascotas.setAdapter(spinnerAdapter);

        spinnerMascotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Pet mascotaSeleccionada = listaMascotas.get(position);
                petIdSeleccionada = mascotaSeleccionada.getId();

                api.obtenerVacunas(petIdSeleccionada, adapter, getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    private void mostrarDialogoAgregarVacuna() {

        if (petIdSeleccionada == -1) {
            Toast.makeText(getContext(), "Selecciona una mascota primero", Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_crear_vacuna, null);

        etNombre = dialogView.findViewById(R.id.etNombreVacuna);
        etFecha = dialogView.findViewById(R.id.etFechaVacuna);
        etProxima = dialogView.findViewById(R.id.etProximaVacuna);

        new AlertDialog.Builder(getContext())
                .setTitle("Agregar vacuna")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    String nombre = etNombre.getText().toString().trim();
                    String fecha = etFecha.getText().toString().trim();
                    String proxima = etProxima.getText().toString().trim();

                    if (nombre.isEmpty() || fecha.isEmpty()) {
                        Toast.makeText(getContext(), "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Vaccine v = new Vaccine();
                    v.setPetId(petIdSeleccionada);
                    v.setName(nombre);
                    v.setDateGiven(fecha);
                    v.setNextDate(proxima);
                    v.setNotes("");

                    listaVacunas.add(v);
                    adapter.notifyItemInserted(listaVacunas.size() - 1);

                    api.crearVacuna(v, getActivity());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditarVacuna(Vaccine vacuna, int position) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_crear_vacuna, null);

        EditText etNombre = dialogView.findViewById(R.id.etNombreVacuna);
        EditText etFecha = dialogView.findViewById(R.id.etFechaVacuna);
        EditText etProxima = dialogView.findViewById(R.id.etProximaVacuna);

        etNombre.setText(vacuna.getName());
        etFecha.setText(vacuna.getDateGiven());
        etProxima.setText(vacuna.getNextDate());

        new AlertDialog.Builder(getContext())
                .setTitle("Editar vacuna")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    vacuna.setName(etNombre.getText().toString().trim());
                    vacuna.setDateGiven(etFecha.getText().toString().trim());
                    vacuna.setNextDate(etProxima.getText().toString().trim());

                    api.actualizarVacuna(vacuna, getActivity());

                    adapter.notifyItemChanged(position);

                    Toast.makeText(getContext(), "Vacuna actualizada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEliminarVacuna(Vaccine vacuna, int position) {

        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar vacuna")
                .setMessage("¿Seguro que quieres eliminar esta vacuna?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    api.eliminarVacuna(vacuna.getId(), getActivity());

                    listaVacunas.remove(position);
                    adapter.notifyItemRemoved(position);

                    Toast.makeText(getContext(), "Vacuna eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
