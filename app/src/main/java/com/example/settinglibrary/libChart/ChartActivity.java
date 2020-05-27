package com.example.settinglibrary.libChart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.settinglibrary.R;

public class ChartActivity extends AppCompatActivity {

    private Button btnBarChart;
    private Button btnLineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chart);

        btnBarChart = findViewById(R.id.btnBarChart);
        btnBarChart.setOnClickListener(this::btnClick);

        btnLineChart = findViewById(R.id.btnLineChart);
        btnLineChart.setOnClickListener(this::btnClick);
    }

    private void btnClick(View v){
        Intent intent;

        switch(v.getId()){
            case R.id.btnBarChart:
                intent = new Intent(ChartActivity.this, BarChartActivity.class);
                startActivity(intent);

            case R.id.btnLineChart:
                intent = new Intent(ChartActivity.this, LineChartActivity.class);
                startActivity(intent);
        }
    }
}
