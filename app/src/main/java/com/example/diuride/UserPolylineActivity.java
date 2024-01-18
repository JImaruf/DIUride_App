package com.example.diuride;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.diuride.DirectionPlaceModel.DirectionLegModel;
import com.example.diuride.DirectionPlaceModel.DirectionResponseModel;
import com.example.diuride.DirectionPlaceModel.DirectionRouteModel;
import com.example.diuride.DirectionPlaceModel.DirectionStepModel;
import com.example.diuride.Notification.FcmNotificationSender;
import com.example.diuride.RestApiServices.APIServices;
import com.example.diuride.RestApiServices.RetrofitClient;
import com.example.diuride.models.OnGoingRideModel;
import com.example.diuride.models.RlistModel;
import com.example.diuride.models.UserModel;
import com.example.diuride.models.userLocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.diuride.databinding.ActivityUserPolylineBinding;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPolylineActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private ActivityUserPolylineBinding binding;
    APIServices retrofitApiServices;
    String mode = "walking";
    String passengerID;
    String riderID;
    FirebaseFirestore mDB;
    FirebaseAuth mAuth;
    double passengerLat=0.0,passengerLon=0.0,riderLat=0.0,riderLon=0.0;

    userLocationModel singleUserLocation;
    String url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserPolylineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        singleUserLocation = new userLocationModel();
        retrofitApiServices = RetrofitClient.getRetrofitClient().create(APIServices.class);
