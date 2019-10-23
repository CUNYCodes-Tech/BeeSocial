package com.example.beesocial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import java.util.List;

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
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this); //this is important
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

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setOnMyLocationClickListener(this);
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
