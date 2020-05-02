package com.example.settinglibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.muddzdev.styleabletoast.StyleableToast;
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
    private Button btnAsyncTask;

    private ImageView imgView;
    private int isGlide = 1;
    private boolean isAsyncTask = true;

    private MyAsyncTask myAsyncTask;

    private Button btnCustomToast;

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
        btnAsyncTask.setOnClickListener(onAsyncTask);
        btnCustomToast.setOnClickListener(onCustomToast);
    }

    private void setReference() {
        btnQRCode = findViewById(R.id.btnQRCode);

        btnCamera = findViewById(R.id.btnCamera);

        txtFadingTxt = findViewById(R.id.txtFadingTxt);
        btnFadingTxt = findViewById(R.id.btnFadingTxt);

        btnRetrofit2 = findViewById(R.id.btnRetrofit2);

        btnFirebase = findViewById(R.id.btnFirebase);

        btnGlide = findViewById(R.id.btnGlide);

        btnAsyncTask = findViewById(R.id.btnAsyncTask);

        imgView = findViewById(R.id.imgView);

        btnCustomToast = findViewById(R.id.btnCustomToast);
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
    // asGif() : GIF image 로딩, centerCrop() : 반 자르기..
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

    private View.OnClickListener onAsyncTask = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        }
    };

    public class MyAsyncTask extends AsyncTask<Void, Void, String>{
        private static final String TAG = "MyAsyncTask";
        private int count = 0;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                Log.e(TAG, "onPreExecute");
            }

            @Override
            protected String doInBackground(Void... voids) {
                for(int i=0; i<10; i++){
                    try {
                        count++;
                        publishProgress();

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return "START";
            }

        @Override
        protected void onProgressUpdate(Void... values) {
            btnAsyncTask.setText((10 - count) + " 초 남았습니다.");

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            btnAsyncTask.setText(s);
        }
    }

    private View.OnClickListener onCustomToast = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            new StyleableToast
//                    .Builder(getApplicationContext())
//                    .text("Hello world!")
//                    .textColor(Color.WHITE)
//                    .backgroundColor(Color.BLUE)
//                    .show();
            StyleableToast.makeText(getApplicationContext(), "Hello World!"
                    , Toast.LENGTH_LONG, R.style.myToast).show();

        }
    };
}
