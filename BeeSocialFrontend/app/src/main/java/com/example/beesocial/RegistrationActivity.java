package com.example.beesocial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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


/**
 * Sources used:
 * https://www.tutlane.com/tutorial/android/android-login-and-registration-screen-design
 * https://stackoverflow.com/questions/35390928/how-to-send-json-object-to-the-server-from-my-android-app
 * https://www.simplifiedcoding.net/android-volley-tutorial/
 * https://www.kompulsa.com/how-to-send-a-post-request-in-android/
 */

public class RegistrationActivity extends AppCompatActivity {
    EditText appFirstName;
    EditText appLastName;
    EditText appEmailAddress;
    EditText appPassword;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.registration_page);

        Button registerButton = findViewById(R.id.registerButton);
        appFirstName = findViewById(R.id.firstName);
        appLastName = findViewById(R.id.lastName);
        appEmailAddress = findViewById(R.id.emailAddress);
        appPassword = findViewById(R.id.password);

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registerUser();
                    }
                }
        );
    }

    private void registerUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String username = appEmailAddress.getText().toString().trim();
        String password = appPassword.getText().toString().trim();
        String url = "http://10.0.2.2:8888/api/users";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


}
