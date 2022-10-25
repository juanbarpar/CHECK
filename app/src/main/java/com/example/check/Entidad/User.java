package com.example.check.Entidad;

public class User {


    private String name, image, email;
    private String expedicion;

    public User(String name, String image, String email, String expedicion) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.expedicion = expedicion;

    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpedicion() {
        return expedicion;
    }

    public void setExpedicion(String expedicion) {
        this.expedicion = expedicion;
    }
}
