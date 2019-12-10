package com.example.beesocial;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Holder> {

    private RecyclerView userView;
    private UserAdapter userAdapter;

    private ArrayList<Event> events;
    private Context mContext;
    private Dialog mDialog;
    private FragmentManager fragmentManager;

    EventAdapter(Context mContext, ArrayList<Event> events, FragmentManager fragmentManager) {
        this.events = events;
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.event_cards, parent, false);
        Holder myHolder = new Holder(view);

        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.event_status_button);

        myHolder.card_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = events.get(myHolder.getAdapterPosition());
                TextView dialog_title = mDialog.findViewById(R.id.clicked_item_title);
                TextView dialog_loc = mDialog.findViewById(R.id.clicked_item_location);
                TextView dialog_date = mDialog.findViewById(R.id.clicked_item_date);
                TextView dialog_time = mDialog.findViewById(R.id.clicked_item_time);
                userView = mDialog.findViewById(R.id.users);

                dialog_title.setText(event.getTitle());
                dialog_loc.setText(event.getLocation());
                dialog_date.setText(event.getDate());
                dialog_time.setText(event.getTime());
                setUpUserRecycler(mContext, event.getUsers(), event);
                userView.setAdapter(userAdapter);

                mDialog.show();
            }
        });

        return myHolder;
    }

    private void deleteEvent(String eventID) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "https://chowmate.herokuapp.com/api/events/" + eventID; //URL where the information will be sent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("eventId", eventID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, jsonObject,
                response -> {
                    Toast.makeText(mContext, "Event deleted!", Toast.LENGTH_SHORT).show();
                    fragmentManager.beginTransaction()
                            .replace(R.id.edit_fragment, new EditEventFrag())
                            .commit();
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {

            //Creates a header with the authentication token
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(mContext);
                String token = sharedPreferences.getString("token", "");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

        };

        requestQueue.add(jsonObjectRequest);
    }

    private void setUpUserRecycler(Context mContext, ArrayList<User> users, Event event) {
        userAdapter = new UserAdapter(users, mContext, event);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        userView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.event_name.setText(events.get(position).getTitle());
        holder.event_loc.setText(events.get(position).getLocation());
        holder.event_date.setText(events.get(position).getDate());
        holder.event_time.setText(events.get(position).getTime());
        holder.delete_event.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteEvent(events.get(position).getEventID());
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        if (events == null)
            return 0;
        return events.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        CardView card_list;
        TextView event_name;
        TextView event_loc;
        TextView event_date;
        TextView event_time;
        Button delete_event;

        Holder(@NonNull View itemView) {
            super(itemView);

            card_list = itemView.findViewById(R.id.card_view);
            event_name = itemView.findViewById(R.id.item_title);
            event_loc = itemView.findViewById(R.id.item_location);
            event_date = itemView.findViewById(R.id.item_date);
            event_time = itemView.findViewById(R.id.item_time);
            delete_event = itemView.findViewById(R.id.delete_button);
        }


//        public void setTitle(String title) {
//            event_name.setText(title);
//        }

        public void setLocation(String location) {
            event_loc.setText(location);
        }

//        public void setDate(String date) {
//            event_date.setText(date);
//        }
//
//        public void setTime(String time) {
//            event_time.setText(time);
//        }


    }


}
