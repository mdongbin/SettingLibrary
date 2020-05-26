package com.example.settinglibrary.libRetrofit2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.settinglibrary.Msg;
import com.example.settinglibrary.R;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit2Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private Retrofit retrofit;
    private Button btnShowData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_retrofit2);

        recyclerView = findViewById(R.id.fit_recyclerview);

        retrofit = new Retrofit.Builder()
                .baseUrl(Msg.Retrofit2URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Retrofit2Interface retrofitInterface= retrofit.create(Retrofit2Interface.class);

        retrofitInterface.getBoxOffice(Msg.Retrofit2_API_KEY,"20200403").enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                Map<String,Object> boxOfficeResult= (Map<String, Object>) response.body().get("boxOfficeResult");
                ArrayList<Map<String, Object>> jsonList = (ArrayList) boxOfficeResult.get("dailyBoxOfficeList");

                mAdapter=new Retrofit2Adapter(jsonList);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });

        btnShowData = findViewById(R.id.btnShowData);
        btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
}
