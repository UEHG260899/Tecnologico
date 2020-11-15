package com.example.tecnologico.ui.busqueda;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

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

public class BuscarFragment extends Fragment implements View.OnClickListener{

    Button btnBusca, btnLimpia;
    EditText etNoctrl;
    TextView tvNombre, tvEdad, tvSexo, tvFecha, tvCarrera, tvStatus;
    ImageView ivFoto;
    SQLite sqLite;
    String noctrl, n, e, s, f, c, est, img;
    static int idp;
    private BuscarViewModel mViewModel;

    public static BuscarFragment newInstance() {
        return new BuscarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buscar, container, false);
        sqLite = new SQLite(getContext());
        componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarViewModel.class);
        // TODO: Use the ViewModel
    }

    private void componentes(View root){
        botonesComponentes(root);
        tvComponentes(root);
        etComponentes(root);
    }

    private void botonesComponentes(View root){
        btnBusca = root.findViewById(R.id.btnBuscarb);
        btnLimpia = root.findViewById(R.id.btnLimpiab);
        ivFoto = root.findViewById(R.id.ivFotoBusc);

        btnLimpia.setOnClickListener(this);
        btnBusca.setOnClickListener(this);
    }

    private void etComponentes(View root){
        etNoctrl = root.findViewById(R.id.tIETNoctrlb);
    }

    private void tvComponentes(View root){
        tvCarrera = root.findViewById(R.id.tvCarrerab);
        tvEdad = root.findViewById(R.id.tvEdadb);
        tvFecha = root.findViewById(R.id.tvFechab);
        tvNombre = root.findViewById(R.id.tvNombreb);
        tvSexo = root.findViewById(R.id.tvSexoB);
        tvStatus = root.findViewById(R.id.tvStatusb);
    }

    private void limpiar(){
        tvCarrera.setText("Carrera: ");
        tvEdad.setText("Edad: ");
        tvFecha.setText("Fecha: ");
        tvNombre.setText("Nombre: ");
        tvSexo.setText("Sexo: ");
        tvStatus.setText("Estatus: ");
        etNoctrl.setText("");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);

        btnBusca.setEnabled(true);
        etNoctrl.setEnabled(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLimpiab:
                limpiar();
                break;
            case R.id.btnBuscarb:
                if(etNoctrl.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Favor de introducir un criterio de busqueda", Toast.LENGTH_LONG).show();
                }else{
                    sqLite.abrirBase();
                    idp = Integer.parseInt(etNoctrl.getText().toString());
                    if(sqLite.getValor(idp).getCount() == 1){
                        Cursor cursor = sqLite.getValor(idp);
                        btnBusca.setEnabled(false);
                        etNoctrl.setEnabled(false);
                        if(cursor.moveToFirst()){
                            do{
                                noctrl = etNoctrl.getText().toString();
                                n = cursor.getString(1);
                                e = cursor.getString(2);
                                s = cursor.getString(3);
                                c = cursor.getString(4);
                                f = cursor.getString(5);
                                est = cursor.getString(6);
                                img = cursor.getString(7);
                            }while (cursor.moveToNext());
                        }

                        tvSexo.setText("Sexo: " + s);
                        tvCarrera.setText("Carrera: " + c);
                        tvStatus.setText("Estatus: " + est);
                        tvFecha.setText("Fecha de Inscripción: " + f);
                        tvNombre.setText("Nombre del Alumno: " + n);
                        tvEdad.setText("Edad: " + e);
                        cargaImagen(img, ivFoto);
                        sqLite.cerrarBase();
                    }else{
                        Toast.makeText(getContext(), "No hay registros que coincidan con los criterios de busqueda", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
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