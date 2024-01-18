package com.example.diuride;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diuride.Listener.OnRlistSelectListener;
import com.example.diuride.Notification.FcmNotificationSender;
import com.example.diuride.databinding.ActivityMainBinding;
import com.example.diuride.models.OnGoingRideModel;
import com.example.diuride.models.RiderNotificationModel;
import com.example.diuride.models.RlistModel;
import com.example.diuride.models.UserModel;
import com.example.diuride.models.userLocationModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity implements OnRlistSelectListener {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri imageUri;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mLocationCallback;
    SettingsClient mSettingsClient;
    LocationRequest mLocationRequest;
    LocationSettingsRequest mLocationSettingsRequest;
    Location lastLocation;
    userLocationModel userLocation;
    SharedPreferences prefs ;
    String propiclink,uname,diuid;
    UserModel mUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.parseColor("#52C498"));
        getWindow().setStatusBarColor(Color.parseColor("#52C498"));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mUser = new UserModel();
        userLocation = new userLocationModel();
        //askNotificationPermission();

//        // display overlay permission for call.
//        PermissionX.init(MainActivity.this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
//                .onExplainRequestReason(new ExplainReasonCallback() {
//                    @Override
//                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList) {
//                        String message = "We need your consent for the following permissions in order to use the offline call function properly";
//                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny");
//                    }
//                }).request(new RequestCallback() {
//                    @Override
//                    public void onResult(boolean allGranted, @NonNull List<String> grantedList,
//                                         @NonNull List<String> deniedList) {
//                    }
//                });
//

//        if(ConnectivityUtils.isNetworkConnected(MainActivity.this))
//        {
//            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(this, "Turn on internet", Toast.LENGTH_SHORT).show();
//        }
      //  FrequentTask.internetStatus(MainActivity.this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.fragmentcontainer, new MapsFragment());
        ft.commit();

        NavigationView navigationView = (NavigationView)findViewById(R.id.drawernavbar);
        View headerView = navigationView.getHeaderView(0);
        RoundedImageView drawerImage = (RoundedImageView) headerView.findViewById(R.id.userDp);
        TextView drawerUsername = (TextView) headerView.findViewById(R.id.drawerUname);
        TextView drawerAccount = (TextView) headerView.findViewById(R.id.drawerUdiuID);
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                       mUser = documentSnapshot.toObject(UserModel.class);
                                        if (mUser != null) {
                                            uname = mUser.getName();
                                            diuid = mUser.getDiuid();
                                            propiclink = mUser.getProimage();
                                            drawerUsername.setText(uname);
                                            drawerAccount.setText(diuid);

                                            Glide.with(MainActivity.this)
                                                    .load(propiclink)
                                                    .centerCrop().placeholder(R.drawable.profilesvg)
                                                    .into(drawerImage);


                                            Glide.with(MainActivity.this)
                                                    .load(propiclink)
                                                    .centerCrop().placeholder(R.drawable.profilesvg)
                                                    .into(binding.userDp);
                                        }

                                    }
                                });

        db.collection("OnGoingRide").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                OnGoingRideModel onGoingRideModel = new OnGoingRideModel();
                onGoingRideModel = documentSnapshot.toObject(OnGoingRideModel.class);
                if(onGoingRideModel!=null)
                {
                    Dialog dialog1 = new Dialog(MainActivity.this);
                    dialog1.setContentView(R.layout.ongoingridealert);
                    dialog1.setCancelable(false);
                    dialog1.show();
                    AppCompatButton ContinueBTN = dialog1.findViewById(R.id.continuebtn);
                    AppCompatButton maybeLaterBTN = dialog1.findViewById(R.id.maybeLaterbtn);

                    ContinueBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this,UserPolylineActivity.class);
                            startActivity(intent);
                            dialog1.dismiss();
                        }
                    });
                    maybeLaterBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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

        ////




        //getting device location

        checkLocationPermission();
        if(requestDeviceLocationOn())
        {
            init();

        }


        //location end

        binding.drawermenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerOnOff();

            }
        });

        binding.userDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(MainActivity.this,EditProfileActivity.class));

            }
        });


        binding.navbarid.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {


                if (i == 0) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.fragmentcontainer, new MapsFragment());
                    ft.commit();

                    //Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                }
