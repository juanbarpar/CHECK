package com.example.check.servicio.utilidades.excepciones;

public class ExcepcionTareaFB extends RuntimeException {
    public ExcepcionTareaFB(String message) {
        super("Error al recibir la tarea de firebase causada por: " + message);
    }
}