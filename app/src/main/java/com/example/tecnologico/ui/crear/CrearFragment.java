package com.example.tecnologico.ui.crear;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Button btnLimpiar, btnGuardar;
    private ImageButton btnFecha;
    private EditText etNoctrl, etNombre, etFecha, etEdad;
    private Spinner spSexo, spCarrera, spStatus;
    private ImageView ivFoto;

    private Uri photoUri;
    public SQLite sqLite;

    public static final int REQUEST_TAKE_PHOTO = 1;
    private static int anio,mes,dia;
    String currentPath, sexo, status, carrera, img ="";

    DatePickerDialog dpd;
    Calendar c;

    private CrearViewModel crearViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        crearViewModel =
                new ViewModelProvider(this).get(CrearViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crear, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        crearViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        sqLite = new SQLite(getContext());

        Componentes(root);

        return root;
    }

    //inicialización de componentes
    private void Componentes(View root){
        EditTextComponentes(root);
        Botones(root);
        SpinnerComponentes(root);
    }

    private void EditTextComponentes(View root){
        etNoctrl = (EditText) root.findViewById(R.id.tIETNoctrlna);
        etNombre = (EditText) root.findViewById(R.id.tIETNombrena);
        etEdad = (EditText) root.findViewById(R.id.tIETEdadna);
        etFecha = (EditText) root.findViewById(R.id.tIETFechana);
    }

    private void Botones(View root){
        btnFecha = root.findViewById(R.id.btnFechana);
        btnLimpiar = root.findViewById(R.id.btnLimpiarna);
        btnGuardar = root.findViewById(R.id.btnGuardar);
        ivFoto = root.findViewById(R.id.ivNa);

        btnFecha.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        ivFoto.setOnClickListener(this);
    }

    private void SpinnerComponentes(View root){
        ArrayAdapter<CharSequence> sexoAdapter, carreraAdapter, statusAdapter;
        sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.genero, android.R.layout.simple_spinner_item);
        carreraAdapter = ArrayAdapter.createFromResource(getContext(), R.array.carrera, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.estatus, android.R.layout.simple_spinner_item);

        spSexo = root.findViewById(R.id.spSexona);
        spSexo.setAdapter(sexoAdapter);
        spCarrera = root.findViewById(R.id.spCarrerana);
        spCarrera.setAdapter(carreraAdapter);
        spStatus = root.findViewById(R.id.spEstatus);
        spStatus.setAdapter(statusAdapter);

        spSexo.setOnItemSelectedListener(this);
        spCarrera.setOnItemSelectedListener(this);
        spStatus.setOnItemSelectedListener(this);
    }

    //Método de limpieza
    private void limpiar(){
        etNombre.setText("");
        etFecha.setText("");
        etEdad.setText("");
        etNoctrl.setText("");

        ArrayAdapter<CharSequence> sexoAdapter, carreraAdapter, statusAdapter;
        sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.genero, android.R.layout.simple_spinner_item);
        carreraAdapter = ArrayAdapter.createFromResource(getContext(), R.array.carrera, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.estatus, android.R.layout.simple_spinner_item);

        sexo = "";
        carrera = "";
        status = "";

        spSexo.setAdapter(sexoAdapter);
        spCarrera.setAdapter(carreraAdapter);
        spStatus.setAdapter(statusAdapter);
        ivFoto.setImageResource(R.drawable.ic_menu_camera);
    }

    //Eventos del boton
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLimpiarna:
                limpiar();
                break;
            case R.id.btnFechana:
                c = Calendar.getInstance();
                anio = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(getContext(), this, anio, mes, dia);
                dpd.show();
                break;
            case R.id.ivNa:
                Intent tomaFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(tomaFoto.resolveActivity(getActivity().getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImage();
                    }catch (IOException e){
                        Toast.makeText(getContext(), "Error al generar foto", Toast.LENGTH_LONG).show();
                    }

                    if(photoFile != null){
                        photoUri = FileProvider.getUriForFile(getContext(), "com.example.tecnologico", photoFile);
                        tomaFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(tomaFoto, REQUEST_TAKE_PHOTO);
                    }
                }
                break;
            case R.id.btnGuardar:

                if(etNoctrl.getText().toString().isEmpty() || etNombre.getText().toString().isEmpty() || etFecha.getText().toString().isEmpty() || etEdad.getText().toString().isEmpty()
                        || sexo.isEmpty() || carrera.isEmpty() || img.isEmpty() || status.isEmpty()){

                    Toast.makeText(getContext(), "Hay campos vacios", Toast.LENGTH_LONG).show();
                }else{
                    int noctrl = Integer.parseInt(etNoctrl.getText().toString());
                    String nom = etNombre.getText().toString().toUpperCase();
                    String edad = etEdad.getText().toString();
                    String fecha = etFecha.getText().toString();

                    sqLite.abrirBase();

                    if(sqLite.addAlumno(noctrl, nom, edad, sexo, carrera, fecha, status, img)){
                        Toast.makeText(getContext(), "Datos almacenados", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "Error en almacenamiento", Toast.LENGTH_LONG).show();
                    }

                    sqLite.cerrarBase();
                    limpiar();
                }
        }
    }

    //Eventos de los spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spSexona:
                if(position != 0){
                    sexo = parent.getItemAtPosition(position).toString();
                }else{
                    sexo = "";
                }
                break;
            case R.id.spCarrerana:
                if(position != 0){
                    carrera = parent.getItemAtPosition(position).toString();
                }else{
                    carrera = "";
                }
                break;
            case R.id.spEstatus:
                if(position != 0){
                    status = parent.getItemAtPosition(position).toString();
                }else{
                    status = "";
                }
                break;
        }
    }

    //método que crea el archivo de la foto
    private File createImage() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFile = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFile, "jpg", storageDir);

        currentPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }

    //método que nos indica si el proceso de almacenamiento de la foto fue exitoso o no
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            ivFoto.setImageURI(photoUri);
            img = currentPath;
            Toast.makeText(getContext(), "Foto guardada en: " + img, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "Algo fallo", Toast.LENGTH_LONG).show();
        }
    }
}