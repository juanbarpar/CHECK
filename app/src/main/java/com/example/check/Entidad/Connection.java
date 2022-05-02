package com.example.check.Entidad;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection extends AsyncTask <String,String,String>{


    @Override
    protected String doInBackground(String... strings) {
        System.out.println("-------------------------------");
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL("https://api.jsonbin.io/b/624c9ed41a1b610f084c45ff/1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            System.out.println(connection.getResponseCode());
            if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line=reader.readLine())!=null){
                    buffer.append(line);
                    System.out.println(line);

                }
                return buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //1443
        return null;
    }

    public boolean isConnected(){

        return true;
        /*
            try {
                String command = "ping -c 1 google.com";
                return (Runtime.getRuntime().exec(command).waitFor() == 0);
            } catch (Exception e) {
                return false;
            }
*/
    }
}
