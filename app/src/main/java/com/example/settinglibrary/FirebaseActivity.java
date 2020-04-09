package com.example.settinglibrary;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseActivity";

    private TextView txtFB_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_firebase);

        setReference();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = rootRef.getRoot();
        Log.e("버전 입력", "Suc2222cess");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseDB data = dataSnapshot.child("version").getValue(FirebaseDB.class);
                txtFB_version.setText("FB version : " + data.getVer_01());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setReference() {
        txtFB_version = findViewById(R.id.txtFB_version);
    }
}
