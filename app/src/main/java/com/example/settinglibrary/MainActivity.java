package com.example.settinglibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    }

    private void setReference() {
        btnQRCode = findViewById(R.id.btnQRCode);

        btnCamera = findViewById(R.id.btnCamera);

        txtFadingTxt = findViewById(R.id.txtFadingTxt);
        btnFadingTxt = findViewById(R.id.btnFadingTxt);

        btnRetrofit2 = findViewById(R.id.btnRetrofit2);

        btnFirebase = findViewById(R.id.btnFirebase);
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
}
