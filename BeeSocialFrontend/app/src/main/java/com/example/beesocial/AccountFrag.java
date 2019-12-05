package com.example.beesocial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AccountFrag extends Fragment {


    // editing users profile under account fragment
    TextView fName, birthday, gender, favFood;
    String usersName, usersBirth, genderId, ff;
    /*   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
       String ID = sharedPreferences.getString("id", "");*/
    String url = "https://chowmate.herokuapp.com/api/profile/";

    User user;

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

        sendJsonRequest();


        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                showEditProfile();


            }
        });

        return view;

    }

    public void sendJsonRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    usersName = response.getString("Users Name");

                    fName.setText(usersName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

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
                    //   update("birthday");
                } else if (which == 2) {
                    showAddItemDialog("Gender");
                    // update("gender");
                } else if (which == 3) {
                    showAddItemDialog("Favorite foods");
                    // update("food");
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
                        if (!TextUtils.isEmpty(value)) {
                            pd.show();
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
    }
}






