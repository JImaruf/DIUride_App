package com.example.diuride;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.diuride.models.OnGoingRideModel;
import com.example.diuride.models.userLocationModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    FirebaseFirestore mDB;
    FirebaseAuth mAuth;
    double lat, lon;

    userLocationModel singleUser;
    SupportMapFragment mapFragment;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int LOCATION_UPDATE_INTERVAL = 3000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //*********check onGoing Ride available or not

        singleUser = new userLocationModel();
       // startUserLocationsRunnable();

        return v;
    }

    private void startUserLocationsRunnable() {
        Log.d(TAG, "startUserLocationsRunnable: starting runnable for retrieving updated locations.");
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                onMapReady(mMap);
                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates() {
        mHandler.removeCallbacks(mRunnable);
    }
//    private  void updatedlocationfetch()
//    {
//        mDB.collection("UserLocation").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    singleUser = task.getResult().toObject(userLocationModel.class);
//                    lat = Double.parseDouble(singleUser.getLat());
//                    lon = Double.parseDouble(singleUser.getLon());
//                    LatLng latLng = new LatLng(lat,lon);
//                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
//                    mMap.addMarker(markerOptions);
//
//
//                }
//            }
//        });
//    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
      //  mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        mDB.collection("UserLocation").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    singleUser = task.getResult().toObject(userLocationModel.class);
//                    lat = Double.parseDouble(singleUser.getLat());
//                    lon = Double.parseDouble(singleUser.getLon());
//                    LatLng latLng = new LatLng(lat,lon);
//                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                  // mMap.addMarker(markerOptions);
//                    double bottomBoundary = Double.parseDouble(singleUser.getLat());
//                    bottomBoundary = bottomBoundary -.1;
//
//                    double leftBoundary = Double.parseDouble(singleUser.getLon());
//                    leftBoundary = leftBoundary -.1;
//                    double topBoundary = Double.parseDouble(singleUser.getLat());
//                    topBoundary=topBoundary+.1;
//                    double rightBoundary = Double.parseDouble(singleUser.getLon());
//                    rightBoundary= rightBoundary+.1;
//                    LatLngBounds mapboundary = new LatLngBounds(
//                            new LatLng(bottomBoundary,leftBoundary),
//                            new LatLng(topBoundary,rightBoundary)
//                    );
//
//
//
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mapboundary,0));
//                      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
//
//                }
//            }
//        });

    }

    private void moveCamera(LatLng latLng,float zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }


    @Override
    public void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
       // startUserLocationsRunnable();
    }
}