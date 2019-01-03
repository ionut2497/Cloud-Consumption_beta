package com.example.ionutpc.cloudmobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class noInternetFragment extends  android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        new CheckInternet().execute();
        return view;

    }


    public class CheckInternet extends AsyncTask<String, Void , String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {
            Boolean noInternet = true;

            while (noInternet) {

                if (((splashActivity) getActivity()).checkInternet()) {
                    noInternet = false;
                    Log.d("onStartTestFrag", "test true");
                    return noInternet.toString();

                } else {
                    Log.d("onStartTestFrag", "test false");
                    Log.d("onStartTestFrag", String.valueOf(((splashActivity) getActivity()).checkInternet()));

                }

            }

            return noInternet.toString();
        }

        @Override
        protected void onPostExecute(String noInternet) {
            if(!Boolean.valueOf(noInternet)){
                Intent homeIntent = new Intent(getActivity(), DashboardActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            }

        }
    }
}