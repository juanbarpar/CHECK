package com.example.check;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GestionExpediciones gesExp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        gesExp = new GestionExpediciones();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 locationViewPager = findViewById(R.id.locationViewPager);
        List<TravelLocation> travelLocations = new ArrayList<>();

        TravelLocation travelLocationFi = new TravelLocation();
        travelLocationFi.imageUrl = "https://checknewplaces.com/wp-content/uploads/2022/03/Banner-fotos-Check-11.jpg";
        travelLocationFi.title = "En Busca del Paraíso Teyuna";
        travelLocationFi.location = "Ciudad Perdida - Santa Marta";
        travelLocationFi.startRating = "14 al 18 de abril de 2022";
        travelLocations.add(travelLocationFi);
        TravelLocation travelLocationS = new TravelLocation();
        travelLocationS.imageUrl = "https://checknewplaces.com/wp-content/uploads/2022/03/4-4.jpg";
        travelLocationS.title = "En Busca del Safari Marino";
        travelLocationS.location = "Nuquí - Chocó";
        travelLocationS.startRating = "14 al 18 de abril de 2022";
        travelLocations.add(travelLocationS);
        TravelLocation travelLocationSe = new TravelLocation();
        travelLocationSe.imageUrl = "https://checknewplaces.com/wp-content/uploads/2022/03/3-4.jpg";
        travelLocationSe.title = "En Busca de Perezosos";
        travelLocationSe.location = "Puerto Inirída - Guainía";
        travelLocationSe.startRating = "14 al 18 de abril de 2022";
        travelLocations.add(travelLocationSe);
        locationViewPager.setAdapter(new TravelLocationAdapter(travelLocations));
        locationViewPager.setClipToPadding(false);
        locationViewPager.setClipChildren(false);
        locationViewPager.setOffscreenPageLimit(3);
        locationViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.90f + r * 0.04f);
            }
        });
        locationViewPager.setPageTransformer(compositePageTransformer);




        Connection connection = new Connection();
        System.out.println(connection.isConnected() + "------------");

        if(true){

            try {
                String response = connection.execute("").get();
                gesExp.getAll(response);


                JSONArray jresponse = new JSONArray(response);

                for (int i = 0;i<jresponse.length();i++){
                    System.out.println(i);
                    JSONObject jsonObject = jresponse.getJSONObject(i);
                    System.out.println(jsonObject);

                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {



        }

    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            goLogin();
        }
    }

    private void goLogin() {

        Intent Log = new Intent(this, LoginActivity.class);
        startActivity(Log);

    }
    public void logout(View view){
        mAuth.signOut();
        goLogin();
    }

}