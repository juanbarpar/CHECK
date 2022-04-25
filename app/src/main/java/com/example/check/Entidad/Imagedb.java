package com.example.check.Entidad;

import java.util.Objects;

public class Imagedb {
    String date;
    String url;
    String user;


    public Imagedb() {

    }

    public Imagedb(String date, String url, String user) {

        this.date = date;
        this.url = url;
        this.user = user;
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

        return Objects.equals(date, imagedb.date) && Objects.equals(url, imagedb.url) && Objects.equals(user, imagedb.user);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
