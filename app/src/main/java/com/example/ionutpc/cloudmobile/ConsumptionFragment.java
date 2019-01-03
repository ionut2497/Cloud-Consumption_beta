package com.example.ionutpc.cloudmobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
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

import com.example.ionutpc.cloudmobile.database.AppDatabase;
import com.example.ionutpc.cloudmobile.database.ConsumptionEntry;
import com.example.ionutpc.cloudmobile.utilities.NetworkUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ConsumptionFragment extends Fragment {

    private  static final String INSTANCE_CONSUMPTION_ID = "instanceConsumptionId";
    private TextView txtProgressCall;
    private ProgressBar progressBarCall ;
    private TextView txtProgressText;
    private ProgressBar progressBarText ;
    private TextView txtProgressData;
    private ProgressBar progressBarData;
    private LinearLayout linearProgress;
    private LinearLayout errorView;
    private static final String TAG = "ionut";
    private ProgressBar dataLoader;
    private static final int DEFAULT_CONSUMPTION_ID = -1;
    private AppDatabase mDb;
    private int mConsumptionId = DEFAULT_CONSUMPTION_ID;
    private Activity myActivity;


    private void initViews(View view) {
        txtProgressCall =  view.findViewById(R.id.txtProgressCall);
        progressBarCall =  view.findViewById(R.id.progressBarCall);
        txtProgressText =  view.findViewById(R.id.txtProgressText);
        progressBarText =  view.findViewById(R.id.progressBarText);
        txtProgressData =  view.findViewById(R.id.txtProgressData);
        progressBarData =  view.findViewById(R.id.progressBarData);
        linearProgress = view.findViewById(R.id.linearProgress);
        errorView = view.findViewById(R.id.errorView);
        dataLoader = view.findViewById(R.id.loading_data_progress);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_CONSUMPTION_ID, mConsumptionId);
        super.onSaveInstanceState(outState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG,"on create view: "+getActivity().toString());
        View view = inflater.inflate(R.layout.fragment_consumption,container,false);
        initViews(view);
        mDb = AppDatabase.getInstance(getActivity().getApplicationContext());

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myActivity = getActivity();
        Log.d(TAG,"onActivityCreated triggered");
        getRepositoryImitator(false);
        setRepeatingAsyncTask();
    }




    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            new FetchConsumptionTask().execute();
                            Log.d(TAG,"AsyncTask Repeated");
                        } catch (Exception e) {
                            getActivity().recreate();
                        }
                    }
                },5000);
            }
        };

        timer.schedule(task, 0, 30000L);

    }
    private void getRepositoryImitator(Boolean noInternet){


        if (noInternet) {
            final LiveData<ConsumptionEntry> consumption = mDb.consumptionDao().loadAllConsumptionsLastLive();
            consumption.observe((LifecycleOwner)this, new Observer<ConsumptionEntry>() {
                @Override
                public void onChanged(@Nullable ConsumptionEntry consumptionEntry) {
                    consumption.removeObserver(this);
                    if (consumptionEntry != null) {
                        Log.d(TAG, "Receiving database update from LiveData : " + consumptionEntry.toString());
                        final Consumption consumption = new Consumption(consumptionEntry.getSms(), consumptionEntry.getSmsmax(), consumptionEntry.getMinutes(), consumptionEntry.getMinutesmax(), consumptionEntry.getData(), consumptionEntry.getMaxdata());
                        updateConsumptionCounters(consumption);
                        showConsumptionView();
                    } else {
                        Log.d(TAG, "Receiving asynctask update from LiveData");
                        new FetchConsumptionTask().execute();
                    }

                }
            });
        }else{
            Log.d(TAG, "Receiving  internet asynctask update");
            Log.d(TAG,getActivity().toString());
            new FetchConsumptionTask().execute();
        }
    }

    private void showConsumptionView() {
        linearProgress.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        dataLoader.setVisibility(View.INVISIBLE);
    }
    private void updateConsumptionCounters(Consumption  consumption){
        if(myActivity== null){

            myActivity = getActivity();
            return;
        }
        runUIChangeOnThread(myActivity,consumption.getMinutesmax(),consumption.getMinutes(),progressBarCall,txtProgressCall,"");
        runUIChangeOnThread(myActivity,consumption.getSmsmax(),consumption.getSms(),progressBarText,txtProgressText,"");
        runUIChangeOnThread(myActivity,consumption.getMaxdata(),consumption.getData(),progressBarData,txtProgressData,"GB");

    }
    private void runUIChangeOnThread(Activity activity,final double max,final double status,final ProgressBar progressBar,final TextView textView,final String measure ){

        if(activity != null){
            activity.runOnUiThread(new Runnable() {
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
        else{
            Log.d(TAG, "No activity has been found for UIChange ");
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class FetchConsumptionTask extends AsyncTask<String, Void, String> {

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

                final Consumption c = new Gson().fromJson(jsonResponse,Consumption.class);
                final ConsumptionEntry consumptionEntry = new ConsumptionEntry(c.getSms(),c.getSmsmax(),c.getMinutes(),c.getMinutesmax(),c.getData(),c.getMaxdata(),new Date());
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        if (mDb.consumptionDao().loadAllConsumptionsLast() == null) {
                            // insert new task
                            mDb.consumptionDao().insertConsumption(consumptionEntry);
                            mConsumptionId = consumptionEntry.getId();

                        } else {
                            mDb.consumptionDao().deleteAllConsumption();
                            mDb.consumptionDao().insertConsumption(consumptionEntry);

                        }
                    }


                });
                showConsumptionView();
                updateConsumptionCounters(c);
            } else {

                getRepositoryImitator(true);
            }
        }



    }


}
