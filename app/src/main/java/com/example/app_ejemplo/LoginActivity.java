package com.example.app_ejemplo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public EditText et_usuario;
    public EditText et_cod_usu;
    private EditText et_clave;
    private Button ingresar, salir;
    private Cursor fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_usuario = (EditText)findViewById(R.id.txt_usuario);
        et_clave = (EditText)findViewById(R.id.txt_clave);
        et_usuario.requestFocus();
    }

    public void validar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String usuario = et_usuario.getText().toString();
        String clave = et_clave.getText().toString();

        if(!usuario.isEmpty() && !clave.isEmpty()){
            Cursor fila = BaseDeDatos.rawQuery
                    ("select * from usuario where usu_login='"+ usuario +"' and usu_clave='"+clave+"'", null);
            //String cod = String.valueOf(BaseDeDatos.rawQuery
                    //("select cod_usu from usuario where usu_login='"+ usuario +"' and usu_clave='"+clave+"'", null));

            if(((Cursor) fila).moveToFirst()){

                finish();
                //funcion para llamar a otra activity
                Intent siguiente = new Intent(this, MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("usu", et_usuario.getText().toString());


                siguiente.putExtras(bundle);
                startActivity(siguiente); //metodo para levantar la otra activity- cambio de pantalla


                BaseDeDatos.close();
            }else{
                Toast.makeText(this, "Usuario o Clave Incorrectos", Toast.LENGTH_LONG).show();
                BaseDeDatos.close();
            }
        }else{
            Toast.makeText(this, "Hay Campos Vacios, VERIFIQUE!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void salir (View view){
        finish();
    }

}
