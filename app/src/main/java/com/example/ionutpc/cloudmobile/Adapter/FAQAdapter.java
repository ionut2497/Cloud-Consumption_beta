package com.example.ionutpc.cloudmobile.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ionutpc.cloudmobile.FAQ;
import com.example.ionutpc.cloudmobile.R;

import java.util.List;


public class FAQAdapter extends  RecyclerView.Adapter<FAQAdapter.ViewHolder> {
    private Context context;
    private List<FAQ> faqs;

    public FAQAdapter(Context context, List<FAQ> faqs) {
        this.context = context;
        this.faqs = faqs;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.faq_item,viewGroup,false);

        return new FAQAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final FAQ faq = faqs.get(i);
        viewHolder.question.setText(faq.getQuestion());
        viewHolder.answer.setText(faq.getAnswer());

    }

    @Override
    public int getItemCount() {
        return  faqs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView question;
    public TextView answer;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        question = itemView.findViewById(R.id.question);
        answer = itemView.findViewById(R.id.answer);

    }
}
}
