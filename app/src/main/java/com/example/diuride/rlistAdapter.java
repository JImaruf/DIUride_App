package com.example.diuride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class rlistAdapter extends RecyclerView.Adapter<rlistAdapter.myViewHolder>{

    Context context;
    List<RlistModel> rlists;

    public rlistAdapter(Context context, List<RlistModel> rlists) {
        this.context = context;
        this.rlists = rlists;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_ride_list,parent,false);
        myViewHolder myViewHolder = new myViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.destination.setText(rlists.get(position).getDestination());
        holder.rPrice.setText(rlists.get(position).getrPrice());
        holder.rTime.setText(rlists.get(position).getrTime());
        holder.rDP.setImageResource(rlists.get(position).rDP);
        holder.Rname.setText(rlists.get(position).getRname());

    }

    @Override
    public int getItemCount() {
        return rlists.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView Rname;
        TextView destination;
        RoundedImageView rDP;
        TextView rPrice;
        TextView rTime;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            Rname = itemView.findViewById(R.id.rName);
            destination = itemView.findViewById(R.id.destination);
            rDP = itemView.findViewById(R.id.rDp);
            rPrice = itemView.findViewById(R.id.rPrice);
            rTime = itemView.findViewById(R.id.rTime);

        }
    }

}

