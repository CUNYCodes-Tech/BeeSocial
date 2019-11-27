package com.example.beesocial;


import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditEventFrag extends Fragment {

    private RecyclerView mRecycler;
    private EventAdapter mEventAdapter;
    private ArrayList<Event> events;

    public EditEventFrag() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_fragment, container, false);
        events = new ArrayList<>();
        mRecycler = v.findViewById(R.id.recyclerView);
        mEventAdapter = new EventAdapter(getContext(), events);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mEventAdapter);

        getEvents();

        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getEvents() {
        String url = "https://chowmate.herokuapp.com/api/events";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    JSONObject jsonObject;
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> address;
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(getContext());
                    String userID = sharedPreferences.getString("id", "");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            if (jsonObject.getString("createdBy").equals(userID)) {
                                Event event = new Event();
                                event.setTitle(jsonObject.getString("name"));
                                event.setEventID(jsonObject.getString("_id"));

                                JSONArray coordinates = jsonObject.getJSONObject("location")
                                        .getJSONArray("coordinates");
                                address = geocoder.getFromLocation(coordinates.getDouble(1),
                                        coordinates.getDouble(0), 1);
                                event.setLocation(address.get(0).getAddressLine(0));

                                Date date = Date.from(Instant.parse(jsonObject.getString("time")));
                                event.setDate(date.toString());

                                ArrayList<User> users = new ArrayList<>();
                                JSONArray interestedUsers = jsonObject.getJSONArray("interested");
                                for (int j = 0; j < interestedUsers.length(); j++) {
                                    users.add(new User());
                                    users.get(j).setUserID(interestedUsers.getString(j));
                                    getUserInfo(users.get(j), interestedUsers.getString(j));
                                }
                                event.setUsers(users);
                                events.add(event);
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    setUpRecycler(events);
                }, Throwable::printStackTrace);

        RequestQueue requestQ = Volley.newRequestQueue(getContext());
        requestQ.add(request);

    }

    private void getUserInfo(User user, String profileID) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //Sets up the RequestQueue

        String url = "https://chowmate.herokuapp.com/api/profile/" + profileID; //URL where the information will be sent

        //Sets the behaviors for the GET request sending the user info
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject userInfo = new JSONObject(response);
                        user.setFirstName(userInfo.getString("firstname"));
                        user.setLastName(userInfo.getString("lastname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace) {
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
        //Fires off to the backend
        requestQueue.add(stringRequest);
    }

    private void setUpRecycler(ArrayList<Event> events) {
        mEventAdapter = new EventAdapter(getContext(), events);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);

    }


}
