package com.example.beesocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Global variables for the text fields
    EditText appEmailAddress;
    EditText appPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Takes the user values from the text fields and saves them
        appEmailAddress = findViewById(R.id.emailAddress);
        appPassword = findViewById(R.id.password);

        //Switches the view to the registration page once the link is clicked
        TextView registration = findViewById(R.id.registrationRedirect);
        registration.setMovementMethod(LinkMovementMethod.getInstance());
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        //Logs in the user once the button is clicked
        TextView login = findViewById(R.id.loginRedirect);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginUser();
                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                startActivity(intent);
            }
        });
    }

    //Method to log in a user
    private void loginUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(this); //Sets up the RequestQueue

        //Formats the values passed from the text fields
        String emailAddress = appEmailAddress.getText().toString().trim();
        String password = appPassword.getText().toString().trim();

        String url = "http://10.0.2.2:8888/api/users/login"; //URL where the information will be sent

        //Sets the behaviors for the POST request sending the user info
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    //If successful, display a Toast confirming login was successful
                    // and saves the authentication token provided by the server
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String token;
                        String id;
                        String reply = null;
                        try {
                            JSONObject data = new JSONObject(response);
                            reply = data.getString("status");
                            id = data.getString("id");
                            token = data.getString("token");
                            editor.putString("token", token);
                            editor.putString("id", id);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                reply,
                                Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                },
                new Response.ErrorListener() {
                    //If it failed, display a Toast explaining why
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "Incorrect credentials!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                message,
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }) {
            //Load the parameters into the request body of the JSON object
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", emailAddress);
                params.put("password", password);
                return params;
            }
        };
        //Fires off to the backend
        requestQueue.add(stringRequest);
    }
}
