package com.example.tecnologico.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class SQLite {

    private sql sql;
    private SQLiteDatabase database;

    public SQLite(Context context){
        sql = new sql(context);
    }

    //Método para abrir la BD
    public void abrirBase(){
        Log.i("sqlite", "Se abre conexion con la BD" + sql.getDatabaseName());
        database = sql.getWritableDatabase();
    }

    //Método para cerrar la BD
    public void cerrarBase(){
        Log.i("sqlite", "Se cierra la conexion a la BD" + sql.getDatabaseName());
        sql.close();
    }

    //Método de inserción
    public boolean addAlumno(int noctrl, String nombre, String edad, String sexo, String carrera, String fecha, String status, String imagen){

        ContentValues cv = new ContentValues();
        cv.put("NOCTRL", noctrl);
        cv.put("NOMBRE", nombre);
        cv.put("EDAD", edad);
        cv.put("SEXO", sexo);
        cv.put("CARRERA", carrera);
        cv.put("FECHA_INGRESO", fecha);
        cv.put("STATUS", status);
        cv.put("IMAGEN", imagen);

        return (database.insert("ALUMNOS", null, cv) != -1) ? true : false;
    }

    //Método que retorna los alumnos
    public Cursor getRegistros(String status){
        return database.rawQuery("SELECT * FROM ALUMNOS WHERE STATUS = '" + status + "'", null);
    }

    //Métodos que retorna los datos de los alumnos
    public ArrayList<String> getAlumnos(Cursor cursor){

        ArrayList<String> ListData = new ArrayList<>();
        String item = "";

        if(cursor.moveToFirst()){
            do{
                item += "NOCTRL: [" + cursor.getString(0) + "]\r\n";
                item += "NOMBRE: [" + cursor.getString(1) + "]\r\n";
                item += "EDAD: [" + cursor.getString(2) + "]\r\n";
                item += "SEXO: [" + cursor.getString(3) + "]\r\n";
                item += "CARRERA: [" + cursor.getString(4) + "]\r\n";
                item += "FECHA INGRESO: [" + cursor.getString(5) + "]\r\n";
                item += "STATUS: [" + cursor.getString(6) + "]\r\n";
                ListData.add(item);
                item = "";
            }while (cursor.moveToNext());
        }

        return ListData;
    }

    //Método que retorna las imagenes
    public ArrayList<String> getImagenes(Cursor cursor){

        ArrayList<String> ListData = new ArrayList<>();


        if(cursor.moveToFirst()){
            do{
                ListData.add(cursor.getString(7));
            }while (cursor.moveToNext());
        }

        return ListData;
    }

    public ArrayList<String> getID(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        String item = "";

        if(cursor.moveToFirst()){
            do{
                item += "NOCTRL: [" + cursor.getString(0) + "]\r\n";
                ListData.add(item);
                item = "";
            }while(cursor.moveToNext());
        }

        return  ListData;
    }

    //Método de actualización
    public String updateAlumno(int noctrl, String nombre, String edad, String sexo, String carrera, String fecha, String status, String imagen){

        ContentValues cv = new ContentValues();
        cv.put("NOCTRL", noctrl);
        cv.put("NOMBRE", nombre);
        cv.put("EDAD", edad);
        cv.put("SEXO", sexo);
        cv.put("CARRERA", carrera);
        cv.put("FECHA_INGRESO", fecha);
        cv.put("STATUS", status);
        cv.put("IMAGEN", imagen);

        int valor = database.update("ALUMNOS", cv, "NOCTRL = " + noctrl, null);

        if(valor == 1){
            return "Alumno actualizado con exito";
        }else{
            return "Error en la actualización";
        }
    }

    //Método de la baja lógica
    public String updateStatus(int noctrl){
        ContentValues cv = new ContentValues();
        cv.put("STATUS", "Inactivo");

        int valor = database.update("ALUMNOS", cv, "NOCTRL = " + noctrl, null);

        if(valor == 1){
            return "Alumno dado de baja";
        }else{
            return "Error en la eliminación";
        }
    }

    //método de la busqueda de un alumno
    public Cursor getValor(int noctrl){
        return database.rawQuery("SELECT * FROM ALUMNOS WHERE NOCTRL = " + noctrl, null);
    }


    //método de eliminacion
    public int eliminar(Editable noctrl){
        return database.delete("ALUMNOS", "ID = "+ noctrl, null);
    }



}
