package com.example.beesocial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountFrag extends Fragment {


    // editing users profile under account fragment

    TextView fName, birthday, gender, favFood;
    String usersName, usersBirth, genderId, ff;


    FloatingActionButton fab;

    ProgressDialog pd;

    RequestQueue rq;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_fragment, container, false);

        fName = view.findViewById(R.id.firstName);
        birthday = view.findViewById(R.id.birthDate);
        gender = view.findViewById(R.id.genderIdentity);
        favFood = view.findViewById(R.id.favoriteFoods);
        fab = view.findViewById(R.id.fab);

        //init progress dialog
        pd = new ProgressDialog(getActivity());

        rq = Volley.newRequestQueue(getContext());
        fName = view.findViewById(R.id.firstName);

        getUserInfo();

        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                showEditProfile();


            }
        });

        return view;

    }

    public void getUserInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ID = sharedPreferences.getString("id", "");
        String url = "https://chowmate.herokuapp.com/api/profile/" + ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    fName.setText(response.getString("firstname"));
                    birthday.setText(response.getString("birthdate"));
                    gender.setText(response.getString("sex"));
                    favFood.setText(response.getString("favoriteFood"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            //Creates a header with the authentication token
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getContext());
                String token = sharedPreferences.getString("token", "");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        rq.add(jsonObjectRequest);
    }

    public void sendJsonRequest() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ID = sharedPreferences.getString("id", "");
        String url = "https://chowmate.herokuapp.com/api/profile/" + ID;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstname", fName.getText().toString());
            jsonObject.put("birthdate", birthday.getText().toString());
            jsonObject.put("sex",  gender.getText().toString());
            jsonObject.put("favoriteFood", favFood.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            //Creates a header with the authentication token
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getContext());
                String token = sharedPreferences.getString("token", "");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        rq.add(jsonObjectRequest);
    }

    private void showEditProfile() {
        String options[] = {"Edit Name", "Edit Birthday", "Edit Gender", "Edit Favorite Foods"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showAddItemDialog("Name");

                } else if (which == 1) {
                    showAddItemDialog("Birthday");

                } else if (which == 2) {
                    showAddItemDialog("Gender");

                } else if (which == 3) {
                    showAddItemDialog("Favorite foods");

                }
            }
        });
        builder.create().show();

    }

    private void showAddItemDialog(String key) {
        EditText taskEditText = new EditText(getActivity());
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Update " + key)
                .setMessage("Edit " + key)
                .setView(taskEditText)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = taskEditText.getText().toString().trim();
                        if (key.equals("Name")) {
                            fName.setText(value);

                        } else if (key.equals("Birthday")) {
                            birthday.setText(value);

                        } else if (key.equals("Gender")) {
                            gender.setText(value);

                        } else if (key.equals("Favorite foods")) {
                            favFood.setText(value);
                        }

                        if (!TextUtils.isEmpty(value)) {
                            HashMap<String, Object> result = new HashMap<>();
                            result.put(key, value);
                        } else {
                            Toast.makeText(getActivity(), "Enter" + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
        sendJsonRequest();
    }
}






