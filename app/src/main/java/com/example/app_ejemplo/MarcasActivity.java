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

public class MarcasActivity extends AppCompatActivity {
    private EditText et_codigo, et_descripcion;
    private ListView lista;
    private Integer idEliminar;
    private Cursor fila;
    private Button registrar;

    @Override

    public void onBackPressed (){

        AlertDialog.Builder builder = new AlertDialog.Builder(MarcasActivity.this);
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
        setContentView(R.layout.activity_marcas);

        et_codigo = (EditText) findViewById(R.id.txt_codigo);
        et_descripcion = (EditText) findViewById(R.id.txt_descripcion);
        lista = (ListView) findViewById(R.id.listamarca);
        registrar = (Button) findViewById(R.id.bt_registrar);
        cargaLista();
        et_codigo.setEnabled(false);
        et_descripcion.requestFocus();

       /* lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String listItem = (String) lista.getItemAtPosition(position);

                    idEliminar = Integer.parseInt(listItem.split(" - ")[0]);

                et_codigo.setText(idEliminar.toString());
                Recuperar();

                Toast.makeText(MarcasActivity.this, "lista " + listItem+ "Id es; "+idEliminar, Toast.LENGTH_LONG).show();


                return true;

            }
        }); */

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String listItem = (String) lista.getItemAtPosition(i);

                idEliminar = Integer.parseInt(listItem.split(" - ")[0]);

                et_codigo.setText(idEliminar.toString());
                Recuperar();
            }
        });


    }

    public void Registrar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString().toUpperCase();

        if (!descripcion.isEmpty()) {
            fila = BaseDeDatos.rawQuery("SELECT * FROM marca WHERE mar_descripcion= '" + descripcion + "'", null);
            if (fila.getCount() > 0) {
                Toast.makeText(this, "El registro ya existe...", Toast.LENGTH_LONG).show();
            } else {
                ContentValues registro = new ContentValues();
                registro.put("mar_descripcion", descripcion);

                BaseDeDatos.insert("marca", null, registro);
                BaseDeDatos.close();
                Cancelar();
                Toast.makeText(this, "Guardado Correctamente", Toast.LENGTH_LONG).show();
                cargaLista();
            }
        } else {
            Toast.makeText(this, "Ingrese la Descripcion", Toast.LENGTH_LONG).show();
        }
    }


    public void Eliminar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        final String codigo = et_codigo.getText().toString();

        if (!codigo.isEmpty()) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MarcasActivity.this);
        builder.setMessage("Esta seguro de eliminar el registro");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if (!codigo.isEmpty()) {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MarcasActivity.this, "administracion", null, 1);
                    SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
                   // fila = BaseDeDatos.rawQuery("SELECT * FROM extintor_cab WHERE cod_mar= " + codigo + "", null);
                   // if (fila.getCount() > 0) {
                      //  Toast.makeText(MarcasActivity.this, "El registro esta siendo usado en otra tabla", Toast.LENGTH_LONG).show();
                    //} else {
                        int cantidad = BaseDeDatos.delete("marca", "cod_mar=" + codigo, null);
                        BaseDeDatos.close();

                        Cancelar();
                        registrar.setEnabled(true);
                        et_descripcion.requestFocus();
                        if (cantidad == 1) {
                            Toast.makeText(MarcasActivity.this, "Registro Eliminado", Toast.LENGTH_LONG).show();
                            cargaLista();
                        } else {
                        }
                    //}

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
            Toast.makeText(MarcasActivity.this, "Seleccione Elemento", Toast.LENGTH_LONG).show();
        }

    }

    public void Modificar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString().toUpperCase();

        if (!codigo.isEmpty() && !descripcion.isEmpty()) {
            fila = BaseDeDatos.rawQuery("SELECT * FROM marca WHERE mar_descripcion= '" + descripcion + "'", null);
            if (fila.getCount() > 0) {
                Toast.makeText(this, "El registro ya existe...", Toast.LENGTH_LONG).show();
            } else {

            ContentValues registro = new ContentValues();
            registro.put("mar_descripcion", descripcion);

            int cantidad = BaseDeDatos.update("marca", registro, "cod_mar=" + codigo, null);
            BaseDeDatos.close();

            if (cantidad == 1) {
                Toast.makeText(this, "Registro Modificado Exitosamente", Toast.LENGTH_LONG).show();
                Cancelar();
                cargaLista();
                registrar.setEnabled(true);
            } else {
                Toast.makeText(this, "Ingrese Codigo de Marca", Toast.LENGTH_LONG).show();
            }

        }
        }

    }

    public void Cancelar() {
        et_descripcion.setText("");
        et_codigo.setText("");
    }

    public void CancelarBoton(View view) {
        et_descripcion.setText("");
        et_codigo.setText("");
        registrar.setEnabled(true);
    }

    public void cargaLista() {

        AdminSQLiteOpenHelper helper = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        fila = db.rawQuery("SELECT cod_mar, mar_descripcion FROM marca ORDER BY cod_mar", null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        while (fila.moveToNext()) {
            //System.out.pUrintln("coco:"+fila.getString(0));
            adapter.add(fila.getString(0) + " - " + fila.getString(1));
        }
        lista.setAdapter(adapter);


    }

    public void Recuperar() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();

        if (!codigo.isEmpty()) {
            Cursor fila = BaseDeDatos.rawQuery
                    ("select mar_descripcion from marca where cod_mar=" + codigo, null);

            if (((Cursor) fila).moveToFirst()) {
                et_descripcion.setText(fila.getString(0));
                registrar.setEnabled(false);

                BaseDeDatos.close();
            } else {
                Toast.makeText(this, "No existe El Articulo", Toast.LENGTH_LONG).show();
                BaseDeDatos.close();
            }
        } else {
            Toast.makeText(this, "Ingrese el Codigo para Buscar", Toast.LENGTH_LONG).show();
        }
    }

}
