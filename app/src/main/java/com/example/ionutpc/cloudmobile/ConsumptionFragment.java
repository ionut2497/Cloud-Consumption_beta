package com.example.ionutpc.cloudmobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ionutpc.cloudmobile.utilities.NetworkUtils;
import com.google.gson.Gson;

import java.net.URL;

public class ConsumptionFragment extends android.support.v4.app.Fragment {

    private TextView txtProgressCall;
    private ProgressBar progressBarCall ;
    private TextView txtProgressText;
    private ProgressBar progressBarText ;
    private TextView txtProgressData;
    private ProgressBar progressBarData;
    private LinearLayout linearProgress;
    private LinearLayout errorView;
    private Handler handler = new Handler();
    private ProgressBar dataLoader;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_consumption,container,false);
        txtProgressCall =  view.findViewById(R.id.txtProgressCall);
        progressBarCall =  view.findViewById(R.id.progressBarCall);
        txtProgressText =  view.findViewById(R.id.txtProgressText);
        progressBarText =  view.findViewById(R.id.progressBarText);
        txtProgressData =  view.findViewById(R.id.txtProgressData);
        progressBarData =  view.findViewById(R.id.progressBarData);
        linearProgress = view.findViewById(R.id.linearProgress);
        errorView = view.findViewById(R.id.errorView);
        dataLoader = view.findViewById(R.id.loading_data_progress);
        new FetchConsumptionTask().execute();
        //updateConsumptionCounters();
        //showConsumptionView();
        return view;

    }

    private void showErrorView() {

        linearProgress.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        dataLoader.setVisibility(View.INVISIBLE);
    }

    private void showConsumptionView() {
        linearProgress.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        dataLoader.setVisibility(View.INVISIBLE);
    }

    private void updateConsumptionCounters(Consumption  consumption){
        runUIChangeOnThread(consumption.getMinutesmax(),consumption.getMinutes(),progressBarCall,txtProgressCall,"");
        runUIChangeOnThread(consumption.getSmsmax(),consumption.getSms(),progressBarText,txtProgressText,"");
        runUIChangeOnThread(consumption.getMaxdata(),consumption.getData(),progressBarData,txtProgressData,"GB");

    }

    private void runUIChangeOnThread(final double max,final double status,final ProgressBar progressBar,final TextView textView,final String measure ){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                double progress = (status/max)*100;
                progressBar.setProgress((int)progress);
                if(measure.isEmpty()){
                    textView.setText(Math.round(status) + "/"+Math.round(max));
                }
                else{
                    textView.setText(status + "/"+Math.round(max)+" "+measure);
                }

            }
        });
    }

    public class FetchConsumptionTask extends AsyncTask<String, Void, String> {

        // COMPLETED (18) Within your AsyncTask, override the method onPreExecute and show the loading indicator
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {


            URL consumption = NetworkUtils.buildUrl();
            try {

                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(consumption);
                Log.d("IONUT_ASYNC",String.valueOf(jsonResponse));


                return jsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            dataLoader.setVisibility(View.INVISIBLE);
            if (jsonResponse != null) {

                Consumption c = new Gson().fromJson(jsonResponse,Consumption.class);

                showConsumptionView();
                updateConsumptionCounters(c);


            } else {
                showErrorView();
            }
        }
    }
}
