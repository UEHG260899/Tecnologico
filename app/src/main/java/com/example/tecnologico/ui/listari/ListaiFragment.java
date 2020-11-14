package com.example.tecnologico.ui.listari;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.example.tecnologico.R;
import com.example.tecnologico.base.SQLite;

import java.io.File;
import java.util.ArrayList;

public class ListaiFragment extends Fragment {

    ArrayList<String> registros, imagenes;
    ArrayList<Integer> ids;
    private ListaiViewModel mViewModel;

    public static ListaiFragment newInstance() {
        return new ListaiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listai, container, false);


        ListView list = root.findViewById(R.id.lvListaIn);
        SQLite sqLite = new SQLite(getContext());

        sqLite.abrirBase();

        Cursor cursor = sqLite.getRegistros("Inactivo");

        ids = sqLite.getID(cursor);
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
                dialogo.setNegativeButton("Dar de alta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        altaAlumno(ids.get(position), sqLite, root);
                    }
                });
                dialogo.show();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListaiViewModel.class);
        // TODO: Use the ViewModel
    }


    private void altaAlumno(int noctrl, SQLite sqLite, View root){
        String resultado = sqLite.updateActivo(noctrl);
        Toast.makeText(getContext(), resultado, Toast.LENGTH_LONG).show();
        //se vuelve a cargar el adaptador
        ListView list = root.findViewById(R.id.lvListaIn);
        Cursor cursor = sqLite.getRegistros("Inactivo");
        registros = sqLite.getAlumnos(cursor);
        imagenes = sqLite.getImagenes(cursor);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, registros);
        list.setAdapter(adapter);
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