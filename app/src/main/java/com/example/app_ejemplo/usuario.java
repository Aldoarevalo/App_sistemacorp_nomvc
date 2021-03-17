package com.example.app_ejemplo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

//import static com.example.appventas.R.id.codigo;
import static com.example.app_ejemplo.R.id.codigoe;
//import static com.example.appventas.R.id.codigou;

public class usuario extends AppCompatActivity {

        private EditText
            et_codigou,
            et_cedula,
            et_nombre,
            et_apellido,
            et_login,
            et_contraseña,
            et_rol,
            et_estado;

    private ListView lista;
    private Integer idEliminar;
    private Cursor fila;
    private Button registrar;
    //private Button buscar;
    @Override

    public void onBackPressed (){
        AlertDialog.Builder builder = new AlertDialog.Builder(usuario.this);
        builder.setMessage("Desea Salir?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        et_codigou = (EditText)findViewById(codigoe);
        et_cedula = (EditText)findViewById(R.id.cedula);
        et_nombre = (EditText)findViewById(R.id.nombre);
        et_apellido = (EditText)findViewById(R.id.apellido);
        et_login = (EditText)findViewById(R.id.login);
        et_contraseña = (EditText)findViewById(R.id.contraseña);
        et_rol = (EditText)findViewById(R.id.rol);
        et_estado = (EditText)findViewById(R.id.estado);
        lista = (ListView)findViewById(R.id.lista_usuario);
        registrar = (Button)findViewById(R.id.btn_registrar);
        //buscar = (Button)findViewById(R.id.bt_buscar);
        cargaLista();
        et_codigou.setEnabled(false);
        et_cedula.requestFocus();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String listItem = (String) lista.getItemAtPosition(i);

                idEliminar = Integer.parseInt(listItem.split(" - ")[0]);

                et_codigou.setText(idEliminar.toString());
                Recuperar();
            }
        });
    }

    public void Registrar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigou = et_codigou.getText().toString();
        String cedula = et_cedula.getText().toString();
        String nombre = et_nombre.getText().toString().toUpperCase();
        String apellido = et_apellido.getText().toString().toUpperCase();
        String login = et_login.getText().toString().toUpperCase();
        String contraseña = et_contraseña.getText().toString();
        String rol = et_rol.getText().toString().toUpperCase().toUpperCase();
        String estado = et_estado.getText().toString().toUpperCase();

        if (!cedula.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !login.isEmpty() && !contraseña.isEmpty() && !rol.isEmpty() && !estado.isEmpty()) {
            fila = BaseDeDatos.rawQuery("SELECT * FROM usuario WHERE cod_usu= '" + codigou + "'", null);
            if (fila.getCount() > 0) {
                Toast.makeText(this, "El registro ya existe...", Toast.LENGTH_LONG).show();
            } else {
                ContentValues registro = new ContentValues();

                registro.put("usu_ci", cedula);
                registro.put("usu_nombre", nombre);
                registro.put("usu_apellido", apellido);
                registro.put("usu_login", login);
                registro.put("usu_clave", contraseña);
                registro.put("usu_rol", rol);
                registro.put("usu_estado", estado);

                BaseDeDatos.insert("usuario", null, registro);
                BaseDeDatos.close();
                Cancelar();
                Toast.makeText(this, "Guardado Correctamente", Toast.LENGTH_LONG).show();
                cargaLista();
            }
        } else {
            Toast.makeText(this, "Hay Campos Vacios, VERIFIQUE!!!", Toast.LENGTH_LONG).show();
        }
    }


    public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        final String codigo = et_codigou.getText().toString();
        final String cedula = et_cedula.getText().toString();

        if (!codigo.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(usuario.this);
            builder.setMessage("Esta seguro de eliminar el registro");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if (!codigo.isEmpty()) {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(usuario.this, "administracion", null, 1);
                    SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
                    fila = BaseDeDatos.rawQuery("SELECT * FROM extintor_cab WHERE cod_usu= '" + codigo + "'", null);
                    if (fila.getCount() > 0) {
                        Toast.makeText(usuario.this, "El registro esta siendo usado en otra tabla", Toast.LENGTH_LONG).show();
                    } else {
                        int cantidad = BaseDeDatos.delete("usuario", "cod_usu=" + codigo, null);
                        BaseDeDatos.close();

                        Cancelar();
                        registrar.setEnabled(true);
                        et_cedula.requestFocus();
                        if (cantidad == 1) {
                            Toast.makeText(usuario.this, "Registro Eliminado", Toast.LENGTH_LONG).show();
                            cargaLista();
                        } else {
                        }
                    }

                /*} else {
                    Toast.makeText(MarcasActivity.this, "Seleccione Elemento", Toast.LENGTH_LONG).show();
                }*/
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            Toast.makeText(usuario.this, "Seleccione Elemento", Toast.LENGTH_LONG).show();
        }

    }

    public void Modificar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1 );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigou = et_codigou.getText().toString();
        String cedula = et_cedula.getText().toString();
        String nombre = et_nombre.getText().toString().toUpperCase();
        String apellido = et_apellido.getText().toString().toUpperCase();
        String login = et_login.getText().toString().toUpperCase();
        String contraseña = et_contraseña.getText().toString();
        String rol = et_rol.getText().toString().toUpperCase().toUpperCase();
        String estado = et_estado.getText().toString().toUpperCase();

        if(!codigou.isEmpty() && !cedula.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !login.isEmpty() && !contraseña.isEmpty() && !rol.isEmpty() && !estado.isEmpty()){

            ContentValues registro = new ContentValues();

            registro.put("usu_ci", cedula);
            registro.put("usu_nombre", nombre);
            registro.put("usu_apellido", apellido);
            registro.put("usu_login", login);
            registro.put("usu_clave", contraseña);
            registro.put("usu_rol", rol);
            registro.put("usu_estado", estado);

            int cantidad = BaseDeDatos.update("usuario", registro, "cod_usu="+ codigou, null);
            BaseDeDatos.close();

            if(cantidad == 1){
                Toast.makeText(this, "Registro Modificado Exitosamente", Toast.LENGTH_LONG).show();
                Cancelar();
                cargaLista();
                registrar.setEnabled(true);
            }else{
                Toast.makeText(this, "Ingrese el Codigo", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Hay Campos Vacios, VERIFIQUE!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void Cancelar(){
        et_codigou.setText("");
        et_cedula.setText("");
        et_nombre.setText("");
        et_apellido.setText("");
        et_login.setText("");
        et_contraseña.setText("");
        et_rol.setText("");
        et_estado.setText("");
        //buscar.setEnabled(true);
        et_cedula.requestFocus();
    }

    public void CancelarBoton(View view){
        et_codigou.setText("");
        et_cedula.setText("");
        et_nombre.setText("");
        et_apellido.setText("");
        et_login.setText("");
        et_contraseña.setText("");
        et_rol.setText("");
        et_estado.setText("");
        registrar.setEnabled(true);
        //buscar.setEnabled(true);
        et_cedula.requestFocus();
    }

    private void cargaLista(){

        AdminSQLiteOpenHelper helper = new AdminSQLiteOpenHelper(this, "administracion", null, 1 );
        SQLiteDatabase db = helper.getWritableDatabase();
        fila = db.rawQuery("SELECT * FROM usuario ORDER BY cod_usu",null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        while(fila.moveToNext()){
            //System.out.println("coco:"+fila.getString(0));
            adapter.add(fila.getString(0) +" - "+fila.getString(1)+" - "+fila.getString(2) +" - "+fila.getString(3) +" - "
                    +fila.getString(4) +" - "+fila.getString(5) +" - "+fila.getString(6) +" - "+fila.getString(7));
        }
        lista.setAdapter(adapter);


    }

    public void Recuperar(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigou.getText().toString();

        if(!codigo.isEmpty()){
            Cursor fila = BaseDeDatos.rawQuery
                    ("select * from usuario where cod_usu=" + codigo, null);

            if(((Cursor) fila).moveToFirst()){
                et_codigou.setText(fila.getString(0));
                et_cedula.setText(fila.getString(1));
                et_nombre.setText(fila.getString(2));
                et_apellido.setText(fila.getString(3));
                et_login.setText(fila.getString(4));
                et_contraseña.setText(fila.getString(5));
                et_rol.setText(fila.getString(6));
                et_estado.setText(fila.getString(7));
                registrar.setEnabled(false);
                //buscar.setEnabled(false);
                et_cedula.requestFocus();
                BaseDeDatos.close();
            }else{
                Toast.makeText(this, "No existe el Usuario", Toast.LENGTH_LONG).show();
                BaseDeDatos.close();
            }
        }else{
            Toast.makeText(this, "Ingrese el Codigo para Buscar", Toast.LENGTH_LONG).show();
        }
    }

    /*public void Buscar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String cedula = et_cedula.getText().toString();

        if(!cedula.isEmpty()){
            Cursor fila = BaseDeDatos.rawQuery
                    ("select emp_codigo, emp_nombre, emp_apellido, emp_salario, emp_telefono, emp_usuario, emp_clave from empleado where emp_ci=" + cedula, null);

            if(((Cursor) fila).moveToFirst()){
                et_codigo.setText(fila.getString(0));
                et_nombre.setText(fila.getString(1));
                et_apellido.setText(fila.getString(2));
                et_salario.setText(fila.getString(3));
                et_telefono.setText(fila.getString(4));
                et_usuario.setText(fila.getString(5));
                et_clave.setText(fila.getString(6));
                registrar.setEnabled(false);
                et_nombre.requestFocus();
                BaseDeDatos.close();
            }else{
                Toast.makeText(this, "No existe El Empleado", Toast.LENGTH_LONG).show();
                BaseDeDatos.close();
            }
        }else{
            Toast.makeText(this, "Ingrese el Numero de Documento", Toast.LENGTH_LONG).show();
        }
    }*/
}
