package com.example.diuride;

import static android.content.Context.ALARM_SERVICE;




import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diuride.BackServices.RideDeleteService;
import com.example.diuride.databinding.FragmentOfferRideBinding;
import com.example.diuride.models.OnGoingRideModel;
import com.example.diuride.models.RlistModel;
import com.example.diuride.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import soup.neumorphism.NeumorphButton;


public class offerRideFragment extends Fragment {

    Calendar calendar ;
    int deleterideReqCode = 1009;
    EditText timeEt,leavingEt,priceEt,endLocEt;
    FirebaseFirestore mDB;
    FirebaseAuth mAuth;
    NeumorphButton offerRIdebtn,editBtn,delBtn;
    UserModel RiderInfo;
    RlistModel rlistModel;
    String rideStatus;
    String Rtime,Rleavingfrom,Rprice,Rdestination,Rname,Rimage;
    TextView statusTv;
    ImageView logo;
    long rideTimeinMili=0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_offer_ride, container, false);
        timeEt = v.findViewById(R.id.timeet);
        leavingEt = v.findViewById(R.id.departureplace);
        priceEt = v.findViewById(R.id.priceet);
        endLocEt = v.findViewById(R.id.endpoint);
        offerRIdebtn = v.findViewById(R.id.ofrRidebtn);
        editBtn = v.findViewById(R.id.editOffer);
        delBtn = v.findViewById(R.id.delOfferbtn);
        logo = v.findViewById(R.id.bikelogo);
        statusTv = v.findViewById(R.id.tv1);
        rlistModel = new RlistModel();
         RiderInfo = new UserModel();

        mDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
       AlarmManager alarmManager = (AlarmManager) v.getContext().getSystemService(ALARM_SERVICE);


//        SharedPreferences prefs = getActivity().getSharedPreferences("ride_status", MODE_PRIVATE);
//        String currentStatus = prefs.getString("status","");
        mDB.collection("RideList").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                RlistModel ride = new RlistModel();
                if(task.isSuccessful())
                {

                    ride = task.getResult().toObject(RlistModel.class);
                    if(ride!=null)
                    {
                        timeEt.setText(ride.getrTime());
                        endLocEt.setText(ride.getDestination());
                        leavingEt.setText(ride.getStartingpoint());
                        priceEt.setText(ride.getrPrice());
                        offerRIdebtn.setVisibility(View.GONE);
//                          logo.setVisibility(View.GONE);
                        editBtn.setVisibility(View.VISIBLE);
                        delBtn.setVisibility(View.VISIBLE);
                        statusTv.setText("Your offer on live,Please wait some time for Passenger");
//                        //setTimer for delete ride
//
//                        Intent iBroadcast = new Intent(v.getContext(), RideDeleteService.class);
//                        PendingIntent pi = PendingIntent.getBroadcast(v.getContext(),deleterideReqCode,iBroadcast, PendingIntent.FLAG_IMMUTABLE);
//
//                        alarmManager.set(AlarmManager.RTC_WAKEUP,rideTimeinMili,pi);
//                        //

                    }


                }

            }
        });


