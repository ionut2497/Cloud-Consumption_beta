package com.example.ionutpc.cloudmobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ionutpc.cloudmobile.Adapter.FAQAdapter;
import com.example.ionutpc.cloudmobile.utilities.NetworkUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FAQFragment extends Fragment {
    private String key ;
    private FAQAdapter faqAdapter;
    private List<FAQ> faqs = new ArrayList<FAQ>();
    private RecyclerView recyclerView;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getArguments().getString("index","012345");
            Log.d("Value oncreate index",key);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.fragment_faq,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        context = getContext();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        faqAdapter = new FAQAdapter(context,faqs);

        recyclerView.setAdapter(faqAdapter);
        new FetchQuestions().execute();
        return view;
    }
    @SuppressLint("StaticFieldLeak")
    private class FetchQuestions extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {


            URL consumption = NetworkUtils.buildUrl(key,"getFAQ");
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
                FAQ[] faq = new Gson().fromJson(jsonResponse,FAQ[].class);
                for(FAQ currentFaq  : faq){
                    faqs.add(currentFaq);
                    System.out.println(currentFaq.toString());
                    faqAdapter.notifyDataSetChanged();
                }
                faqs.size();

            } else {

            }
        }
}
}
