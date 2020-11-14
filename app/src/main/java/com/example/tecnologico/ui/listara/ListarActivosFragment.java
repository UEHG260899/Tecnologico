package com.example.tecnologico.ui.listara;

import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tecnologico.R;
import com.example.tecnologico.base.SQLite;

import java.io.File;
import java.util.ArrayList;

public class ListarActivosFragment extends Fragment {

    ArrayList<String> registros, imagenes;
    private ListarActivosViewModel listarActivosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listarActivosViewModel =
                new ViewModelProvider(this).get(ListarActivosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listaa, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        listarActivosViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        ListView list = root.findViewById(R.id.lvListaAct);
        SQLite sqLite = new SQLite(getContext());

        sqLite.abrirBase();

        Cursor cursor = sqLite.getRegistros("Activo");

        registros = sqLite.getAlumnos(cursor);
        imagenes = sqLite.getImagenes(cursor);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, registros);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alumno, null);
                ((TextView) dialogView.findViewById(R.id.tvNuevoAlumno)).setText(registros.get(position));
                ImageView ivImagen = dialogView.findViewById(R.id.ivFotoAlumno);
                cargaImagen(imagenes.get(position), ivImagen);
                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Alumno");
                dialogo.setView(dialogView);
                dialogo.setPositiveButton("Aceptar", null);
                dialogo.show();
            }
        });



        return root;
    }


    public void cargaImagen(String imagen, ImageView iv){
        try {
            File filePhoto = new File(imagen);
            Uri photUri = FileProvider.getUriForFile(getContext(), "com.example.tecnologico", filePhoto);
            iv.setImageURI(photUri);
        }catch(Exception ex){
            Toast.makeText(getContext(), "Ocurrio un error en la carga de la imagen", Toast.LENGTH_LONG).show();
            Log.d("Carga Imagen", "Error al cargar la imagen" + imagen + "\n Mensaje" + ex.getMessage() + "\n Causa: " + ex.getCause());
        }
    }
}