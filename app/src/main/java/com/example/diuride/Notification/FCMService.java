package com.example.diuride.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.example.diuride.EditProfileActivity;
import com.example.diuride.MainActivity;
import com.example.diuride.MainActivity2;
import com.example.diuride.R;
import com.example.diuride.SplashScreen;
import com.example.diuride.UserPolylineActivity2;
import com.example.diuride.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FCMService extends FirebaseMessagingService {
    FirebaseFirestore mDB= FirebaseFirestore.getInstance();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();


    String title,msg;
    String uType="none";
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        if(mAuth.getCurrentUser()!=null)
        {
            mDB.collection("Users").document(mAuth.getCurrentUser().getUid()).update("fcmtoken",token);
        }


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if(message.getNotification()!=null)
        {
            title = message.getNotification().getTitle();
            msg = message.getNotification().getBody();
//            mDB.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    UserModel ob =  task.getResult().toObject(UserModel.class);
//                    if(ob!= null)
//                    {
//                     String uType = ob.getUserType();
//                    }
//
//                }
//            });
            pushNotification(title,msg);
            
        }
        

    }

    private void pushNotification(String title, String msg) {

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        final  String CHANNEL_ID= "push_notification";
        Intent iNotify;
        iNotify = new Intent(this, SplashScreen.class);
//           iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//       if(uType.equals("rider"))
//       {
//          iNotify = new Intent(this, MainActivity2.class);
//           iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//       }
//       else  {
//           iNotify = new Intent(this, MainActivity2.class);
//           iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//       }


        PendingIntent pi = PendingIntent.getActivity(this,100,iNotify,PendingIntent.FLAG_IMMUTABLE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "Custom Channel";
            String description = "Channel for Push Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);



            if(nm!=null)
            {
                nm.createNotificationChannel(channel);
            }
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.baseline_directions_bike_24)
                    .setContentIntent(pi)
                    .setSubText(msg).setContentTitle(title)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
        }

        else
        {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.baseline_directions_bike_24)
                     .setContentIntent(pi)
                    .setSubText(msg).setContentTitle(title)
                    .setAutoCancel(true)
                    .build();

        }

        if(nm!=null)
        {
            nm.notify(1,notification);
        }


    }
}
