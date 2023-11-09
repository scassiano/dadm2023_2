package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlite.controladores.EmpresaBD;
import com.example.sqlite.modelos.Empresa;

public class BuscarEmpresaActivity extends AppCompatActivity implements View.OnClickListener{

    Context context;
    EditText txtnombre;
    Button btnbuscar;
    EmpresaBD empresaBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_empresa);
        init();
    }

    private void init(){
        context = getApplicationContext();
        txtnombre = findViewById(R.id.bus_txtnombre);
        btnbuscar = findViewById(R.id.bus_btnbuscar);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bus_btnbuscar){
            String nombre = txtnombre.getText().toString();
            Empresa empresa = buscarEmpresa(nombre);
            //Si se logra recuperar una empresa con dicho nombre
            if ( empresa != null ){
                Bundle bolsa = new Bundle();
                bolsa.putInt("id",empresa.getId());
                bolsa.putString("nombre",empresa.getNombre());
                bolsa.putString("url",empresa.getUrl());
                bolsa.putInt("telefono",empresa.getTelefono());
                bolsa.putString("email",empresa.getEmail());
                bolsa.putString("productosServicios",empresa.getProductosServicios());
                bolsa.putString("clasificacion",empresa.getClasificacion());

                Intent i = new Intent(context, GestionarEmpresaActivity.class);
                i.putExtras(bolsa);
                startActivity(i);
            }else{
                Toast.makeText(context, "No existe empresa con ese nombre", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Empresa buscarEmpresa(String nombre){
        empresaBD = new EmpresaBD(context, "EmpresasBD.db", null, 1);
        Empresa empresa = empresaBD.elemento(nombre);
        return empresa;
    }
}