package com.example.beesocial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    //Global variables for the text fields
    EditText appBirthDate;
    EditText appGenderIdentity;
    EditText appFavoriteFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Grabs the information from the current text fields and sets the global variables to them
        Button profileCreate = findViewById(R.id.profileCreate);
        appBirthDate = findViewById(R.id.birthDate);
        appGenderIdentity = findViewById(R.id.genderIdentity);
        appFavoriteFoods = findViewById(R.id.favoriteFoods);

        profileCreate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProfile();
                    }
                }
        );
    }

    private void updateProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String genderIdentity = appGenderIdentity.getText().toString().trim();
        String favoriteFoods = appFavoriteFoods.getText().toString().trim();
//        Date userDate = null;
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//        try {
//            userDate = format.parse(appBirthDate.getText().toString().trim());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        //get the userID from shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ID = sharedPreferences.getString("id", "");
        String url = "http://10.0.2.2:8888/api/profile/" + ID; //URL where the information will be sent
        System.out.println(url);

    }
}
