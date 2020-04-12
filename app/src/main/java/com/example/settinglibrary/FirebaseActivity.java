package com.example.settinglibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class FirebaseActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseActivity";

    private TextView txtFB_version;
    private TextView txtFB_LoginID;
    private Button btnFBAccount;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private SignInButton btnGoogleLogin;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_firebase);

        setReference();

        // FB Auth
        FB_Auth();

        // FB RDB
        LoginFirebase();

        btnFBAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFBAccount.getText().toString().equals("LOGIN")){
                    FB_Auth();
                }else{
                    FirebaseAuth.getInstance().signOut();

                    btnFBAccount.setText("LOGIN");
                    txtFB_LoginID.setText("");
                    Log.e("로그아웃", "성공");
                }

            }
        });

        getFCM();
    }

    private void getFCM() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.e(TAG, "getInstance Failed !" + task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();

                        Log.e(TAG, token);
                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void FB_Auth() {
        // -- 권한 얻기.
        mAuth = FirebaseAuth.getInstance();

        // -- 다이얼로그 띄우기.
        LayoutInflater inflater = getLayoutInflater();
        View viewFB = inflater.inflate(R.layout.login_firebase, null);

        btnGoogleLogin = (SignInButton) viewFB.findViewById(R.id.btnGoogleLogin);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewFB).setCancelable(false);

        dialog = builder.create();

        dialog.show();

        // -- 구글 로그인 정보 가져오기 및 로그인한 사용자 정보 주입
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // -- 구글 로그인 버튼 이벤트
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, Msg.RC_SIGN_IN);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Msg.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                FBAuth_Google(account);
                btnFBAccount.setText("LOGOUT");
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void FBAuth_Google(GoogleSignInAccount account) {
        Log.e("여기타나요", "?");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(FirebaseActivity.this, "성공하셨습니다 !", Toast.LENGTH_SHORT).show();
                            txtFB_LoginID.setText(account.getDisplayName() + " 님이 로그인하셨습니다.");
                            dialog.dismiss();
                        } else {
                            // 로그인 실패
                            Toast.makeText(FirebaseActivity.this, "실패하였습니다 1", Toast.LENGTH_SHORT).show();
                            Log.e("로그인 실패 ! ", account.getDisplayName());
                        }

                    }
                });
    }

    private void LoginFirebase() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = rootRef.getRoot();

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

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setReference() {
        txtFB_version = findViewById(R.id.txtFB_version);
        txtFB_LoginID = findViewById(R.id.txtFB_LoginID);
        btnFBAccount = findViewById(R.id.btnFBAccount);
    }
}
