package com.example.demo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {

    Context context;
    ArrayList<SmsModel> list;





    public SmsAdapter(Context context, ArrayList<SmsModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position) {
        SmsModel m = list.get(position);

        holder.txtNumber.setText(m.number);
        holder.txtMessage.setText(m.message);
        holder.txtTime.setText(m.time);

        // LOOP-WISE COLOR (NOT RANDOM)
        int[] circleColors = {
                Color.parseColor("#F44336"), // Red
                Color.parseColor("#4CAF50"), // Green
                Color.parseColor("#2196F3"), // Blue
                Color.parseColor("#FF9800"), // Orange
                Color.parseColor("#9C27B0")  // Purple
        };

        int color = circleColors[position % circleColors.length];

        holder.imgPerson.setBackgroundTintList(
                ColorStateList.valueOf(color)
        );
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPerson;
        TextView txtNumber, txtMessage, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPerson = itemView.findViewById(R.id.imgPerson);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }

}
