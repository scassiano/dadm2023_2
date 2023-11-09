package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sqlite.controladores.EmpresaBD;
import com.example.sqlite.modelos.Empresa;

import java.util.ArrayList;
import java.util.Arrays;

public class GestionarEmpresaActivity extends AppCompatActivity implements View.OnClickListener{

    Context contexto;

    EditText txtnombre, txturl, txttelefono, txtemail, txtproductosservicios;

    Button btnguardar, btnactualizar, btnborrar;

    Spinner spnclasificacion;

    int id;

    EmpresaBD empresaBD;

    //lista para facilitar el manejo del spinner

    ArrayList<String> listaClasificaciones = new ArrayList<String>(Arrays.asList("consultoria","desarrollo a la medida","fabrica de software"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_empresa);
        init();
    }

    private void init(){
        contexto = getApplicationContext();
        txtnombre = findViewById(R.id.ges_txtnombre);
        txturl = findViewById(R.id.ges_txturl);
        txttelefono = findViewById(R.id.ges_txttelefono);
        txtemail = findViewById(R.id.ges_txtemail);
        txtproductosservicios = findViewById(R.id.ges_txtpys);
        spnclasificacion = findViewById(R.id.ges_spnclasificacion);
        btnguardar = findViewById(R.id.ges_btnguardar);
        btnactualizar = findViewById(R.id.ges_btnactualizar);
        btnborrar = findViewById(R.id.ges_btnborrar);

        //Se accede a la información que mandaron los extras
        Intent i = getIntent();
        Bundle bolsa = i.getExtras();
        id = bolsa.getInt("id");
        //Si el id es diferente de 0, entonces es porque se llego desde la pantalla
        //De buscar empresa
        if(id != 0){
            //Se desactiva el boton de guardar, al ser inecesario
            btnguardar.setVisibility(View.GONE);
            txtnombre.setText(bolsa.getString("nombre"));
            txturl.setText(bolsa.getString("url"));
            txttelefono.setText(Integer.toString(bolsa.getInt("telefono")));
            txtemail.setText(bolsa.getString("email"));
            txtproductosservicios.setText(bolsa.getString("productosServicios"));
            spnclasificacion.setSelection(listaClasificaciones.indexOf(bolsa.getString("clasificacion")));
        } else{
            //Se llamo desde la pantalla de registrar entonces solo se muestren los botones guardar
            btnborrar.setVisibility(View.GONE);
            btnactualizar.setVisibility(View.GONE);
        }
    }

    //metodo para limpiar todos los campos
    private void limpiarCampos(){
        id = 0;
        txtnombre.setText("");
        txturl.setText("");
        txttelefono.setText("");
        txtemail.setText("");
        txtproductosservicios.setText("");
        spnclasificacion.setSelection(0);
    }

    //Metodo para llenar los datos de la empresa
    private Empresa llenarDatosEmpresa(){
        Empresa empresa = new Empresa();
        String n = txtnombre.getText().toString();
        String u = txturl.getText().toString();
        Integer tel = Integer.parseInt(txttelefono.getText().toString());
        String e = txtemail.getText().toString();
        String pys = txtproductosservicios.getText().toString();
        String clas = spnclasificacion.getSelectedItem().toString().trim();

        empresa.setId(id);
        empresa.setNombre(n);
        empresa.setUrl(u);
        empresa.setTelefono(tel);
        empresa.setEmail(e);
        empresa.setProductosServicios(pys);
        empresa.setClasificacion(clas);

        return empresa;
    }

    //Metodo para guardar o registrar
    private void guardar(){
        empresaBD = new EmpresaBD(contexto, "EmpresasBD.db",null, 1);
        Empresa empresa = llenarDatosEmpresa();
        //Si el id es igual a 0 es senal de que es un libro nuevo
        if (id == 0){
            empresaBD.agregar(empresa);
            Toast.makeText(contexto, "Empresa Guardada :D", Toast.LENGTH_LONG).show();
            limpiarCampos();
        } else {
            //Esta es una actualizacion a algo que ya existe
            empresaBD.actualizar(id, empresa);
            Toast.makeText(contexto, "La Empresa Se Ha Actualizado", Toast.LENGTH_LONG).show();
        }
    }


    //Borrar una empresa de la base de datos
    private void borrar(){
        empresaBD = new EmpresaBD(contexto, "EmpresasBD.db",null, 1);
        //Si el id es igual a 0 es senal de que es un libro nuevo
        if (id == 0){
            Toast.makeText(contexto, "No fue posible borrar", Toast.LENGTH_LONG).show();
        } else {
            //Se borra la empresa con el id con el que se llego a esta pantalla
            empresaBD.borrar(id);
            Toast.makeText(contexto, "La Empresa Se Ha Borrado", Toast.LENGTH_LONG).show();
            btnactualizar.setVisibility(View.GONE);
            btnborrar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ges_btnguardar){
            guardar();
        } else if(viewId == R.id.ges_btnactualizar){
            guardar();
        } else if(viewId == R.id.ges_btnborrar){
            borrar();
        }
    }
}