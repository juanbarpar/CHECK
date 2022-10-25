package com.example.check.Gestion;

import com.example.check.Entidad.Imagedb;

import java.util.ArrayList;
import java.util.List;

public class GestionImage {

    public static List<Imagedb> imagedbs = new ArrayList<>();
    public static void addImage(Imagedb image){
        imagedbs.add(0,image);
    }



}
