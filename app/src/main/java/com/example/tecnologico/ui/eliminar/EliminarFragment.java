package com.example.tecnologico.ui.eliminar;

import androidx.core.content.FileProvider;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tecnologico.R;
import com.example.tecnologico.base.SQLite;

import java.io.File;

public class EliminarFragment extends Fragment implements View.OnClickListener {

    private Button btnBuscar, btnLimpiar, btnBaja, btnEliminar;
    private EditText etNoctrl;
    private TextView tvNombre, tvEdad, tvSexo, tvFecha, tvCarrera, tvEstatus;
    private ImageView ivFoto;
    private EliminarViewModel mViewModel;

    public static String noctrl, n, e, s, f, c , est, img;
    static int bnd = 0, idp;
    public SQLite sqLite;

    public static EliminarFragment newInstance() {
        return new EliminarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eliminar, container, false);

        sqLite = new SQLite(getContext());
        componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EliminarViewModel.class);
        // TODO: Use the ViewModel
    }

    private void componentes(View root){
        botonesComponentes(root);
        editTextComponentes(root);
        tvComponentes(root);
    }


    private void botonesComponentes(View root){
        btnBaja = root.findViewById(R.id.btnBaja);
        btnEliminar = root.findViewById(R.id.btnEliminar);
        btnBuscar = root.findViewById(R.id.btnBuscarel);
        btnLimpiar = root.findViewById(R.id.btnLimpiarel);
        ivFoto = root.findViewById(R.id.ivFotoEl);

        btnLimpiar.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnBaja.setOnClickListener(this);
    }

    private void editTextComponentes(View root){
        etNoctrl = root.findViewById(R.id.tIETNoctrlel);
    }

    private void tvComponentes(View root){
        tvCarrera = root.findViewById(R.id.tvCarrerael);
        tvEdad = root.findViewById(R.id.tvEdadel);
        tvEstatus = root.findViewById(R.id.tvStatusel);
        tvFecha = root.findViewById(R.id.tvFechael);
        tvNombre = root.findViewById(R.id.tvNombreel);
        tvSexo = root.findViewById(R.id.tvSexoel);
    }

    private void limpiar(){
        etNoctrl.setText("");
        tvSexo.setText("Sexo: ");
        tvCarrera.setText("Carrera: ");
        tvEstatus.setText("Estatus: ");
        tvFecha.setText("Fecha de Inscripción: ");
        tvNombre.setText("Nombre del Alumno: ");
        tvEdad.setText("Edad: ");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);
        btnBuscar.setEnabled(true);
        etNoctrl.setEnabled(true);
        bnd = 0;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLimpiarel:
                limpiar();
                break;
            case R.id.btnBuscarel:
                if(etNoctrl.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Favor de introducir un criterio de busqueda", Toast.LENGTH_LONG).show();
                    bnd = 0;
                }

                sqLite.abrirBase();

                idp = Integer.parseInt(etNoctrl.getText().toString());
                if(sqLite.getValor(idp).getCount() == 1){
                    Cursor cursor = sqLite.getValor(idp);
                    btnBuscar.setEnabled(false);
                    etNoctrl.setEnabled(false);
                    if(cursor.moveToFirst()){
                        do{
                            noctrl = etNoctrl.getText().toString();
                            n = cursor.getString(1);
                            e = cursor.getString(2);
                            s = cursor.getString(3);
                            f = cursor.getString(4);
                            c = cursor.getString(5);
                            est = cursor.getString(6);
                            img = cursor.getString(7);
                        }while(cursor.moveToNext());
                    }

                    tvSexo.setText("Sexo: " + s);
                    tvCarrera.setText("Carrera: " + c);
                    tvEstatus.setText("Estatus: " + est);
                    tvFecha.setText("Fecha de Inscripción: " + f);
                    tvNombre.setText("Nombre del Alumno: " + n);
                    tvEdad.setText("Edad: " + e);
                    cargaImagen(img, ivFoto);

                    bnd = 1;
                }else{
                    Toast.makeText(getContext(), "No hay registros con ese numero de control", Toast.LENGTH_LONG).show();
                    bnd = 0;
                }

                sqLite.cerrarBase();
                break;
            case R.id.btnEliminar:
                if(bnd == 1){
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alumno, null);
                    ((TextView) dialogView.findViewById(R.id.tvNuevoAlumno)).setText("¿Desea eliminar el registro de forma permanente?\n" +
                            "NOCTROL: [" + noctrl + "]\n" +
                            "NOMBRE: [" + n + "]\n" +
                            "EDAD: [" + e + "]\n" +
                            "SEXO: [" + s + "]\n" +
                            "FECHA DE INSCRIPCIÖN: [" + f + "]\n" +
                            "CARRERA: [" + c + "]\n" +
                            "ESTATUS: [" + est + "]");

                    ImageView image = dialogView.findViewById(R.id.ivFotoAlumno);
                    cargaImagen(img, image);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Importante");
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            aceptar();
                            limpiar();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Registro aun activo", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.show();
                    bnd = 0;
                }else{
                    Toast.makeText(getContext(), "Favor de ingresar un criterio de busqueda", Toast.LENGTH_LONG);
                    bnd = 0;
                }
                break;
            case R.id.btnBaja:
                if(bnd == 1){
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alumno, null);
                    ((TextView) dialogView.findViewById(R.id.tvNuevoAlumno)).setText("¿Desea dar de baja al alumno?\n" +
                            "NOCTROL: [" + noctrl + "]\n" +
                            "NOMBRE: [" + n + "]\n" +
                            "EDAD: [" + e + "]\n" +
                            "SEXO: [" + s + "]\n" +
                            "FECHA DE INSCRIPCIÖN: [" + f + "]\n" +
                            "CARRERA: [" + c + "]\n" +
                            "ESTATUS: [" + est + "]");

                    ImageView image = dialogView.findViewById(R.id.ivFotoAlumno);
                    cargaImagen(img, image);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Importante");
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bajaAlumno(Integer.parseInt(noctrl));
                            limpiar();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Operación cancelada", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialog.show();

                }else{
                    Toast.makeText(getContext(), "Favor de ingresar un criterio de busqueda válido", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }


    private void aceptar(){
        sqLite.abrirBase();
        sqLite.eliminar(etNoctrl.getText());
        Toast.makeText(getContext(), "Registro eliminado", Toast.LENGTH_LONG).show();
        sqLite.cerrarBase();
    }

    private void bajaAlumno(int noctrl){
        sqLite.abrirBase();
        String resultado = sqLite.updateStatus(noctrl);
        Toast.makeText(getContext(), resultado, Toast.LENGTH_LONG).show();
        sqLite.cerrarBase();
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