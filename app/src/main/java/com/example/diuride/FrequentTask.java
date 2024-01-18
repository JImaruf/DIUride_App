package com.example.diuride;

import android.content.Context;
import android.widget.Toast;

public class FrequentTask {

    Context context;

    public static void  internetStatus(Context context)
    {
        if(ConnectivityUtils.isNetworkConnected(context))
        {
            Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(context, "Turn on internet", Toast.LENGTH_SHORT).show();
        }

    }

}