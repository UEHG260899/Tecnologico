package com.example.tecnologico.ui.editar;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tecnologico.R;
import com.example.tecnologico.base.SQLite;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private EditText etNoctrl, etNombre, etEdad, etFecha;
    private Button btnBuscar, btnLimpiar, btnEdita;
    private ImageView ivFoto;
    private ImageButton btnFecha;
    private Spinner spSexo, spCarrera, spStatus;
    private Uri photUri;
    private Calendar c;
    private DatePickerDialog dpd;
    public static String s, car, est, currentPath, img = "";
    private ArrayAdapter<CharSequence> sexoAdapter, statusAdapter, carreraAdapter;
    private EditarViewModel mViewModel;

    public static final int REQUEST_TAKE_PHOTO = 1;
    private static int anio, mes, dia;
    static int bnd = 0, idp;
    public SQLite sqLite;

    public static EditarFragment newInstance() {
        return new EditarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editar, container, false);
        Bundle bundle = this.getArguments();
        if(bundle == null){
            sqLite = new SQLite(getContext());
            componentes(root);
        }else{
            sqLite = new SQLite(getContext());
            componentes(root);
            etNoctrl.setText(bundle.getString("noctrl"));
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditarViewModel.class);
        // TODO: Use the ViewModel
    }

    private void componentes(View root) {
        botonesComponentes(root);
        spinnerComponentes(root);
        etComponentes(root);
    }

    private void botonesComponentes(View root) {
        btnLimpiar = root.findViewById(R.id.btnLimpiared);
        btnBuscar = root.findViewById(R.id.btnBuscared);
        btnEdita = root.findViewById(R.id.btnGuardared);
        btnFecha = root.findViewById(R.id.btnFechaed);
        ivFoto = root.findViewById(R.id.ivFotoEd);

        btnFecha.setOnClickListener(this);
        btnEdita.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        ivFoto.setOnClickListener(this);
    }

    private void spinnerComponentes(View root) {

        sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.genero, android.R.layout.simple_spinner_item);
        carreraAdapter = ArrayAdapter.createFromResource(getContext(), R.array.carrera, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.estatus, android.R.layout.simple_spinner_item);

        spSexo = root.findViewById(R.id.spSexoed);
        spSexo.setAdapter(sexoAdapter);
        spCarrera = root.findViewById(R.id.spCarreraed);
        spCarrera.setAdapter(carreraAdapter);
        spStatus = root.findViewById(R.id.spStatused);
        spStatus.setAdapter(statusAdapter);

        spSexo.setOnItemSelectedListener(this);
        spCarrera.setOnItemSelectedListener(this);
        spStatus.setOnItemSelectedListener(this);
    }

    private void etComponentes(View root) {
        etEdad = root.findViewById(R.id.tIETEdaded);
        etFecha = root.findViewById(R.id.tIETFechaed);
        etNombre = root.findViewById(R.id.tIETNombreed);
        etNoctrl = root.findViewById(R.id.tIETNoctrled);
    }

    private void limpiar() {
        etNoctrl.setText("");
        etNombre.setText("");
        etEdad.setText("");
        etFecha.setText("");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);

        sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.genero, android.R.layout.simple_spinner_item);
        carreraAdapter = ArrayAdapter.createFromResource(getContext(), R.array.carrera, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.estatus, android.R.layout.simple_spinner_item);

        img = "";
        s = "";
        car = "";

        spSexo.setAdapter(sexoAdapter);
        spCarrera.setAdapter(carreraAdapter);
        spStatus.setAdapter(statusAdapter);

        btnBuscar.setEnabled(true);
        etNoctrl.setEnabled(true);
        bnd = 0;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLimpiared:
                limpiar();
                break;
            case R.id.btnBuscared:
                if (etNoctrl.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Favor de ontroducir un criterio de busqueda", Toast.LENGTH_LONG).show();
                    bnd = 0;
                }else{
                    sqLite.abrirBase();

                    idp = Integer.parseInt(etNoctrl.getText().toString());
                    if (sqLite.getValor(idp).getCount() == 1) {
                        Cursor cursor = sqLite.getValor(idp);
                        btnBuscar.setEnabled(false);
                        etNoctrl.setEnabled(false);
                        if (cursor.moveToFirst()) {
                            do {
                                etNombre.setText(cursor.getString(1));
                                etEdad.setText(cursor.getString(2));
                                s = cursor.getString(3);
                                car = cursor.getString(4);
                                etFecha.setText(cursor.getString(5));
                                est = cursor.getString(6);
                                img = cursor.getString(7);
                            } while (cursor.moveToNext());
                        }

                        etNoctrl.setEnabled(false);
                        btnBuscar.setEnabled(false);
                        cargaImagen(img, ivFoto);
                        spSexo.setSelection(sexoAdapter.getPosition(s));
                        spCarrera.setSelection(carreraAdapter.getPosition(car));
                        spStatus.setSelection(statusAdapter.getPosition(est));

                        bnd = 1;
                    } else {
                        Toast.makeText(getContext(), "No se han encontrado coincidencias", Toast.LENGTH_LONG).show();
                    }

                    sqLite.cerrarBase();
                }


                break;
            case R.id.btnFechaed:
                c = Calendar.getInstance();
                anio = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(getContext(), this, anio, mes, dia);
                dpd.show();
                break;
            case R.id.ivFotoEd:
                Intent tomaFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (tomaFoto.resolveActivity(getActivity().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImage();
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Error al generar foto", Toast.LENGTH_LONG).show();
                    }

                    if (photoFile != null) {
                        photUri = FileProvider.getUriForFile(getContext(), "com.example.tecnologico", photoFile);
                        tomaFoto.putExtra(MediaStore.EXTRA_OUTPUT, photUri);
                        startActivityForResult(tomaFoto, REQUEST_TAKE_PHOTO);
                    }
                }
                break;
            case R.id.btnGuardared:
                if (bnd == 1) {
                    if (etNombre.getText().toString().isEmpty() || etFecha.getText().toString().isEmpty() || etEdad.getText().toString().isEmpty() || s.isEmpty()
                            || car.isEmpty() || img.isEmpty() || est.isEmpty()) {
                        Toast.makeText(getContext(), "Hay campos vacios", Toast.LENGTH_LONG).show();
                    } else {
                        int noctrl = Integer.parseInt(etNoctrl.getText().toString());
                        String nombre = etNombre.getText().toString().toUpperCase();
                        String edad = etEdad.getText().toString();
                        String fecha = etFecha.getText().toString();
                        sqLite.abrirBase();
                        String resultado = sqLite.updateAlumno(noctrl, nombre, edad, s, car, fecha, est, img);
                        Toast.makeText(getContext(), resultado, Toast.LENGTH_LONG).show();
                        sqLite.cerrarBase();
                        limpiar();
                    }
                } else {
                    Toast.makeText(getContext(), "No hay datos que modificar", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spSexoed:
                if (position != 0) {
                    s = parent.getItemAtPosition(position).toString();
                } else {
                    s = "";
                }
                break;
            case R.id.spStatused:
                if (position != 0) {
                    est = parent.getItemAtPosition(position).toString();
                } else {
                    est = "";
                }
                break;
            case R.id.spCarreraed:
                if (position != 0) {
                    car = parent.getItemAtPosition(position).toString();
                } else {
                    car = "";
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void cargaImagen(String imagen, ImageView iv) {
        try {
            File filePhoto = new File(imagen);
            Uri photUri = FileProvider.getUriForFile(getContext(), "com.example.tecnologico", filePhoto);
            iv.setImageURI(photUri);
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Ocurrio un error en la carga de la imagen", Toast.LENGTH_LONG).show();
            Log.d("Carga Imagen", "Error al cargar la imagen" + imagen + "\n Mensaje" + ex.getMessage() + "\n Causa: " + ex.getCause());
        }
    }

    private File createImage() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFile = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile, "jpg", storageDir);

        currentPath = image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            ivFoto.setImageURI(photUri);
            img = currentPath;
            Toast.makeText(getContext(), "Foto guardada en: " + img, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Algo fallo", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }
}