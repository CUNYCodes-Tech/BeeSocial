/*
  Sources used:
  https://www.tutlane.com/tutorial/android/android-login-and-registration-screen-design
  https://stackoverflow.com/questions/35390928/how-to-send-json-object-to-the-server-from-my-android-app
  https://www.simplifiedcoding.net/android-volley-tutorial/
  https://www.kompulsa.com/how-to-send-a-post-request-in-android/
  https://stackoverflow.com/questions/26167631/how-to-access-the-contents-of-an-error-response-in-volley
 */

package com.example.beesocial;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    //Global variables to be used throughout the activity
    EditText appFirstName;
    EditText appLastName;
    EditText appEmailAddress;
    EditText appPassword;
    EditText appConfirmPassword;

    //Allows the XML page to be rendered
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_registration);

        //Grabs all the information from the text fields
        Button registerButton = findViewById(R.id.registerButton);
        appFirstName = findViewById(R.id.firstName);
        appLastName = findViewById(R.id.lastName);
        appEmailAddress = findViewById(R.id.emailAddress);
        appPassword = findViewById(R.id.password);
        appConfirmPassword = findViewById(R.id.confirmPassword);

        //Sets behavior and action to take once the register button has been clicked
        registerButton.setOnClickListener(
                v -> registerUser()
        );
    }

    //Method to register a new user
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
        String url = "https://chowmate.herokuapp.com/api/users/signup"; //URL where the information will be sent

        //Sends the saved information if passwords match to the server
        //If successful, display a Toast confirming registration went through
        //If it failed, display a Toast explaining why
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
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

                },
                error -> {
                    String message = null;
                    try {
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        JSONObject data = new JSONObject(responseBody);
                        JSONObject data2 = new JSONObject(data.optString("err"));
                        message = data2.optString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getApplicationContext(),
                            message,
                            Toast.LENGTH_LONG);
                    toast.show();
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
