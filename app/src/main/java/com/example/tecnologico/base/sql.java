package com.example.tecnologico.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sql extends SQLiteOpenHelper {

    private static final String DATABASE = "alumnos";
    private static final int VERSION = 1;
    private static final String tAlumnos = "CREATE TABLE ALUMNOS (" +
            "NOCTRL INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "NOMBRE TEXT NOT NULL," +
            "EDAD TEXT NOT NULL," +
            "SEXO TEXT NOT NULL," +
            "CARRERA TEXT NOT NULL," +
            "FECHA_INGRESO TEXT NOT NULL," +
            "STATUS TEXT NOT NULL," +
            "IMAGEN TEXT NOT NULL);";

    public sql(Context context){
        super(context, DATABASE, null, VERSION);
    }


    //Al crear la base se crea la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tAlumnos);
    }

    //Si se actualiza, se ejecuta un borrado de la tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS ALUMNOS");
            db.execSQL(tAlumnos);
        }
    }


}
