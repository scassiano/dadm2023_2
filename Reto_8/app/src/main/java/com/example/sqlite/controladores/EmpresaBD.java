package com.example.sqlite.controladores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.sqlite.modelos.Empresa;

import java.util.ArrayList;
import java.util.List;

public class EmpresaBD extends SQLiteOpenHelper implements IEmpresaBD {
    //Clase para acceder y manipular los datos
    Context contexto;

    //Una lista auxiliar que sirve para mostrar todos los registros de la base de datos
    private List<Empresa> empresaList = new ArrayList<>();


    //Constructor
    //Elementos que requiere SQLite OpenHelper
    public EmpresaBD(@Nullable Context contexto, @Nullable String bdName,
                     @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(contexto, bdName, factory, version);
        this.contexto = contexto;
    }

    //Metodos a implementar

    //Este metodo solo se ejecuta una vez
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Metodo que permite definir la estructura de la BD
        //Y los datos iniciales
        String sql = "CREATE TABLE empresas("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT,"+
                "url TEXT,"+
                "telefono INTEGER,"+
                "email TEXT,"+
                "productosServicios TEXT,"+
                "clasificacion TEXT)";
        sqLiteDatabase.execSQL(sql);
        //Este es un registro de prueba
        //El id se registra como null, ya que es autoincremental
        String insert = "INSERT INTO empresas VALUES(null,"+
                "'UN Software',"+
                "'https://unal.edu.co/',"+
                "3165000,"+
                "'unsoftware@unal.edu.co',"+
                "'Ofrece desarrollo de aplicaciones moviles y web',"+
                "'desarrollo a la medida')";
        sqLiteDatabase.execSQL(insert);

    }

    //Este es para actualizar la estructura de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Metodo para retornar el registro de una empresa buscada por el id
    @Override
    public Empresa elemento(int id) {
        //Solo lectura ya que es una consulta
        SQLiteDatabase database = getReadableDatabase();
        //Cursor es un apuntador a los resutlados de una consulta
        Cursor cursor = database.rawQuery("SELECT * FROM empresas WHERE _id="+id,null);
        //Si no hay nada en la consulta retornar null
        //Se pone en un try por si hay algun error en el metodo al extraer empresa o
        //cursor moveToNext
        try {
            if(cursor.moveToNext()){
                return extraerEmpresa(cursor);
            } else {
                return null;
            }
        }catch (Exception e){
            Log.d("TAG", "Error elemento (id) EmpresaBD" + e.getMessage());
            throw e;
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    private Empresa extraerEmpresa(Cursor cursor) {
        //Se crea un objeto mpresa con los datos
        //Que se traen de la base de datos
        Empresa empresa = new Empresa();
        empresa.setId(cursor.getInt(0));
        empresa.setNombre(cursor.getString(1));
        empresa.setUrl(cursor.getString(2));
        empresa.setTelefono(cursor.getInt(3));
        empresa.setEmail(cursor.getString(4));
        empresa.setProductosServicios(cursor.getString(5));
        empresa.setClasificacion(cursor.getString(6));

        return empresa;
    }

    //Recupera un elemento segun su nombre
    @Override
    public Empresa elemento(String nombre) {
        //Solo lectura ya que es una consulta
        SQLiteDatabase database = getReadableDatabase();
        //Cursor es un apuntador a los resutlados de una consulta
        Cursor cursor = database.rawQuery("SELECT * FROM empresas WHERE nombre='"+nombre+"'",null);
        //Si no hay nada en la consulta retornar null
        //Se pone en un try por si hay algun error en el metodo al extraer empresa o
        //cursor moveToNext
        try {
            if(cursor.moveToNext()){
                return extraerEmpresa(cursor);
            } else {
                return null;
            }
        }catch (Exception e){
            Log.d("TAG", "Error elemento (title) EmpresaBD" + e.getMessage());
            throw e;
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    @Override
    public List<Empresa> lista() {
        empresaList.clear();
        //Retorna todos los elementos encontrados en la base de datos
        //Ordenados ascendentemente por titulo
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM empresas;";
        Cursor cursor = database.rawQuery(sql, null);
        //Recorre todos los elementos que encuentre en la consulta
        //Se van almacenando los objetos de tipo empresa en la base de datos
        if(cursor.moveToFirst()){
            do{
                empresaList.add(
                       new Empresa(
                               cursor.getInt(0),
                               cursor.getString(1),
                               cursor.getString(2),
                               cursor.getInt(3),
                               cursor.getString(4),
                               cursor.getString(5),
                               cursor.getString(6)
                       )
                );
            }while(cursor.moveToNext());
        }
        cursor.close();
        return empresaList;
    }

    //Agregar un registro de una empresa
    @Override
    public void agregar(Empresa empresa) {
        SQLiteDatabase database = getWritableDatabase();
        //Una clase que sirve para almacenar los valores
        //Que se van a registrar en una inserción
        ContentValues values = new ContentValues();
        //id no se pone porque es autoincremental
        values.put("nombre", empresa.getNombre());
        values.put("url", empresa.getUrl());
        values.put("telefono", empresa.getTelefono());
        values.put("email",empresa.getEmail());
        values.put("productosServicios", empresa.getProductosServicios());
        values.put("clasificacion", empresa.getClasificacion());

        //Se realiza la insercion apoyado en los objetos creados
        database.insert("empresas", null, values);
    }

    //Actualizar un registro de la base de datos
    @Override
    public void actualizar(int id, Empresa empresa) {
        //Acceso a base de datos en modo escritura
        SQLiteDatabase database = getWritableDatabase();
        //Estos parametros seran los que se utilizaran para actualizar un registro
        String[] parametros = {String.valueOf(id)};
        //Una clase que sirve para almacenar los valores
        //Que se van a registrar en una actualizacion
        ContentValues values = new ContentValues();
        //id no se pone porque es autoincremental
        values.put("nombre", empresa.getNombre());
        values.put("url", empresa.getUrl());
        values.put("telefono", empresa.getTelefono());
        values.put("email",empresa.getEmail());
        values.put("productosServicios", empresa.getProductosServicios());
        values.put("clasificacion", empresa.getClasificacion());

        //Se realiza la actualizacion apoyado en los objetos creados y los parametros
        database.update("empresas",values, "_id=?", parametros);
    }

    @Override
    public void borrar(int id) {
        //borrar atraves de un id
        //Acceso a base de datos en modo escritura
        SQLiteDatabase database = getWritableDatabase();
        //Estos parametros seran los que se utilizaran para borrar un registro
        String[] parametros = {String.valueOf(id)};
        //Se hace la sentencia de borrado
        database.delete("empresas","_id=?",parametros);
    }

}
