package com.example.check.Gestion;

import com.example.check.Entidad.TravelLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GestionExpediciones {


    public List<TravelLocation> getAll(String response) {

        List<TravelLocation> travelLocations = new ArrayList<>();

        try {

            try {

                JSONArray jresponse = new JSONArray(response);

                for (int i = 0;i<jresponse.length();i++) {
                    JSONObject jsonObject = jresponse.getJSONObject(i);
                    TravelLocation travelLocation = new TravelLocation();
                    travelLocation.imageUrl = (jsonObject.getString("imagen"));
                    travelLocation.title = (jsonObject.getString("Nombre"));
                    travelLocation.location = (jsonObject.getString("ubicación"));
                    travelLocation.startRating = (jsonObject.getString("fecha"));
                    travelLocation.url = (jsonObject.getString("url"));

                    travelLocations.add(travelLocation);


                    System.out.println(jsonObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.printf("g");
            e.printStackTrace();
        }

        return travelLocations;
    }
}
