package com.example.beesocial;


import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

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
        mAdapter= new Adapter(getContext(),items);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);

        jsonrequest();


        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);



    }

    private void jsonrequest() {
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Items item = new Items();
                        item.setTitle(jsonObject.getString("name"));
                        item.setLocation(jsonObject.getString("location"));
                        item.setDate(jsonObject.getString("date"));
                        item.setTime(jsonObject.getString("time"));
                        items.add(item);


                    } catch (JSONException e) {
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

        requestQ = Volley.newRequestQueue(EditEventFrag.this);
        requestQ.add(request);


    }

    private void setuprecycler(ArrayList<Items> items) {

        mAdapter = new Adapter(context, items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);

    }

}
