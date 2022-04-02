package com.example.check;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GestionExpediciones {


    public ArrayList<Expedicion> getAll(String response) {

        Expedicion exp = null;

        ArrayList<Expedicion> exps = new ArrayList();
        try {

            try {

                JSONArray jresponse = new JSONArray(response);

                for (int i = 0;i<jresponse.length();i++) {


                    System.out.println(i);
                    JSONObject jsonObject = jresponse.getJSONObject(i);
                    String a = (jsonObject.getString("Expedicion"));
                    String b = (jsonObject.getString("Fechas"));
                    String c = (jsonObject.getString("url"));


                    exps.add(exp);
                    System.out.println(jsonObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.printf("g");
            e.printStackTrace();
        }

        return exps;
    }
}
