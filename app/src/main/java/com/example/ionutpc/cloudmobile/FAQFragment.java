package com.example.ionutpc.cloudmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FAQFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_faq,container,false);
    }
}
