package com.example.beesocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText appEmailAddress;
    EditText appPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appEmailAddress = findViewById(R.id.emailAddress);
        appPassword = findViewById(R.id.password);

        //Code that will switch the view to the registration page
        TextView registration = findViewById(R.id.registrationRedirect);
        registration.setMovementMethod(LinkMovementMethod.getInstance());
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        //Code that will switch the view to the login page
        TextView login = findViewById(R.id.loginRedirect);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    //Method to log in a user
    private void loginUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String emailAddress = appEmailAddress.getText().toString().trim();
        String password = appPassword.getText().toString().trim();
        String url = "http://10.0.2.2:8888/api/users/login"; //URL where the information will be sent

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    //If successful, display a Toast confirming login was successful
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String token;
                        String reply = null;
                        try {
                            JSONObject data = new JSONObject(response);
                            reply = data.getString("status");
                            token = data.getString("token");
                            editor.putString("token", token);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                reply,
                                Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(MainActivity.this, testActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                },
                new Response.ErrorListener() {
                    //If it failed, display a Toast explaining why
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "Incorrect credentials!";
//                        try {
//                            String responseBody = new String(error.networkResponse.data, "utf-8");
//                            JSONObject data = new JSONObject(responseBody);
//                            JSONObject data2 = new JSONObject(data.optString("err"));
//                            message = data2.optString("message");
//                        } catch (UnsupportedEncodingException e) {
//
//                        } catch (JSONException e) {
//
//                        }

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
