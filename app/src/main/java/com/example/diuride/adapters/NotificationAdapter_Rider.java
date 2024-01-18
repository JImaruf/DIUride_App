package com.example.diuride.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diuride.R;
import com.example.diuride.UserPolylineActivity2;
import com.example.diuride.emptyOngoingmsg;
import com.example.diuride.models.RiderNotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import soup.neumorphism.NeumorphButton;

public class NotificationAdapter_Rider extends RecyclerView.Adapter<NotificationAdapter_Rider.MyViewHolder>{
    
    Context context;
    List<RiderNotificationModel> list;
    TextView notiAvailablemsg;
    ImageView Icon;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    public NotificationAdapter_Rider(Context context, List<RiderNotificationModel> list, TextView notiAvailablemsg, ImageView icon) {
        this.context = context;
        this.list = list;
        this.notiAvailablemsg = notiAvailablemsg;
        Icon = icon;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_for_rider,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(list!=null){
            notiAvailablemsg.setVisibility(View.GONE);
            Icon.setVisibility(View.GONE);

            holder.passenger_name.setText(list.get(position).getPname());
            Glide.with(context)
                    .load(list.get(position).getPdp())
                    .centerCrop()
                    .placeholder(R.drawable.profilesvg)
                    .into(holder.PproPic);


            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDB.collection("OnGoingRide").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                              RiderNotificationModel riderNotificationModel =  task.getResult().toObject(RiderNotificationModel.class);
                              if(riderNotificationModel!=null)
                              {
                                  Intent intent = new Intent(context,UserPolylineActivity2.class);
                                  context.startActivity(intent);
                              }
                              else
                              {
                                  Toast.makeText(context, "Time Over!", Toast.LENGTH_SHORT).show();
                                  Intent intent = new Intent(context, emptyOngoingmsg.class);
                                  context.startActivity(intent);
                              }
                            }

                        }
                    });


//                    Dialog dialog1 = new Dialog(context);
//                    dialog1.setContentView(R.layout.getpassengerbox);
//                    dialog1.setCancelable(false);
//                    dialog1.show();
//                    AppCompatButton getPassenger = dialog1.findViewById(R.id.getPassengerbtn);
//                    AppCompatButton cancelRide = dialog1.findViewById(R.id.cancelRidebyRiderbtn);
//                    getPassenger.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Intent intent = new Intent(context,UserPolylineActivity2.class);
//                            context.startActivity(intent);
//                            //  Toast.makeText(context, "done"+rlistModelList.get(position).getRname() ,Toast.LENGTH_SHORT).show();
//                            dialog1.dismiss();
//                        }
//                    });
//                    cancelRide.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            //send notification to the rider.
////                            mDB.collection("Users").document(list.get(position).getPuid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////                                @Override
////                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                                    UserModel ob = new UserModel();
////                                    if(task.isSuccessful())
////                                    {
////                                        ob = task.getResult().toObject(UserModel.class);
////                                        if(ob!=null) {
////
////
////                                            FcmNotificationSender notificationSender = new FcmNotificationSender(ob.getFCMtoken(),"Ride Canceled","Rider Canceled the Ride Offer",,"editPro");
////                                            notificationSender.SendNotification();
////                                            mDB.collection("OnGoingRide").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                @Override
////                                                public void onComplete(@NonNull Task<Void> task) {
////                                                    if(task.isSuccessful())
////                                                    {
////                                                        //start a new activity for give thanks
////                                                    }
////
////                                                }
////                                            }).addOnFailureListener(new OnFailureListener() {
////                                                @Override
////                                                public void onFailure(@NonNull Exception e) {
////
////                                                }
////                                            });
////                                            mDB.collection("OnGoingRide").document(riderID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                @Override
////                                                public void onComplete(@NonNull Task<Void> task) {
////                                                    if(task.isSuccessful())
////                                                    {
////
////                                                    }
////
////                                                }
////                                            }).addOnFailureListener(new OnFailureListener() {
////                                                @Override
////                                                public void onFailure(@NonNull Exception e) {
////
////                                                }
////                                            });
////
////
////                                            //Toast.makeText(MainActivity.this, "joss bro", Toast.LENGTH_SHORT).show();
////                                            Intent intent = new Intent(UserPolylineActivity.this, MainActivity.class);
////                                            startActivity(intent);
////                                        }
////                                    }
////
////                                }
////                            });
////
////
////
////                            //  Toast.makeText(context, "done"+rlistModelList.get(position).getRname() ,Toast.LENGTH_SHORT).show();
////                            dialog1.dismiss();
////                            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView passenger_name;
        RoundedImageView PproPic;
        CardView container;
        NeumorphButton getPass,cancelRide;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            passenger_name = itemView.findViewById(R.id.passName);
            PproPic = itemView.findViewById(R.id.pDP);
            container = itemView.findViewById(R.id.notification_rider_container);
            getPass = itemView.findViewById(R.id.getPassBTN);
            cancelRide = itemView.findViewById(R.id.cancelRbtn);
        }
    }

}