//        if(currentStatus.equals("active"))
//        {
//            mDB.collection("RideList").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                    RlistModel ride = new RlistModel();
//                    if(task.isSuccessful())
//                    {
//                        ride = task.getResult().toObject(RlistModel.class);
//                        timeEt.setText(ride.getrTime());
//                       endLocEt.setText(ride.getDestination());
//                        leavingEt.setText(ride.getStartingpoint());
//                         priceEt.setText(ride.getrPrice());
//                         offerRIdebtn.setVisibility(View.GONE);
////                          logo.setVisibility(View.GONE);
//                         editBtn.setVisibility(View.VISIBLE);
//                         delBtn.setVisibility(View.VISIBLE);
//                         statusTv.setText("Your offer on live,Please wait some time for Passenger");
//
//                    }
//
//                }
//            });
//
//        }

        offerRIdebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rtime = timeEt.getText().toString();
                Rdestination = endLocEt.getText().toString();
                Rleavingfrom = leavingEt.getText().toString();
                Rprice = priceEt.getText().toString();
                DocumentReference riderInfo = mDB.collection("Users").document(mAuth.getCurrentUser().getUid());
                riderInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        RiderInfo = documentSnapshot.toObject(UserModel.class);
                       // Toast.makeText(getActivity(), ""+RiderInfo.getProimage(), Toast.LENGTH_SHORT).show();
                        if(RiderInfo!=null)
                        {
                    String url = RiderInfo.getProimage();
                    rlistModel.setDestination(Rdestination);
                    rlistModel.setRname(RiderInfo.getName());
                    rlistModel.setrDP(RiderInfo.getProimage());
                    rlistModel.setrPrice(Rprice);
                    rlistModel.setStartingpoint(Rleavingfrom);
                    rlistModel.setrTime(Rtime);
                    rlistModel.setuType(RiderInfo.getUserType());
                    rlistModel.setRuid(mAuth.getCurrentUser().getUid());
                   // Toast.makeText(getContext(), ""+url, Toast.LENGTH_SHORT).show();

                    DocumentReference RideListRef = mDB.collection("RideList").document(mAuth.getCurrentUser().getUid());
                    RideListRef.set(rlistModel);
                           //setTimer for delete ride
                            if(rideTimeinMili!=0)
                            {
                                Intent iBroadcast = new Intent(v.getContext(), RideDeleteService.class);
                                PendingIntent pi = PendingIntent.getBroadcast(v.getContext(),deleterideReqCode,iBroadcast, PendingIntent.FLAG_IMMUTABLE);

                                alarmManager.set(AlarmManager.RTC_WAKEUP,rideTimeinMili,pi);

                            }

                            //



//                    //save state through sharedpref
//                            // Saving user data in SharedPreferences
//                            rideStatus="active";
//                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("ride_status", MODE_PRIVATE).edit();
//
//                            editor.putString("status",rideStatus);
//                            // Add other user information as needed
//                            editor.apply();
                            offerRIdebtn.setVisibility(View.GONE);
//                            logo.setVisibility(View.GONE);
                            editBtn.setVisibility(View.VISIBLE);
                            delBtn.setVisibility(View.VISIBLE);
                            statusTv.setText("Your offer on live,Please wait some time for Passenger");





                        }
                else {
                    Toast.makeText(getContext(), "failed!", Toast.LENGTH_SHORT).show();

                }

                    }
                });

            }
        });

        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hours =calendar.get(Calendar.HOUR_OF_DAY);
                int minute =calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                       calendar.set(Calendar.HOUR_OF_DAY,hour);
                       calendar.set(Calendar.MINUTE,min);

                       calendar.setTimeZone(TimeZone.getDefault());
                       //calendar.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH);

                       Date date = calendar.getTime();
                      rideTimeinMili = date.getTime();
                       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                       String time = simpleDateFormat.format(calendar.getTime());
                       timeEt.setText(time);
                        Toast.makeText(getActivity(), "time : "+time, Toast.LENGTH_SHORT).show();

                    }
                },hours,minute,false);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rtime = timeEt.getText().toString();
                Rdestination = endLocEt.getText().toString();
                Rleavingfrom = leavingEt.getText().toString();
                Rprice = priceEt.getText().toString();

                DocumentReference riderInfo = mDB.collection("Users").document(mAuth.getCurrentUser().getUid());
                riderInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        RiderInfo = documentSnapshot.toObject(UserModel.class);
                      //  Toast.makeText(getActivity(), ""+RiderInfo.getProimage(), Toast.LENGTH_SHORT).show();
                        if(RiderInfo!=null)
                        {
                            String url = RiderInfo.getProimage();
                            rlistModel.setDestination(Rdestination);
                            rlistModel.setRname(RiderInfo.getName());
                            rlistModel.setrDP(RiderInfo.getProimage());
                            rlistModel.setrPrice(Rprice);
                            rlistModel.setStartingpoint(Rleavingfrom);
                            rlistModel.setrTime(Rtime);
                            rlistModel.setRuid(mAuth.getCurrentUser().getUid());
                          //  Toast.makeText(getContext(), ""+url, Toast.LENGTH_SHORT).show();

                            DocumentReference RideListRef = mDB.collection("RideList").document(mAuth.getCurrentUser().getUid());
                            RideListRef.set(rlistModel);
                        //setTimer for delete ride
                            if(rideTimeinMili!=0)
                            {
                                Intent iBroadcast = new Intent(v.getContext(), RideDeleteService.class);
                                PendingIntent pi = PendingIntent.getBroadcast(v.getContext(),deleterideReqCode,iBroadcast, PendingIntent.FLAG_IMMUTABLE);

                                alarmManager.set(AlarmManager.RTC_WAKEUP,rideTimeinMili,pi);

                            }

                            //

//                            //save state through sharedpref
//                            // Saving user data in SharedPreferences
//                            rideStatus="active";
//                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("ride_status", MODE_PRIVATE).edit();
//
//                            editor.putString("status",rideStatus);
//                            // Add other user information as needed
//                            editor.apply();

                        }
                        else {
                            Toast.makeText(getContext(), "failed!", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences.Editor editor = getActivity().getSharedPreferences("ride_status", MODE_PRIVATE).edit();
//                 rideStatus = "inactive";
//                editor.putString("status",rideStatus);
//                editor.apply();
                editBtn.setVisibility(View.GONE);
                delBtn.setVisibility(View.GONE);
                offerRIdebtn.setVisibility(View.VISIBLE);
                timeEt.setText("");
                endLocEt.setText("");
                leavingEt.setText("");
                priceEt.setText("");
                statusTv.setText("Offer a Ride");
                mDB.collection("RideList").document(mAuth.getCurrentUser().getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        return v;
    }


}