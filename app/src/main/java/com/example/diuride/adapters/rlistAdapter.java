package com.example.diuride.adapters;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diuride.Listener.OnRlistSelectListener;
import com.example.diuride.R;

import com.example.diuride.models.OnGoingRideModel;
import com.example.diuride.models.RlistModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Objects;


public class rlistAdapter extends RecyclerView.Adapter<rlistAdapter.MYviewholder> {
    Context context ;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    List<RlistModel> rlistModelList;
    OnRlistSelectListener listener;
    RecyclerView rideListRecyler;
    TextView norideTv;


    public rlistAdapter(Context context, List<RlistModel> rlistModelList, RecyclerView rideListRecyler, TextView norideTv) {
        this.context = context;
        this.rlistModelList = rlistModelList;
        this.rideListRecyler = rideListRecyler;
        this.norideTv = norideTv;
    }

    @NonNull
    @Override
    public MYviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sample_ride_list,parent,false);
        MYviewholder mYviewholder = new MYviewholder(v);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return mYviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MYviewholder holder, int position) {



                   rideListRecyler.setVisibility(View.VISIBLE);
                   norideTv.setVisibility(View.GONE);

               if(!rlistModelList.get(position).getStatus().equals("booked"))
               {
                     holder.itemView.setVisibility(View.VISIBLE);
                  // holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));



                  // String places = rlistModelList.get(position).getStartingpoint() + " To " + rlistModelList.get(position).getDestination();

                   holder.RnameTV.setText(rlistModelList.get(position).getRname());
                   holder.rTimeTV.setText("Time: " + rlistModelList.get(position).getrTime());
                   holder.priceTV.setText("Price: " + rlistModelList.get(position).getrPrice() + " BDT");
                   holder.startPoint.setText(rlistModelList.get(position).getStartingpoint());
                   holder.endPoint.setText(rlistModelList.get(position).getDestination());
                   String url = rlistModelList.get(position).getrDP();
                   Glide.with(context)
                           .load(url)
                           .centerCrop()
                           .placeholder(R.drawable.profilesvg)
                           .into(holder.RproPic);



                   holder.container.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           ///////
                           db.collection("OnGoingRide").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                               @Override
                               public void onSuccess(DocumentSnapshot documentSnapshot) {

                                   OnGoingRideModel onGoingRideModel = new OnGoingRideModel();
                                   onGoingRideModel = documentSnapshot.toObject(OnGoingRideModel.class);
                                   if(onGoingRideModel!=null)
                                   {
                                       Dialog dialog1 = new Dialog(context);
                                       dialog1.setContentView(R.layout.ongoingridealertforadapter);
                                       dialog1.setCancelable(false);
                                       dialog1.show();
                                       AppCompatButton okaybtn = dialog1.findViewById(R.id.Okaybtn);


                                       okaybtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                             //  Intent intent = new Intent(context, UserPolylineActivity.class);
                                               dialog1.dismiss();
                                           }
                                       });

                                   }
                                   else
                                   {
                                       Dialog dialog1 = new Dialog(context);
                                       dialog1.setContentView(R.layout.confirmride_dialog_box);
                                       dialog1.setCancelable(false);
                                       dialog1.show();
                                       AppCompatButton okay = dialog1.findViewById(R.id.conRbtn);
                                       AppCompatButton cancel = dialog1.findViewById(R.id.cancelRbtn);

                                       okay.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               listener = (OnRlistSelectListener) context;
                                               RlistModel selectedRide = new RlistModel();
                                               selectedRide = rlistModelList.get(position);
                                               listener.onRlistCliked(selectedRide);
                                               //  Toast.makeText(context, "done"+rlistModelList.get(position).getRname() ,Toast.LENGTH_SHORT).show();
                                               dialog1.dismiss();
                                           }
                                       });
                                       cancel.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
                                               dialog1.dismiss();
                                           }
                                       });

                                   }

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {

                               }
                           });
                           ////////

                       }
                   });
               }

               else
               {
                   holder.itemView.setVisibility(View.GONE);
                   //norideTv.setVisibility(View.VISIBLE);
                   holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
               }

    }

    @Override
    public int getItemCount() {
        return rlistModelList.size();
    }


    class MYviewholder extends RecyclerView.ViewHolder{
        TextView startPoint,endPoint,priceTV,RnameTV,rTimeTV;
        RoundedImageView RproPic;
        CardView container;

        public MYviewholder(@NonNull View itemView) {
            super(itemView);
            startPoint = itemView.findViewById(R.id.startpoint);
            endPoint = itemView.findViewById(R.id.endpointt);
            rTimeTV = itemView.findViewById(R.id.rTime);
            priceTV = itemView.findViewById(R.id.rPrice);
            RnameTV = itemView.findViewById(R.id.rName);
            RproPic = itemView.findViewById(R.id.pDP);
            container = itemView.findViewById(R.id.listcontainer);
        }
    }


}