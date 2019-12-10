package com.example.beesocial;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
public class AccountFrag extends Fragment {
    // editing users profile under account fragment
    private TextView fName, birthday, gender, favFood;
    private RequestQueue rq;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        fName = view.findViewById(R.id.firstName);
        birthday = view.findViewById(R.id.birthDate);
        gender = view.findViewById(R.id.genderIdentity);
        favFood = view.findViewById(R.id.favoriteFoods);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        //init progress dialog
        //ProgressDialog pd = new ProgressDialog(getActivity());
        rq = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        fName = view.findViewById(R.id.firstName);
        getUserInfo();
        fab.setOnClickListener(v -> showEditProfile());
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getUserInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ID = sharedPreferences.getString("id", "");
        String url = "https://chowmate.herokuapp.com/api/profile/" + ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                fName.setText(response.getString("firstname"));
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
                Instant instant = Instant.from(dateTimeFormatter.parse(response.getString("birthdate")));
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                birthday.setText(localDateTime.toLocalDate().plusDays(1).toString());
                gender.setText(response.getString("sex"));
                favFood.setText(response.getString("favoriteFood"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        rq.add(jsonObjectRequest);
    }
    private void sendJsonRequest() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String ID = sharedPreferences.getString("id", "");
        String url = "https://chowmate.herokuapp.com/api/profile/" + ID;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstname", fName.getText().toString());
            jsonObject.put("birthdate", birthday.getText().toString());
            jsonObject.put("sex", gender.getText().toString());
            jsonObject.put("favoriteFood", favFood.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                response -> Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show(),
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
        rq.add(jsonObjectRequest);
    }
    private void showEditProfile() {
        String[] options = {"Edit Name", "Edit Birthday", "Edit Gender", "Edit Favorite Foods"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showAddItemDialog("Name");
            } else if (which == 1) {
                showAddItemDialog("Birthday");
            } else if (which == 2) {
                showAddItemDialog("Gender");
            } else if (which == 3) {
                showAddItemDialog("Favorite foods");
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
                .setPositiveButton("Update", (dialog1, which) -> {
                    String value = taskEditText.getText().toString().trim();
                    switch (key) {
                        case "Name":
                            fName.setText(value);
                            sendJsonRequest();
                            break;
                        case "Birthday":
                            birthday.setText(value);
                            sendJsonRequest();
                            break;
                        case "Gender":
                            gender.setText(value);
                            sendJsonRequest();
                            break;
                        case "Favorite foods":
                            favFood.setText(value);
                            sendJsonRequest();
                            break;
                    }
                    if (!TextUtils.isEmpty(value)) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put(key, value);
                    } else {
                        Toast.makeText(getActivity(), "Enter" + "", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}




