package com.example.beesocial;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Sources used:
 * https://www.tutlane.com/tutorial/android/android-login-and-registration-screen-design
 * https://stackoverflow.com/questions/35390928/how-to-send-json-object-to-the-server-from-my-android-app
 * https://www.simplifiedcoding.net/android-volley-tutorial/
 * https://www.kompulsa.com/how-to-send-a-post-request-in-android/
 * https://stackoverflow.com/questions/26167631/how-to-access-the-contents-of-an-error-response-in-volley
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
        String firstName = appFirstName.getText().toString().trim();
        String lastName = appLastName.getText().toString().trim();
        String url = "http://10.0.2.2:8888/api/users/signup";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                response,
                                Toast.LENGTH_LONG);

                        toast.show();
                        System.out.println(response);
                        //code to redirect screen goes here
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String body = null;
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if (error.networkResponse.data != null) {
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                body,
                                Toast.LENGTH_LONG);

                        toast.show();
                        System.out.println(body);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("firstname", firstName);
                params.put("lastname", lastName);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


}
