package com.example.sqlite.modelos;

public class Empresa {
    //Clase que permite estructurar los atributos de una
    //Empresa de software para manejarlos en la tabla

    //Se definen todos los atributos
    private int id;
    private String nombre;
    private String url;
    private int telefono;
    private String email;
    private String productosServicios;
    private String clasificacion;

    //Constructor vacio
    public Empresa() {
    }

    //Constructor con argumentos
    public Empresa(int id, String nombre, String url, int telefono, String email, String productosServicios, String clasificacion) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
        this.telefono = telefono;
        this.email = email;
        this.productosServicios = productosServicios;
        this.clasificacion = clasificacion;
    }

    //Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProductosServicios() {
        return productosServicios;
    }

    public void setProductosServicios(String productosServicios) {
        this.productosServicios = productosServicios;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    //toString
    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", url='" + url + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                ", productosServicios='" + productosServicios + '\'' +
                ", clasificacion='" + clasificacion + '\'' +
                '}';
    }
}