//        passengerID = getIntent().getStringExtra("passengerID");
//        riderID = getIntent().getStringExtra("riderID");
        mDB.collection("OnGoingRide").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                OnGoingRideModel onGoingRideModel = new OnGoingRideModel();
                if(task.isSuccessful())
                {
                    onGoingRideModel = task.getResult().toObject(OnGoingRideModel.class);
                    if(onGoingRideModel!=null)
                    {
                        passengerID = onGoingRideModel.getPuid();
                        riderID = onGoingRideModel.getRuid();

                       // onMapReady(mGoogleMap);
                        startUserLocationsRunnable();
                        //mapFragment.getMapAsync(this);
                    }
                    else
                    {
                        Toast.makeText(UserPolylineActivity.this, "No Ride Found!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
       // fetchUsersLocation();


//        Toast.makeText(this, ""+passengerID, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, ""+riderID, Toast.LENGTH_SHORT).show();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

       // startUserLocationsRunnable();


        binding.cancelRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog1 = new Dialog(UserPolylineActivity.this);
                dialog1.setContentView(R.layout.cancelrideconfirmdialog);
                dialog1.setCancelable(false);
                dialog1.show();
                AppCompatButton CancelRide = dialog1.findViewById(R.id.CancelRideBTN);
                AppCompatButton Nobtn = dialog1.findViewById(R.id.notCancelRide);

                CancelRide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //send notification to the rider.
                        mDB.collection("Users").document(riderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                UserModel ob = new UserModel();
                                if(task.isSuccessful())
                                {
                                    ob = task.getResult().toObject(UserModel.class);
                                    if(ob!=null) {


                                        FcmNotificationSender notificationSender = new FcmNotificationSender(ob.getFCMtoken(),"Ride Canceled","Passenger Canceled Your Ride Offer",UserPolylineActivity.this,"editPro");
                                        notificationSender.SendNotification();
                                        mDB.collection("OnGoingRide").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    //start a new activity for give thanks
                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                        mDB.collection("OnGoingRide").document(riderID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {

                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                        mDB.collection("RideList").document(riderID).update("status","unbooked").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(UserPolylineActivity.this, "Ride Canceled!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                        //Toast.makeText(MainActivity.this, "joss bro", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(UserPolylineActivity.this,MainActivity.class);
//                                        startActivity(intent);
                                    }
                                }

                            }
                        });

                      mDB.collection("RiderNotiList").document(riderID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                          }
                      });

                        //  Toast.makeText(context, "done"+rlistModelList.get(position).getRname() ,Toast.LENGTH_SHORT).show();
                        dialog1.dismiss();
                    }
                });
                Nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(UserPolylineActivity.this, "!", Toast.LENGTH_SHORT).show();
                        dialog1.dismiss();
                    }
                });

            }
        });
       binding.RiderFoundBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Dialog dialog1 = new Dialog(UserPolylineActivity.this);
               dialog1.setContentView(R.layout.successfulfindridepassengerdialog);
               dialog1.setCancelable(false);
               dialog1.show();
               AppCompatButton foundRider = dialog1.findViewById(R.id.foundRider);
               AppCompatButton NotFound = dialog1.findViewById(R.id.notFound);

               foundRider.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       stopLocationRunnable();
                       mDB.collection("OnGoingRide").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful())
                               {
                                   mDB.collection("RiderNotiList").document(riderID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {

                                       }
                                   });
                                   //start a new activity for give thanks
                                   startActivity(new Intent(UserPolylineActivity.this,SuccessFullRIdeShare.class));
                                   finish();
                               }

                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                           }
                       });
                       dialog1.dismiss();
                   }
               });


               NotFound.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(UserPolylineActivity.this, "!", Toast.LENGTH_SHORT).show();
                       dialog1.dismiss();
                   }


               });
           }
       });
       ////display overlay
                // need a activityContext.


       binding.callIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              // startCallService();
               showCallDialog();


           }
       });


    }

    private void showCallDialog() {

        Dialog dialog1 = new Dialog(UserPolylineActivity.this);
        dialog1.setContentView(R.layout.callingdialog);
        dialog1.setCancelable(false);
        dialog1.show();
        ZegoSendCallInvitationButton call = dialog1.findViewById(R.id.Confirmcall);
        AppCompatButton NoCall = dialog1.findViewById(R.id.NoCallbtn);



        call.setIsVideoCall(false);
        call.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        call.setInvitees(Collections.singletonList(new ZegoUIKitUser(riderID)));


        NoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

            }
        });

    }


    public void startCallService()
    {
        Application application = getApplication(); // Android's application context
        long appID = 1121113909;   // yourAppID
        String appSign ="881c384fd42698a4287e47ff9ace17cc29041a61b96b78a8cc2e761332d4f815";  // yourAppSign
        String userID =mAuth.getCurrentUser().getUid(); // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName ="Passenger";   // yourUserName


        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
//        url = "https://maps.googleapis.com/maps/api/directions/json?origin="+passengerLat+","+passengerLon+"&destination="+riderLat+","+riderLon+"&mode="+mode+"&key=AIzaSyApl3I1wwymJg1Y_XvJDGwzEDuPsOIfitg";
//    //  url = "https://maps.googleapis.com/maps/api/directions/json?origin=22.361456,90.318736&destination=22.861456,90.118736&mode=driving&key=AIzaSyApl3I1wwymJg1Y_XvJDGwzEDuPsOIfitg";
//
////        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        setupGoogleMap();
//        fetchUsersLocation();
//        getDirection(url);
    }

    public void FindPolylines()
    {
        mDB.collection("OnGoingRide").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                OnGoingRideModel onGoingRideModel = new OnGoingRideModel();
                if(task.isSuccessful())
                {
                    onGoingRideModel = task.getResult().toObject(OnGoingRideModel.class);
                    if(onGoingRideModel==null)
                    {
                        stopLocationRunnable();
                        startActivity(new Intent(UserPolylineActivity.this,MainActivity.class));
                        finish();

                    }
                    else
                    {
                        setupGoogleMap();
                        fetchUsersLocation();
                        url = "https://maps.googleapis.com/maps/api/directions/json?origin="+passengerLat+","+passengerLon+"&destination="+riderLat+","+riderLon+"&mode="+mode+"&key=AIzaSyApl3I1wwymJg1Y_XvJDGwzEDuPsOIfitg";
                        //  url = "https://maps.googleapis.com/maps/api/directions/json?origin=22.361456,90.318736&destination=22.861456,90.118736&mode=driving&key=AIzaSyApl3I1wwymJg1Y_XvJDGwzEDuPsOIfitg";

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        getDirection(url);

                    }
                }

            }
        });


    }


    public void getDirection(String url) {

        //String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+passengerLat+","+passengerLon+"&destination="+riderLat+","+riderLon+"&mode=driving&key=AIzaSyApl3I1wwymJg1Y_XvJDGwzEDuPsOIfitg";
       // String url = "https://maps.googleapis.com/maps/api/directions/json?origin=22.361456,90.318736&destination=22.861456,90.118736&mode=driving&key=AIzaSyApl3I1wwymJg1Y_XvJDGwzEDuPsOIfitg";

        retrofitApiServices.getDirection(url).enqueue(new Callback<DirectionResponseModel>() {
            @Override
            public void onResponse(Call<DirectionResponseModel> call, Response<DirectionResponseModel> response) {
                Gson gson = new Gson();
                String res = gson.toJson(response.body());
                Log.d("TAG", "onResponse: " + res);

                if (response.errorBody() == null) {
                    if (response.body() != null) {
                        clearUI();

                        if (response.body().getDirectionRouteModels().size() > 0) {
                            DirectionRouteModel routeModel = response.body().getDirectionRouteModels().get(0);

                            //getSupportActionBar().setTitle(routeModel.getSummary());

                            DirectionLegModel legModel = routeModel.getLegs().get(0);
//                            binding.txtStartLocation.setText(legModel.getStartAddress());
//                            binding.txtEndLocation.setText(legModel.getEndAddress());
//
//                            bottomSheetLayoutBinding.txtSheetTime.setText(legModel.getDuration().getText());
//                            bottomSheetLayoutBinding.txtSheetDistance.setText(legModel.getDistance().getText());


                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(legModel.getEndLocation().getLat(), legModel.getEndLocation().getLng()))
                                    .title("End Location"));

//                            mGoogleMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(legModel.getStartLocation().getLat(), legModel.getStartLocation().getLng()))
//                                    .title("Start Location"));

                          // adapter.setDirectionStepModels(legModel.getSteps());


                            List<LatLng> stepList = new ArrayList<>();

                            PolylineOptions options = new PolylineOptions()
                                    .width(25)
                                    .color(Color.BLUE)
                                    .geodesic(true)
                                    .clickable(true)
                                    .visible(true);

                            List<PatternItem> pattern;
                            if (mode.equals("walking")) {
                                pattern = Arrays.asList(
                                        new Dot(), new Gap(10));

                                options.jointType(JointType.ROUND);
                            } else {
                                pattern = Arrays.asList(
                                        new Dash(30));
                            }

                            options.pattern(pattern);

                            for (DirectionStepModel stepModel : legModel.getSteps()) {
                                List<com.google.maps.model.LatLng> decodedLatLng = decode(stepModel.getPolyline().getPoints());
                                for (com.google.maps.model.LatLng latLng : decodedLatLng) {
                                    stepList.add(new LatLng(latLng.lat, latLng.lng));
                                }
                            }

                            options.addAll(stepList);
                            Polyline polyline = mGoogleMap.addPolyline(options);

                            LatLng startLocation = new LatLng(legModel.getStartLocation().getLat(), legModel.getStartLocation().getLng());
                            LatLng endLocation = new LatLng(legModel.getStartLocation().getLat(), legModel.getStartLocation().getLng());


                           // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(startLocation, endLocation), 17));

                        } else {
                           // Toast.makeText(UserPolylineActivity.this, "No route find", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                       // Toast.makeText(UserPolylineActivity.this, "No route find", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("TAG", "onResponse: " + response);
                }

                //  loadingDialog.stopLoading();
            }

            @Override
            public void onFailure(Call<DirectionResponseModel> call, Throwable t) {

            }
        });
    }
    private void setupGoogleMap() {

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(false);

        // getCurrentLocation();
    }

    private void clearUI() {

        mGoogleMap.clear();
//        binding.txtStartLocation.setText("");
//        binding.txtEndLocation.setText("");
//        getSupportActionBar().setTitle("");
//        bottomSheetLayoutBinding.txtSheetDistance.setText("");
//        bottomSheetLayoutBinding.txtSheetTime.setText("");

    }
    private List<com.google.maps.model.LatLng> decode(String points) {

        int len = points.length();

        final List<com.google.maps.model.LatLng> path = new ArrayList<>(len / 2);
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new com.google.maps.model.LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;

    }

    private void fetchUsersLocation()
    {
//        passengerID = getIntent().getStringExtra("passengerID");
//        riderID = getIntent().getStringExtra("riderID");
        mDB.collection("UserLocation").document(passengerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    singleUserLocation = task.getResult().toObject(userLocationModel.class);
                    if(singleUserLocation!=null)
                    {

                        passengerLat = Double.parseDouble(singleUserLocation.getLat());
                        passengerLon= Double.parseDouble(singleUserLocation.getLon());
                        // LatLng passengerLatLon = new LatLng(lat,lon);

                    }

                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        mDB.collection("UserLocation").document(riderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    singleUserLocation = task.getResult().toObject(userLocationModel.class);
                    riderLat = Double.parseDouble(singleUserLocation.getLat());
                    riderLon= Double.parseDouble(singleUserLocation.getLon());
                    // LatLng passengerLatLon = new LatLng(lat,lon);

                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private void startUserLocationsRunnable() {
        Log.d(TAG, "startUserLocationsRunnable: starting runnable for retrieving updated locations.");
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                FindPolylines();
                mHandler.postDelayed(mRunnable, 4000);
            }
        }, 4000);
    }

   public void stopLocationRunnable()
    {
        mHandler.removeCallbacks(mRunnable);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationRunnable();
    }
}