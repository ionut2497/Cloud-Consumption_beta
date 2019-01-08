package com.example.ionutpc.cloudmobile;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.ionutpc.cloudmobile.utilities.NetworkUtils;
import com.google.gson.Gson;

import java.net.URL;

/**
 * Implementation of App Widget functionality.
 */
public class ConsumptionWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.consumption_widget);
        views.setImageViewResource(R.id.imageButtonSync,R.drawable.ic_refresh_black_24dp);
        Intent intentSync = new Intent(context, ConsumptionWidget.class);
        intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentSync.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId } );
        PendingIntent pendingSync = PendingIntent.getBroadcast(context,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.imageButtonSync,pendingSync);
        Toast.makeText(context, "Consumption Updated", Toast.LENGTH_LONG).show();
        new FetchConsumptionTask(views,appWidgetId, appWidgetManager).execute();
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static class FetchConsumptionTask extends AsyncTask<String, Void, String> {
        private RemoteViews views;
        private int WidgetID;
        private AppWidgetManager WidgetManager;

        public FetchConsumptionTask(RemoteViews views, int appWidgetID, AppWidgetManager appWidgetManager){
            this.views = views;
            this.WidgetID = appWidgetID;
            this.WidgetManager = appWidgetManager;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... params) {


            URL consumption = NetworkUtils.buildUrl("12345","getConsumptionData");
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
            if (jsonResponse != null) {

                final Consumption c = new Gson().fromJson(jsonResponse, Consumption.class);
                views.setViewVisibility(R.id.linearProgress, View.VISIBLE);
                views.setViewVisibility(R.id.errorView, View.INVISIBLE);
                double progresscall = (c.getMinutes()/c.getMinutesmax())*100;
                double progresstext = (c.getSms()/c.getSmsmax())*100;
                double progressdata = (c.getData()/c.getMaxdata())*100;
                views.setProgressBar(R.id.progressBarCall ,100,(int)progresscall,false);
                views.setProgressBar(R.id.progressBarData ,100,(int)progressdata,false);
                views.setProgressBar(R.id.progressBarText ,100,(int)progresstext,false);
                views.setTextViewText(R.id.txtProgressCall, String.valueOf(Math.round(c.getMinutes()) + "/"+Math.round(c.getMinutesmax())));
                views.setTextViewText(R.id.txtProgressData, String.valueOf(c.getData() + "/"+Math.round(c.getMaxdata())+" GB"));
                views.setTextViewText(R.id.txtProgressText, String.valueOf(Math.round(c.getSms()) + "/"+Math.round(c.getSmsmax())));
                WidgetManager.updateAppWidget(WidgetID, views);
            } else {
                views.setViewVisibility(R.id.linearProgress, View.INVISIBLE);
                views.setViewVisibility(R.id.errorView, View.VISIBLE);
                WidgetManager.updateAppWidget(WidgetID, views);
            }
        }


    }

}

