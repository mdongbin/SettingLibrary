package com.example.settinglibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class Retrofit2Adapter extends RecyclerView.Adapter<Retrofit2Adapter.MyViewHolder> {

    private ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

    public Retrofit2Adapter(ArrayList<Map<String, Object>> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Retrofit2Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Retrofit2Adapter.MyViewHolder holder, int position) {
        Map<String, Object> item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRank, tvMovieNm, tvOpenDt;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvRank=itemView.findViewById(R.id.txtRank);
            tvMovieNm=itemView.findViewById(R.id.txtMovieName);
            tvOpenDt=itemView.findViewById(R.id.txtMovieDate);

        }
        public void setItem(Map<String, Object> item){

            tvRank.setText(item.get("rank").toString());
            tvMovieNm.setText(item.get("movieNm").toString());
            tvOpenDt.setText(item.get("openDt").toString());

        }
    }
}
