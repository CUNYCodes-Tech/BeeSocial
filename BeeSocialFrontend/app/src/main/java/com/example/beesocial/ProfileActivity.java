/**
 * Sources used:
 */
package com.example.beesocial;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

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

        //Sets the behavior for when the button is clicked
        profileCreate.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        updateProfile();
                    }
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateProfile() {
        //Creates the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Grabs the user information from the text fields
        String genderIdentity = appGenderIdentity.getText().toString().trim();
        String favoriteFoods = appFavoriteFoods.getText().toString().trim();

        //Grabs the user birthdate and calculates the difference between then and now
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthdate = LocalDate.parse(appBirthDate.getText().toString().trim(), formatter);
        LocalDate currentDate = LocalDate.now();
        Long years = ChronoUnit.YEARS.between(birthdate, currentDate);
        String age = Long.toString(years);

        //Gets the user ID from Shared Preferences and creates a path to update
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ID = sharedPreferences.getString("id", "");
        String url = "http://10.0.2.2:8888/api/profile/" + ID; //URL where the information will be sent

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Profile updated!",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            //Creates a header with the authentication token
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String token = sharedPreferences.getString("token", "");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            //Creates the parameters for the request body
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("age", age);
                params.put("sex", genderIdentity);
                params.put("description", favoriteFoods);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}