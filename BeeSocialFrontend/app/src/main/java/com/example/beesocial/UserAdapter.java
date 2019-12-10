package com.example.beesocial;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {
    private Event event;
    private ArrayList<User> users;
    private Context context;
    Dialog dialog;

    UserAdapter(ArrayList<User> users, Context context, Event event) {
        this.users = users;
        this.context = context;
        this.event = event;
    }

    @NonNull
    @Override
    public UserAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.interested_user, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.Holder holder, int position) {
        String userName = users.get(position).getFirstName() + " " + users.get(position).getLastName();
        holder.user.setText(userName);
        holder.user.setOnClickListener(v -> {
            String url = "https://chowmate.herokuapp.com/api/events/invite/" + event.getEventID();
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("person", users.get(position).getUserID());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                    response -> Toast.makeText(context, "User confirmed!", Toast.LENGTH_SHORT).show(),
                    Throwable::printStackTrace) {
                //Creates a header with the authentication token
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    String token = sharedPreferences.getString("token", "");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        });
    }

    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        return users.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView user;

        Holder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.userInfo);
        }
    }
}
