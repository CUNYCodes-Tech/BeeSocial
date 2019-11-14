package com.example.beesocial;


import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditEventFrag extends Fragment {

    private RecyclerView mRecycler;
    private Adapter mAdapter;
    private ArrayList<Items> items;
    private final String JSON_URL = "https://chowmate.herokuapp.com/api/events";
    private JsonArrayRequest request;
    private RequestQueue requestQ;
    View v;

    public EditEventFrag() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.edit_fragment, container, false);
        items = new ArrayList<>();
        mRecycler = v.findViewById(R.id.recyclerView);
        mAdapter = new Adapter(getContext(), items);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);

        jsonrequest();

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void jsonrequest() {
        request = new JsonArrayRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject = null;
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> address;

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                jsonObject = response.getJSONObject(i);
                                Items item = new Items();
                                item.setTitle(jsonObject.getString("name"));
                                JSONArray coordinates = jsonObject.getJSONObject("location")
                                        .getJSONArray("coordinates");
                                address = geocoder.getFromLocation(coordinates.getDouble(1),
                                        coordinates.getDouble(0), 1);
                                item.setLocation(address.get(0).getAddressLine(0));
                                //LocalDateTime date;
                                LocalDateTime time;

                                java.util.Date date = Date.from(Instant.parse(jsonObject.getString("time")));
                                item.setDate(date.toString());

                                //item.setDate(jsonObject.getString("date"));
                                //item.setTime(jsonObject.getString("time"));
                                items.add(item);


                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }

                        setuprecycler(items);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        requestQ = Volley.newRequestQueue(getContext());
        requestQ.add(request);


    }

    private void setuprecycler(ArrayList<Items> items) {

        mAdapter = new Adapter(getContext(), items);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);

    }




}
