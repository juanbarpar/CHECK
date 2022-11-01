package com.example.check.repositorio.dao;

import com.example.check.repositorio.entidad.Imagedb;

import java.util.ArrayList;
import java.util.List;

public class ImagenDao {

    public static List<Imagedb> imagedbs = new ArrayList<>();
    public static void addImage(Imagedb image){
        imagedbs.add(0,image);
    }

}
