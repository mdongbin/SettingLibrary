package com.example.settinglibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.settinglibrary.libCalendar.CalendarActivity;
import com.example.settinglibrary.libChart.ChartActivity;
import com.example.settinglibrary.libFirebase.FirebaseActivity;
import com.example.settinglibrary.libRetrofit2.Retrofit2Activity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.muddzdev.styleabletoast.StyleableToast;
import com.squareup.picasso.Picasso;
import com.tomer.fadingtextview.FadingTextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private Button btnQRCode;
    private Button btnCamera;
    private FadingTextView txtFadingTxt;
    private Button btnFadingTxt;
    private Button btnRetrofit2;
    private Button btnFirebase;
    private Button btnImage;
    private Button btnAsyncTask;
    private Button btnChart;
    private Button btnCalendar;
    private Button btnReadFile;
    private ImageView imgView;
    private int isImage = 1;
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

    // region initial References / Controls

    private void setControls() {
        btnQRCode.setOnClickListener(onQRCode);
        btnCamera.setOnClickListener(onCamera);
        btnFadingTxt.setOnClickListener(onFadingTxt);
        btnRetrofit2.setOnClickListener(onRetrofit2);
        btnFirebase.setOnClickListener(onFirebase);
        btnImage.setOnClickListener(onImage);
        btnAsyncTask.setOnClickListener(onAsyncTask);
        btnCustomToast.setOnClickListener(onCustomToast);
        btnChart.setOnClickListener(onChart);
        btnCalendar.setOnClickListener(onCalendar);
        btnReadFile.setOnClickListener(onReadFile);
    }

    private void setReference() {
        btnQRCode = findViewById(R.id.btnQRCode);

        btnCamera = findViewById(R.id.btnCamera);

        txtFadingTxt = findViewById(R.id.txtFadingTxt);
        btnFadingTxt = findViewById(R.id.btnFadingTxt);

        btnRetrofit2 = findViewById(R.id.btnRetrofit2);

        btnFirebase = findViewById(R.id.btnFirebase);

        btnImage = findViewById(R.id.btnImage);

        btnAsyncTask = findViewById(R.id.btnAsyncTask);

        imgView = findViewById(R.id.imgView);

        btnCustomToast = findViewById(R.id.btnCustomToast);

        btnChart = findViewById(R.id.btnChart);

        btnCalendar = findViewById(R.id.btnCalendar);

        btnReadFile = findViewById(R.id.btnReadFile);
    }

    //endregion

    // region QRCode(ZXing)

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

    // endregion

    // region Camera2

    private View.OnClickListener onCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            startActivity(intent);
        }
    };

    // endregion

    //region Custom Fading Text

    private View.OnClickListener onFadingTxt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] values = {"ANDROID", "DEVELOPER", "JUNIOR"};
            txtFadingTxt.setTexts(values);
        }
    };

    //endregion

    // region (Connecting lib) Retrofit2

    private View.OnClickListener onRetrofit2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Retrofit2Activity.class);
            startActivity(intent);
        }
    };

    //endregion

    // region Firebase - Auth / RDB / FCM
    private View.OnClickListener onFirebase = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FirebaseActivity.class);
            startActivity(intent);
        }
    };

    //endregion

    //region (image lib) Glide

    // with : Context, load : URI, placeholder : 로딩 이미지, error : 오류 이미지
    // override : 이미지 크기(메모리 절약 차원), into : imgView, thumbnail() : 비율 흐릿하게 보여줌.
    // asGif() : GIF image 로딩, centerCrop() : 반 자르기..
    // Picasso :  이미지 사이즈 변환없이 그대로 표출, 원본 이미지기 때문에 용량이 큼
    //            이미지 로딩이 빠름(사이즈 무변환), 캐시 이미지 로딩 느림, 고품질, GIF 지원 불가
    // Glide : Picasso에 비해 많은 기능, 이미지 사이즈 줄여 저장, 상대적으로 적은 메모리
    //          이미지 로딩 느림(사이즈 변환), 캐시 이미지 로딩 빠름, 저품질, GIF 지원 가능.
    private View.OnClickListener onImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = "https://image.fmkorea.com/files/attac" +
                    "h/new/20180627/425547776/837628905/1125113061/be82" +
                    "af9c593fedfcc40d20b5a9dae43c.png";

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose Library")
                    .setMessage("What is the Image Library?")
                    .setPositiveButton("GLIDE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(isImage % 2 == 1){
                                imgView.setVisibility(View.VISIBLE);
                                Glide.with(MainActivity.this).load(url).
                                        placeholder(R.mipmap.ic_launcher).
                                        thumbnail(0.2f).into(imgView);

                                isImage++;
                            }
                            else{
                                imgView.setVisibility(View.GONE);
                                isImage++;
                            }
                        }
                    })
                    .setNegativeButton("PICASSO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // url 부분에 drawble 경로를 넣어도 무방.
                            if(isImage % 2 == 1){
                                imgView.setVisibility(View.VISIBLE);
                                Picasso.get().load(url).into(imgView);

                                isImage++;
                            }
                            else{
                                imgView.setVisibility(View.GONE);
                                isImage++;
                            }
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();






        }
    };

    //endregion

    //region Making Single Thread(AsyncTask)

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

    //endregion

    // region StyleableToast

    // Font Download : https://www.fontsquirrel.com/fonts/calistoga
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

    // endregion

    // region Chart

    private View.OnClickListener onChart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            startActivity(intent);
        }
    };

    // endregion

    // region Calendar

    private View.OnClickListener onCalendar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        }
    };

    // endregion

    //region Read txt File
    private View.OnClickListener onReadFile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StringBuffer sb = new StringBuffer();

            try{
                InputStream is = getResources().openRawResource(R.raw.test);
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                byte[] buffer = new byte[is.available()];


//                while(br.readLine() != null){
//                    sb.append(br.readLine()).append("\n");
//                }

                while(is.read(buffer) != -1);

                String result = new String(buffer);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(result);

                AlertDialog dialog = builder.create();
                dialog.show();

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    };

    //endregion
}
