package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sqlite.controladores.EmpresaBD;
import com.example.sqlite.modelos.Empresa;

import java.util.ArrayList;
import java.util.List;

public class ListadoEmpresasActivity extends AppCompatActivity {


    ListView listView;
    ArrayList<String> nombresEmpresas;
    ArrayList<Integer> idEmpresas;

    EmpresaBD empresaBD;

    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_empresas);
        init();
    }

    private void init() {
        contexto = getApplicationContext();
        empresaBD = new EmpresaBD(contexto, "EmpresaBD.db",null,1);
        listView = findViewById(R.id.listaEmpresas);
        llenarListView();
    }

    private void llenarListView() {
        nombresEmpresas = new ArrayList<String>();
        idEmpresas = new ArrayList<Integer>();

        List<Empresa> listaEmpresas = new ArrayList<Empresa>();

        //Llenar la lista
        listaEmpresas = empresaBD.lista();

        for(int i=0; i<listaEmpresas.size(); i++){
            Empresa e = listaEmpresas.get(i);
            nombresEmpresas.add(e.getNombre());
            idEmpresas.add(e.getId());
        }

        //Se crea un adaptador para pasar los datos de la lista
        //al listView creado previamente
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        contexto,
                        android.R.layout.simple_list_item_1,
                        nombresEmpresas
                );

        listView.setAdapter(adapter);

        //Se crea la siguiente estructura para que cuando se haga click en un
        //elemento de la lista se pueda ir a la pantalla de gestion
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Se obtiene la empresa a partir de su ID
                Empresa empresa = empresaBD.elemento(idEmpresas.get(i));
                Bundle bolsa = new Bundle();
                bolsa.putInt("id",empresa.getId());
                bolsa.putString("nombre",empresa.getNombre());
                bolsa.putString("url",empresa.getUrl());
                bolsa.putInt("telefono",empresa.getTelefono());
                bolsa.putString("email",empresa.getEmail());
                bolsa.putString("productosServicios",empresa.getProductosServicios());
                bolsa.putString("clasificacion",empresa.getClasificacion());

                Intent i1 = new Intent(contexto, GestionarEmpresaActivity.class);
                i1.putExtras(bolsa);
                startActivity(i1);
            }
        });
    }
}