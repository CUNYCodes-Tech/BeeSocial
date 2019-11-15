package com.example.beesocial;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFrag extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    Marker mCurrLocationMarker;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        //Sets up everything needed for the map and its markers to display and function
        mMapView = v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            //Checks to see if the user has given the app permission to access the location
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Creates a location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //Actually request a location update
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

        //Sets up the myLocation layer
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Gets all open events and sets markers
        getOpenEvents();

        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        //Grabs the user's ID from the shared preferences
                        SharedPreferences sharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(getContext());
                        String userID = sharedPreferences.getString("id", "");
                        String url = "https://chowmate.herokuapp.com/api/events/addInterest/" + marker.getTag();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Interested", userID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                                url, jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getContext(), "Interested in event!", Toast.LENGTH_SHORT).show();
                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
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
                        requestQueue.add(jsonObjectRequest);
                    }
                }
        );
    }

    //Gets all open events from the backend and displays markers for each one
    //provided that the event does not belong to the user
    private void getOpenEvents() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String url = "https://chowmate.herokuapp.com/api/events"; //URL where the information will be sent
//        String url = "http://10.0.2.2:8888/api/events";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //Grabs the user's ID from the shared preferences
                            SharedPreferences sharedPreferences =
                                    PreferenceManager.getDefaultSharedPreferences(getContext());
                            String userID = sharedPreferences.getString("id", "");

                            //Goes through the events and filters through them
                            for (int i = 0; i < response.length(); i++) {
                                if ((!response.getJSONObject(i).getBoolean("closed")) &&
                                        (!response.getJSONObject(i).getString("createdBy").equals(userID))) {

                                    //Grabs the date and time for the event
                                    java.util.Date date = Date.from(Instant.parse(response.getJSONObject(i).getString("time")));

                                    //Sets a marker
                                    JSONArray coordinates = response.getJSONObject(i)
                                            .getJSONObject("location")
                                            .getJSONArray("coordinates");
                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(coordinates.getDouble(1), coordinates.getDouble(0)))
                                            .title(response.getJSONObject(i).getString("name")));
                                    marker.setSnippet(date.toString());
                                    marker.setTag(response.getJSONObject(i).getString("_id"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    };

}