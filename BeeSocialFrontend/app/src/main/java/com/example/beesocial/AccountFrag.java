package com.example.beesocial;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccountFrag extends Fragment {


    // editing users profile under account fragment
    TextView fName, birthday, gender, favFood;
    ImageView userPhoto, coverPhoto;

    Profile profile;

    FloatingActionButton fab;

    ProgressDialog pd;

//    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//    private String ID = sharedPreferences.getString("id", "");
//    private final String url = "https://chowmate.herokuapp.com/api/profile/" + ID;
//    private JsonArrayRequest request;
//    private RequestQueue requestQ;

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

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showEditProfile();
            }
        });

        return view;

    }

    private void showEditProfile() {
        String options[] = {"Edit Name", "Edit Birthday", "Edit Gender", "Edit Favorite Foods"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //edit name clicked
                } else if (which == 1) {
                    //edit birthday clicked
                } else if (which == 2) {
                    //edit gender clicked
                } else if (which == 3) {
                    //edit fav foods clicked
                }
            }
        });
        builder.create().show();

    }


}
