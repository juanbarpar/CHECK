package com.example.check;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.net.Uri;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import android.content.Intent;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GestionExpediciones gesExp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        gesExp = new GestionExpediciones();
        Connection connection = new Connection();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SmoothBottomBar smoothBottomBar = findViewById(R.id.bar_nav);
        /*
        ViewPager2 locationViewPager = findViewById(R.id.locationViewPager);


        System.out.println(smoothBottomBar.getItemActiveIndex());


        try {
            locationViewPager.setAdapter(new TravelLocationAdapter(gesExp.getAll(connection.execute("").get())));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        */

        System.out.println(connection.isConnected() + "------------");
        replace(new HomeFragment());


        smoothBottomBar.setOnItemSelected((Function1<? super Integer, kotlin.Unit>) o -> {

            System.out.println(o);

            switch (o) {
                case 0:
                    replace(new HomeFragment());
                    break;
                case 1:
                    replace(new SocialFragment());
                    break;
                case 2:
                    replace(new ChatFragment());
                    break;
                case 3:
                    replace(new UserFragment());
                    break;
            }


            return null;
        });

    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frames, fragment);
        transaction.commit();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser == null) {
            goLogin();
        }
    }

    private void goLogin() {


        Intent Log = new Intent(this, LoginActivity.class);
        startActivity(Log);

    }

    private void goWeb(String inURL) {

        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));

        startActivity(browse);

    }

    public void logout(View view) {
        mAuth.signOut();
        goLogin();
    }




    public void getView(View view) {

        KenBurnsView kbvImage;
        TextView textTitle, textLocation, textStartRating;

        System.out.println(view.getTag());

        kbvImage = view.findViewById(R.id.kbvLocation);
        textTitle = view.findViewById(R.id.textTitle);
        textStartRating = view.findViewById(R.id.textStartRating);
        textLocation = view.findViewById(R.id.textLocation);

        System.out.println(textTitle.getText());
        System.out.println(textStartRating.getText());
        System.out.println(textLocation.getText());
        System.out.println(kbvImage.getTag());

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.bt_sheet_dialog
        );

        View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bt_sheet, (LinearLayout) findViewById(R.id.bt_sheet_container)
        );
        bottonSheetView.findViewById(R.id.button_reserva).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                goWeb((String) textTitle.getTag());

                bottomSheetDialog.dismiss();
            }
        });

        KenBurnsView kenBurnsView = bottonSheetView.findViewById(R.id.imageRes);
        TextView textViewexp = bottonSheetView.findViewById(R.id.expedicion);
        TextView textViewlugar = bottonSheetView.findViewById(R.id.lugar);

        bottonSheetView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewe) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Haz parte de nuestras " +
                        "expediciones programadas a distintos lugares de Colombia. " +
                        "Una diversidad de destinos, itinerarios llenos de momentos" +
                        " auténticos y experiencias de vida que recordarás para siempre " + textTitle.getTag());
                try {
                    MainActivity.this.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    System.out.println("Whatsapp have not been installed.");
                }
            }
        });

        textViewexp.setText(textTitle.getText());
        textViewlugar.setText(textLocation.getText());
        Picasso.get().load((String) kbvImage.getTag()).into(kenBurnsView);
        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();
    }


}