package com.example.check.repositorio.entidad;

public class Usuario {


    private String nombre, imagen, correo, expedicion;

    public Usuario(String nombre, String imagen, String correo, String expedicion) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.correo = correo;
        this.expedicion = expedicion;

    }

    public Usuario() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getExpedicion() {
        return expedicion;
    }

    public void setExpedicion(String expedicion) {
        this.expedicion = expedicion;
    }
}
