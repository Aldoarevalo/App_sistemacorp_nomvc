package com.example.app_ejemplo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase bd_ejemplo) {
        bd_ejemplo.execSQL("create table marca(cod_mar integer primary key autoincrement, mar_descripcion text)");
        bd_ejemplo.execSQL("create table usuario(cod_usu integer primary key autoincrement, usu_ci integer, usu_nombre text, " +
        "usu_apellido text, usu_rol text, usu_estado text, usu_login text, usu_clave integer)");

        bd_ejemplo.execSQL("insert into usuario values('01','1234567', 'JUAN', 'GONZALEZ', 'ADMINISTRADOR','ACTIVO', 'admin','123')");
           }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il) {

    }
}
