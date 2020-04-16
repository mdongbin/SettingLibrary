package com.example.settinglibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.tomer.fadingtextview.FadingTextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button btnQRCode;
    private Button btnCamera;
    private FadingTextView txtFadingTxt;
    private Button btnFadingTxt;
    private Button btnRetrofit2;
    private Button btnFirebase;
    private Button btnGlide;

    private ImageView imgView;
    private int isGlide = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setReference();
        setControls();

    }

    private void setControls() {
        btnQRCode.setOnClickListener(onQRCode);
        btnCamera.setOnClickListener(onCamera);
        btnFadingTxt.setOnClickListener(onFadingTxt);
        btnRetrofit2.setOnClickListener(onRetrofit2);
        btnFirebase.setOnClickListener(onFirebase);
        btnGlide.setOnClickListener(onGlide);
    }

    private void setReference() {
        btnQRCode = findViewById(R.id.btnQRCode);

        btnCamera = findViewById(R.id.btnCamera);

        txtFadingTxt = findViewById(R.id.txtFadingTxt);
        btnFadingTxt = findViewById(R.id.btnFadingTxt);

        btnRetrofit2 = findViewById(R.id.btnRetrofit2);

        btnFirebase = findViewById(R.id.btnFirebase);

        btnGlide = findViewById(R.id.btnGlide);

        imgView = findViewById(R.id.imgView);
    }

    private View.OnClickListener onQRCode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.setPrompt("QR Code");
            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.initiateScan();
        }
    };

    private View.OnClickListener onCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            startActivity(intent);
        }
    };




    private View.OnClickListener onFadingTxt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] values = {"ANDROID", "DEVELOPER", "JUNIOR"};
            txtFadingTxt.setTexts(values);
        }
    };

    private View.OnClickListener onRetrofit2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Retrofit2Activity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener onFirebase = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FirebaseActivity.class);
            startActivity(intent);
        }
    };

    // with : Context, load : URI, placeholder : 로딩 이미지, error : 오류 이미지
    // override : 이미지 크기(메모리 절약 차원), into : imgView, thumbnail() : 비율 흐릿하게 보여줌.
    // asGif() : GIF image 로딩, centerCrop() : 반 자르기.
    private View.OnClickListener onGlide = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isGlide % 2 == 1){
                imgView.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this).load("https://image.fmkorea.com/files/attac" +
                        "h/new/20180627/425547776/837628905/1125113061/be82" +
                        "af9c593fedfcc40d20b5a9dae43c.png").
                        placeholder(R.mipmap.ic_launcher).
                        thumbnail(0.2f).into(imgView);

                isGlide++;
            }
            else{
                imgView.setVisibility(View.GONE);
                isGlide++;
            }

        }
    };

}
