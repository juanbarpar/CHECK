package com.example.check.repositorio.entidad;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DestinosViaje {
    public String Nombre, ubicación, imagen, url;
    public String fecha;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinosViaje that = (DestinosViaje) o;
        return Objects.equals(Nombre, that.Nombre) && Objects.equals(ubicación, that.ubicación) && Objects.equals(imagen, that.imagen) && Objects.equals(url, that.url) && Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Nombre, ubicación, imagen, url, fecha);
    }

    @NonNull
    @Override
    public String toString() {
        return "TravelLocation{" +
                "Nombre='" + Nombre + '\'' +
                ", ubicación='" + ubicación + '\'' +
                ", imagen='" + imagen + '\'' +
                ", url='" + url + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}