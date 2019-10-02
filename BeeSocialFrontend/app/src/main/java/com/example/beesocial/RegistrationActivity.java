package com.example.beesocial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


/**
 * Sources used:
 * https://www.tutlane.com/tutorial/android/android-login-and-registration-screen-design
 * https://stackoverflow.com/questions/35390928/how-to-send-json-object-to-the-server-from-my-android-app
 * https://www.simplifiedcoding.net/android-volley-tutorial/
 * https://www.kompulsa.com/how-to-send-a-post-request-in-android/
 * https://stackoverflow.com/questions/26167631/how-to-access-the-contents-of-an-error-response-in-volley
 */

public class RegistrationActivity extends AppCompatActivity {
    //Global variables to be used throughout the activity
    EditText appFirstName;
    EditText appLastName;
    EditText appEmailAddress;
    EditText appPassword;
    EditText appConfirmPassword;

    //Allows the XML page to be rendered
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.registration_page);

        //Grabs all the information from the text fields
        Button registerButton = findViewById(R.id.registerButton);
        appFirstName = findViewById(R.id.firstName);
        appLastName = findViewById(R.id.lastName);
        appEmailAddress = findViewById(R.id.emailAddress);
        appPassword = findViewById(R.id.password);
        appConfirmPassword = findViewById(R.id.confirmPassword);

        //Sets behavior and action to take once the register button has been clicked
        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registerUser();
                    }
                }
        );
    }

    //Method to register a new user
    private void registerUser() {
        //Creates a request queue and takes the global variables' values
        // and saves them to local ones
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String firstName = appFirstName.getText().toString().trim();
        String lastName = appLastName.getText().toString().trim();
        String username = appEmailAddress.getText().toString().trim();
        String password = appPassword.getText().toString().trim();
        String confirmPassword = appConfirmPassword.getText().toString().trim();

        //Displays message if names do not contain only letters
        for (int i = 0; i != firstName.length(); ++i) {
            if (!Character.isLetter(firstName.charAt(i))) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "First name contains non-alphabetic characters!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }

        for (int i = 0; i != lastName.length(); ++i) {
            if (!Character.isLetter(lastName.charAt(i))) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Last name contains non-alphabetic characters!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }


        //Displays message if passwords do not match
        if (!password.equals(confirmPassword)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Password fields do not match!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String url = "http://10.0.2.2:8888/api/users/signup"; //URL where the information will be sent

        //Sends the saved information if passwords match to the server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    //If successful, display a Toast confirming registration went through
                    @Override
                    public void onResponse(String response) {
                        String reply = null;
                        try {
                            JSONObject data = new JSONObject(response);
                            reply = data.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),
                                reply,
                                Toast.LENGTH_LONG);

                        toast.show();
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    //If it failed, display a Toast explaining why
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = null;
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            JSONObject data2 = new JSONObject(data.optString("err"));
                            message = data2.optString("message");
                        } catch (UnsupportedEncodingException e) {

                        } catch (JSONException e) {

                        }

                        Toast toast = Toast.makeText(getApplicationContext(),
                                message,
                                Toast.LENGTH_LONG);

                        toast.show();
                        System.out.println(message);

                    }
                }) {
            //Load the parameters into the request body of the JSON object
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
        //Fires off to the backend
        requestQueue.add(stringRequest);
    }


}
