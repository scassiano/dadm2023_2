package com.example.sqlite.controladores;

import com.example.sqlite.modelos.Empresa;

import java.util.List;

public interface IEmpresaBD {

    //Definicion de metodos que van a interactuar con la base de datos
    Empresa elemento(int id); //Devuelve el elemento dado su id
    Empresa elemento(String nombre); //Devuelve el elemento dado su titulo exacto
    List<Empresa> lista(); //Devuelve una lista con todos los elementos registrados
    void agregar(Empresa empresa); //Agrega el elemento indicado
    void actualizar(int id, Empresa empresa); //Actualiza datos del elemento dado su id
    void borrar(int id); //Elimina el elemento indicado con el id
}
