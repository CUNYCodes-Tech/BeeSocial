package com.example.beesocial;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Holder> {

    private RecyclerView userView;
    private UserAdapter userAdapter;

    private ArrayList<Event> events;
    private Context mContext;
    private Dialog mDialog;

    public EventAdapter(Context mContext, ArrayList<Event> events) {
        this.events = events;
        this.mContext = mContext;
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
                TextView dialog_title = (TextView) mDialog.findViewById(R.id.clicked_item_title);
                TextView dialog_loc = (TextView) mDialog.findViewById(R.id.clicked_item_location);
                TextView dialog_date = (TextView) mDialog.findViewById(R.id.clicked_item_date);
                TextView dialog_time = (TextView) mDialog.findViewById(R.id.clicked_item_time);
                userView = (RecyclerView) mDialog.findViewById(R.id.users);

                dialog_title.setText(event.getTitle());
                dialog_loc.setText(event.getLocation());
                dialog_date.setText(event.getDate());
                dialog_time.setText(event.getTime());
                setUpUserRecycler(mContext, event.getUsers());
                userView.setAdapter(userAdapter);
                //event.getUsers();

                mDialog.show();
            }
        });

        return myHolder;
    }

    private void setUpUserRecycler(Context mContext, ArrayList<User> users) {
        userAdapter = new UserAdapter(users, mContext);
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

        public Holder(@NonNull View itemView) {
            super(itemView);

            card_list = (CardView) itemView.findViewById(R.id.card_view);
            event_name = (TextView) itemView.findViewById(R.id.item_title);
            event_loc = (TextView) itemView.findViewById(R.id.item_location);
            event_date = (TextView) itemView.findViewById(R.id.item_date);
            event_time = (TextView) itemView.findViewById(R.id.item_time);
        }


        public void setTitle(String title) {
            event_name.setText(title);
        }

        public void setLocation(String location) {
            event_loc.setText(location);
        }

        public void setDate(String date) {
            event_date.setText(date);
        }

        public void setTime(String time) {
            event_time.setText(time);
        }


    }


}
