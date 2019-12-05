package com.example.beesocial;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class CreateEventFrag extends Fragment {

    private EditText appNameOfEvent;
    private Place eventLocation;
    private EditText appPickDate;
    private EditText appPickTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_fragment, container, false);

        Button createEventSubmit = v.findViewById(R.id.createEventSubmit);
        appNameOfEvent = v.findViewById(R.id.nameOfEvent);
        appPickDate = v.findViewById(R.id.pickDate);
        appPickTime = v.findViewById(R.id.pickTime);

        // Initialize the SDK
        Places.initialize(Objects.requireNonNull(getContext()), "AIzaSyAQ5csIa51u78zaFa4_vnUJnyA78e1qcbE");

        // Create a new Places client instance
        //PlacesClient placesClient = Places.createClient(getContext());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                eventLocation = place;
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Sets the behavior for when the button is clicked
        createEventSubmit.setOnClickListener(
                v1 -> submitEvent()
        );

        Button timePicker = v.findViewById(R.id.TimePicker);
        timePicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void submitEvent() {
        //Creates the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Gets the user ID from Shared Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ID = sharedPreferences.getString("id", "");

        //Checks if the event name is empty
        if (appNameOfEvent.getText().toString().isEmpty() ||
                appNameOfEvent == null) {
            Toast toast = Toast.makeText(getContext(),
                    "Please enter a name for the event!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //Checks if the event location is null
        if (eventLocation == null) {
            Toast toast = Toast.makeText(getContext(),
                    "Please select a location!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //Checks if the event date is blank
        if (appPickDate.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getContext(),
                    "Date is blank!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //Checks if the event time is blank
        if (appPickTime.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getContext(),
                    "Time is blank!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //Grabs the user information from the text fields
        String nameOfEvent = appNameOfEvent.getText().toString().trim();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDate date = LocalDate.parse(appPickDate.getText().toString().trim(), dateFormat);
        LocalTime time = LocalTime.parse(appPickTime.getText().toString().trim(), timeFormat);
        LocalDateTime eventTimeDate = LocalDateTime.of(date, time);

        //Sets up the JSON Objects to be sent.
        JSONObject params = new JSONObject();
        JSONObject locationBody = new JSONObject();
        JSONArray coordinates = new JSONArray();

        //Adds parameters to the appropriate objects
        try {
            params.put("name", nameOfEvent);
            params.put("createdBy", ID);
            params.put("time", eventTimeDate);
            locationBody.put("type", "Point");
            coordinates.put(Objects.requireNonNull(eventLocation.getLatLng()).longitude);
            coordinates.put(eventLocation.getLatLng().latitude);
            locationBody.put("coordinates", coordinates);
            params.put("location", locationBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://chowmate.herokuapp.com/api/events"; //URL where the information will be sent
//        String url = "http://10.0.2.2:8888/api/events";

        // TODO: Handle error
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, params,
                        response -> {
                            Toast toast = Toast.makeText(getContext(),
                                    "Event created!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            assert getFragmentManager() != null;
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFrag()).commit();
                        }, Throwable::printStackTrace) {
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

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    }

    public static class DateTimePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        }
    }

}
