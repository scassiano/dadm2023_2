package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Atributos
    Context context;
    Button btnRegistrar, btnBuscar, btnListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //Inicializar las variables al llegar a esta actividad
    private void init(){
        context = getApplicationContext();
        btnRegistrar = findViewById(R.id.btnregistrar);
        btnBuscar = findViewById(R.id.btnbuscar);
        btnListar = findViewById(R.id.btnlistar);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        //Un if para saber que boton fue seleccionado
        int itemId = view.getId();
        if (itemId == R.id.btnregistrar){
            Toast.makeText(context,"Registrar",Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, GestionarEmpresaActivity.class);
            Bundle bolsa = new Bundle();
            //Esto sirve para que la aplicacion reconozca que es un valor nuevo
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if(itemId == R.id.btnbuscar){
            Toast.makeText(context,"Buscar",Toast.LENGTH_LONG).show();
            Intent i2 = new Intent(context, BuscarEmpresaActivity.class);
            startActivity(i2);
        } else if (itemId == R.id.btnlistar){
            Toast.makeText(context,"Listar",Toast.LENGTH_LONG).show();
            Intent i3 = new Intent(context, ListadoEmpresasActivity.class);
            startActivity(i3);
        }
    }
}