//                else if (i == 1) {
//                    FragmentManager fm = getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//
//                    ft.replace(R.id.fragmentcontainer, new NotificationFragment());
//                    ft.commit();
//
//                    //Toast.makeText(MainActivity.this, "notification", Toast.LENGTH_SHORT).show();
//                }
                else if (i == 1) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.fragmentcontainer, new RideListFragment());
                    ft.commit();
                    //Toast.makeText(MainActivity.this, "Ride List", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "default", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        binding.drawernavbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.aboutUs) {

                    drawerOnOff();
                   // Toast.makeText(MainActivity.this, "about us!", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.logout) {
                    stopLocationUpdates();
                    drawerOnOff();
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    finish();
                    }
//                else if (id == R.id.rHistory) {
//                    drawerOnOff();
//                    Toast.makeText(MainActivity.this, "history!", Toast.LENGTH_SHORT).show();
//                }
                   else if (id == R.id.ongoingride) {
                    drawerOnOff();
                    //Toast.makeText(MainActivity.this, "On Going Ride!", Toast.LENGTH_SHORT).show();
                    db.collection("OnGoingRide").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            OnGoingRideModel onGoingRideModel = new OnGoingRideModel();
                            onGoingRideModel = documentSnapshot.toObject(OnGoingRideModel.class);
                            if(onGoingRideModel!=null)
                            {
                                Intent intent = new Intent(MainActivity.this,UserPolylineActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                startActivity(new Intent(MainActivity.this,emptyOngoingmsg.class));
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                } else if (id == R.id.editProfile) {
                    drawerOnOff();
                    startActivity(new Intent(MainActivity.this,EditProfileActivity.class));
                    //Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
        startCallServiceAsPassenger();
    }

    public void drawerOnOff()
    {
        if (!binding.drawerid.isDrawerOpen(GravityCompat.START)) {

            binding.drawerid.openDrawer(GravityCompat.START);
        } else {
            binding.drawerid.closeDrawer(GravityCompat.START);
        }
    }
    //step 1
    //location permission code....

    public void checkLocationPermission()
    {
        Log.d(TAG,"inside check location");
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }

    }

    //step 2....
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                        init(); //code for after request granted....
                    }
                }return;
        }
    }
    //step 3...
    @SuppressLint("MissingPermission")
    private void startLocationUpdates()
    {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.d(TAG,"Location settings are ok");
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    Log.d(TAG,"inside error->"+statusCode);
                });
    }
    //step4...
    public void stopLocationUpdates()
    {
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(task -> {
                    Log.d(TAG,"stop location updates");
                });
    }

    //step 5...
    private void receiveLocation(LocationResult locationResult)
    {
        lastLocation = locationResult.getLastLocation();
        Log.d(TAG,"latitude:"+lastLocation.getLatitude());
        Log.d(TAG,"longitude:"+lastLocation.getLongitude());
        Log.d(TAG,"Altitude:"+lastLocation.getAltitude());
        //---%6f because after decimal fixed 6 digits...
        String s_lat =String.format(Locale.ROOT,"%6f",lastLocation.getLatitude());
        String s_lon =String.format(Locale.ROOT,"%6f",lastLocation.getLongitude());
        userLocation.setLat(s_lat);
        userLocation.setLon(s_lon);
        userLocation.setuID(mAuth.getCurrentUser().getUid());
        userLocation.setuName(uname);
       // Toast.makeText(this, "updating", Toast.LENGTH_SHORT).show();

        if(mAuth.getCurrentUser()!=null)
        {
            db.collection("UserLocation").document(mAuth.getCurrentUser().getUid()).set(userLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                    }
                }
            });
        }

       // Toast.makeText(context, "lat:"+s_lat.+"lon:"+s_lon, Toast.LENGTH_SHORT).show();

//        d_lat = lastLocation.getLatitude();
//        d_long = lastLocation.getLongitude();
//
//        tv_lat.setText(""+s_lat);
//        tv_long.setText(""+s_lon);
//
//        try{
//            //==code to fetch address from lat long
//            Geocoder geocoder = new Geocoder(this,Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(d_lat,d_long,1);
//            fetched_address = addresses.get(0).getAddressLine(0);
//            Log.d(TAG,""+fetched_address);
//            tv_address.setText(fetched_address);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

    }

    //step 6...
    public void init()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                receiveLocation(locationResult);
            }
        };

