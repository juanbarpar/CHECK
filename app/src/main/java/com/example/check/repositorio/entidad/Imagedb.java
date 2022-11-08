package com.example.check.repositorio.entidad;

import java.util.Objects;

public class Imagedb {
    String date;
    String url;
    String user;
    String expedicion;


    public Imagedb(String date, String user, String url, String expedicion) {
        this.date = date;
        this.url = url;
        this.user = user;
        this.expedicion = expedicion;
    }

    public Imagedb() {}

    public String getExpedicion() {
        return expedicion;
    }

    public void setExpedicion(String expedicion) {
        this.expedicion = expedicion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Imagedb imagedb = (Imagedb) o;
        return Objects.equals(date, imagedb.date) && Objects.equals(url, imagedb.url) && Objects.equals(user, imagedb.user) && Objects.equals(expedicion, imagedb.expedicion);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return date + "," + user + "," + url + "," + expedicion;
    }
}
