package com.example.check.servicio.utilidades.excepciones;

public class ExcepcionConexion extends RuntimeException {
    public ExcepcionConexion(String message) {
        super("Error de coneccion provocado por: " + message);

    }
}