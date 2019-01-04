package com.example.ionutpc.cloudmobile;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class splashActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 2000;
    private boolean hasInternet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    @Override
    protected void onStart() {
        Log.d("onStartTest","onStart triggered "+ String.valueOf(hasInternet));
        super.onStart();
        final Handler handler = new Handler();
        final Handler checkInternetHandler = new Handler();
        checkInternetHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hasInternet = checkInternet();
                if(hasInternet){
                    Intent homeIntent = new Intent(splashActivity.this,DashboardActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_splash,new noInternetFragment()).commit();
                        }
                    });
                }
            }
        },SPLASH_TIME_OUT);

    }

    public boolean checkInternet() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedMobile || haveConnectedWifi;
    }
}