//        mLocationRequest = LocationRequest.create()
//                .setInterval(5000)
//                .setFastestInterval(5000)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setMaxWaitTime(100);
//
        //solution
        mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,5000)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                //.setMinUpdateDistanceMeters(500)
                .setMinUpdateDistanceMeters(1)
                .setWaitForAccurateLocation(true)
                .build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest =builder.build();
        startLocationUpdates();

    }


    private boolean requestDeviceLocationOn() {
        LocationManager locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled((LocationManager.GPS_PROVIDER))) {
            return true;

        } else {
            showAlertMessageLocationDisabled();

        }
        return false;
    }

    private void showAlertMessageLocationDisabled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,101);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Location turned on is Required!", Toast.LENGTH_SHORT).show();
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 101)
        {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean provideEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(provideEnable)
            {
                Toast.makeText(MainActivity.this, "gps enable", Toast.LENGTH_SHORT).show();
                init();
            }
            else
            {
                requestDeviceLocationOn();
                Toast.makeText(MainActivity.this, "gps not enable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationPermission();
        requestDeviceLocationOn();
        init();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onRlistCliked(RlistModel rlistModel) {
        //add it to rider notification list
        RiderNotificationModel riderNotificationModel = new RiderNotificationModel(mUser.getName(),mUser.getProimage(),mAuth.getUid());

        db.collection("RiderNotiList").document(rlistModel.getRuid()).set(riderNotificationModel).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        //OngoingRide saved information
        db.collection("RideList").document(rlistModel.getRuid()).update("status","booked").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Ride Booked Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        OnGoingRideModel onGoingRideModel = new OnGoingRideModel(rlistModel.getRuid(),mAuth.getUid(),rlistModel.getDestination(),rlistModel.getStartingpoint(),rlistModel.getrPrice(),rlistModel.getrTime());
        db.collection("OnGoingRide").document(Objects.requireNonNull(mAuth.getUid())).set(onGoingRideModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        db.collection("OnGoingRide").document(rlistModel.getRuid()).set(onGoingRideModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        //send notification to the rider.
        db.collection("Users").document(rlistModel.getRuid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel ob = new UserModel();
                if(task.isSuccessful())
                {
                    ob = task.getResult().toObject(UserModel.class);
                    if(ob!=null) {


                        FcmNotificationSender notificationSender = new FcmNotificationSender(ob.getFCMtoken(),"Ride Confirmed","A Passenger Accept Your Ride Offer",MainActivity.this,"editPro");
                        notificationSender.SendNotification();
                        String passengerID = mAuth.getUid();
                        String RiderID = rlistModel.getRuid();

                        //Toast.makeText(MainActivity.this, "joss bro", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,UserPolylineActivity.class);
//                        intent.putExtra("passengerID",passengerID);
//                        intent.putExtra("riderID",RiderID);

                        startActivity(intent);

//                    PushNotification notificationData = new PushNotification(new NotificationData("hello","ki obostha"),ob.getFCMtoken());
//
//                     api.getRetrofitClientFCM().create(apiInterface.class).sendNotification(notificationData)
//                             .enqueue(new Callback<PushNotification>() {
//                                 @Override
//                                 public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
//                                     if(response.isSuccessful())
//                                     {
//                                         Toast.makeText(MainActivity.this, "success"+response.message(), Toast.LENGTH_SHORT).show();
//                                     }
//                                 }
//
//                                 @Override
//                                 public void onFailure(Call<PushNotification> call, Throwable t) {
//
//                                 }
//                             });

                    }
                }

            }
        });










//        String passengerID = mAuth.getUid();
//        String RiderID = rlistModel.getRuid();
//
//        //Toast.makeText(MainActivity.this, "joss bro", Toast.LENGTH_SHORT).show();
//         Intent intent = new Intent(MainActivity.this,UserPolylineActivity.class);
//         intent.putExtra("passengerID",passengerID);
//         intent.putExtra("riderID",RiderID);
//
//         startActivity(intent);
        //Toast.makeText(MainActivity.this, ""+rlistModel.getRuid(), Toast.LENGTH_SHORT).show();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragmentcontainer, new MapsFragment());
//        ft.commit();
//        binding.navbarid.setItemActiveIndex(0);
    }
    private void startCallServiceAsPassenger()
    {
        Application application = getApplication(); // Android's application context
        long appID = 1121113909;   // yourAppID
        String appSign ="881c384fd42698a4287e47ff9ace17cc29041a61b96b78a8cc2e761332d4f815";  // yourAppSign
        String userID =mAuth.getCurrentUser().getUid(); // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName ="Passenger";   // yourUserName


        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }
    ///////
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    //////
}