package com.example.ionutpc.cloudmobile;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ConsumptionFragment extends android.support.v4.app.Fragment {

    private TextView txtProgressCall;
    private ProgressBar progressBarCall ;
    private TextView txtProgressText;
    private ProgressBar progressBarText ;
    private TextView txtProgressData;
    private ProgressBar progressBarData;
    private LinearLayout linearProgress;
    private LinearLayout errorView;
    private int callStatus = 0;
    private int textStatus = 0;
    private int dataStatus = 0;
    private Handler handler = new Handler();
    private int result = 45;

    private void showConsumptionView() {
        linearProgress.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
    }

    private void showErrorView() {
        linearProgress.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
    }

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (callStatus <= result) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            progressBarCall.setProgress(callStatus);
                            txtProgressCall.setText(callStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callStatus++;
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (textStatus <= result) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            progressBarText.setProgress(textStatus);
                            txtProgressText.setText(textStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    textStatus++;
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (dataStatus <= result) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            progressBarData.setProgress(dataStatus);
                            txtProgressData.setText(dataStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dataStatus++;
                }
            }
        }).start();

        return view;

    }
}
