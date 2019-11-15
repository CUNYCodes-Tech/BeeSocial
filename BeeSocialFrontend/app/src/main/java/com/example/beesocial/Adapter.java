package com.example.beesocial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    ArrayList<Items> items;
    Context mContext;
    Dialog mDialog;
    ArrayList<Users> users;

    public Adapter(Context mContext, ArrayList<Items> items) {
        this.items = items;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.event_cards, parent, false);
        Holder myHolder = new Holder(view);

        mDialog= new Dialog(mContext);
        mDialog.setContentView(R.layout.event_status_button);


      myHolder.card_list.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              TextView dialog_title= (TextView) mDialog.findViewById(R.id.clicked_item_title);
              TextView dialog_loc= (TextView) mDialog.findViewById(R.id.clicked_item_location);
              TextView dialog_date= (TextView) mDialog.findViewById(R.id.clicked_item_date);
              TextView dialog_time=(TextView) mDialog.findViewById(R.id.clicked_item_time);

              dialog_title.setText(items.get(myHolder.getAdapterPosition()).getTitle());
              dialog_loc.setText(items.get(myHolder.getAdapterPosition()).getLocation());
              dialog_date.setText(items.get(myHolder.getAdapterPosition()).getDate());
              dialog_time.setText(items.get(myHolder.getAdapterPosition()).getTime());

              mDialog.show();
          }
      });

        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.event_name.setText(items.get(position).getTitle());
        holder.event_loc.setText(items.get(position).getLocation());
        holder.event_date.setText(items.get(position).getDate());
        holder.event_time.setText(items.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }






    public class Holder extends RecyclerView.ViewHolder {

        CardView card_list;
        TextView event_name;
        TextView event_loc;
        TextView event_date;
        TextView event_time;

        public Holder(@NonNull View itemView) {
            super(itemView);

            card_list= (CardView) itemView.findViewById(R.id.card_view);
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
