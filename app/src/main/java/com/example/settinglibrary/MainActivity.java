package com.example.settinglibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.github.florent37.camerafragment.widgets.CameraSettingsView;
import com.github.florent37.camerafragment.widgets.CameraSwitchView;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.MediaActionSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button btnQRCode;

    //region Camera 변수 생성.
    private CameraFragment cameraFragment;
    private Button btnCamera;
    private Button addCameraButton;
    private RelativeLayout cameraLayout;
    private CameraSettingsView settings_view;
    private FlashSwitchView flash_switch_view;
    private CameraSwitchView front_back_camera_switcher;
    private RelativeLayout record_panel;
    private RecordButton record_button;
    private TextView record_duration_text;
    private TextView record_size_mb_text;
    private MediaActionSwitchView photo_video_camera_switcher;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setReference();
        setControls();
    }

    private void setControls() {
        btnQRCode.setOnClickListener(onQRCode);

        //region Camera Controls
        btnCamera.setOnClickListener(onCamera);
        flash_switch_view.setOnClickListener(onFlashSwitch);
        front_back_camera_switcher.setOnClickListener(onCameraSwitcher);
        record_button.setOnClickListener(onCameraRecord);
        settings_view.setOnClickListener(onCameraSetting);
        photo_video_camera_switcher.setOnClickListener(onJpgAviSwitcher);
        addCameraButton.setOnClickListener(onAddCamera);
    }

    private void setReference() {
        btnQRCode = findViewById(R.id.btnQRCode);

        //region Camera Reference
        cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());
        btnCamera = findViewById(R.id.btnCamera);
        addCameraButton = findViewById(R.id.addCameraButton);
        cameraLayout = findViewById(R.id.cameraLayout);
        settings_view = findViewById(R.id.settings_view);
        flash_switch_view = findViewById(R.id.flash_switch_view);
        front_back_camera_switcher = findViewById(R.id.front_back_camera_switcher);
        record_panel = findViewById(R.id.record_panel);
        record_button = findViewById(R.id.record_button);
        record_duration_text = findViewById(R.id.record_duration_text);
        record_size_mb_text = findViewById(R.id.record_size_mb_text);
        photo_video_camera_switcher = findViewById(R.id.photo_video_camera_switcher);
        //endregion
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

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, cameraFragment, "Camera")
                    .commit();
        }
    };

    private View.OnClickListener onFlashSwitch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CameraFragmentApi cameraFragment = getCameraFragment();
            if (cameraFragment != null) {
                cameraFragment.toggleFlashMode();
            }
        }
    };

    private View.OnClickListener onCameraSwitcher = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CameraFragmentApi cameraFragment = getCameraFragment();
            if (cameraFragment != null) {
                cameraFragment.switchCameraTypeFrontBack();
            }
        }
    };

    private View.OnClickListener onCameraRecord = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CameraFragmentApi cameraFragment = getCameraFragment();
            if (cameraFragment != null) {
                cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultAdapter() {
                       @Override
                       public void onVideoRecorded(String filePath) {
                           Toast.makeText(getBaseContext(), "onVideoRecorded " + filePath, Toast.LENGTH_SHORT).show();
                       }

                       @Override
                       public void onPhotoTaken(byte[] bytes, String filePath) {
                           Toast.makeText(getBaseContext(), "onPhotoTaken " + filePath, Toast.LENGTH_SHORT).show();
                       }
                   },
    "/storage/self/primary",
    "photo0");
            }
        }
    };

    private View.OnClickListener onCameraSetting = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CameraFragmentApi cameraFragment = getCameraFragment();
            if (cameraFragment != null) {
                cameraFragment.openSettingDialog();
            }
        }
    };

    private View.OnClickListener onJpgAviSwitcher = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CameraFragmentApi cameraFragment = getCameraFragment();
            if (cameraFragment != null) {
                cameraFragment.switchActionPhotoVideo();
            }
        }
    };

    private View.OnClickListener onAddCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                final String[] permissions = {
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};

                final List<String> permissionsToRequest = new ArrayList<>();
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToRequest.add(permission);
                    }
                }
                if (!permissionsToRequest.isEmpty()) {
                    ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), Msg.REQUEST_CAMERA_PERMISSIONS);
                } else addCamera();
            } else {if (grantResults.length != 0) {
                addCamera();
            }
                addCamera();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            addCamera();
        }
    }

    public void addCamera(){

        @Override
        public void shouldRotateControls(int degrees) {
            ViewCompat.setRotation(onCameraSwitcher, degrees);
            ViewCompat.setRotation(onJpgAviSwitcher, degrees);
            ViewCompat.setRotation(onFlashSwitch, degrees);
            ViewCompat.setRotation(onCameraRecord, degrees);
            ViewCompat.setRotation(record_size_mb_text, degrees);
        }

        @Override
        public void onRecordStateVideoReadyForRecord() {
            record_button.displayVideoRecordStateReady();
        }

        @Override
        public void onRecordStateVideoInProgress() {
            record_button.displayVideoRecordStateInProgress();
        }

        @Override
        public void onRecordStatePhoto() {
            record_button.displayPhotoState();
        }

        @Override
        public void onStopVideoRecord() {
            record_size_mb_text.setVisibility(View.GONE);
            //cameraSwitchView.setVisibility(View.VISIBLE);
            settings_view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onStartVideoRecord(File outputFile) {
        }
    });

        cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
            @Override
            public void lockControls() {
                cameraSwitchView.setEnabled(false);
                recordButton.setEnabled(false);
                settingsView.setEnabled(false);
                flashSwitchView.setEnabled(false);
            }

            @Override
            public void unLockControls() {
                cameraSwitchView.setEnabled(true);
                recordButton.setEnabled(true);
                settingsView.setEnabled(true);
                flashSwitchView.setEnabled(true);
            }

            @Override
            public void allowCameraSwitching(boolean allow) {
                cameraSwitchView.setVisibility(allow ? View.VISIBLE : View.GONE);
            }

            @Override
            public void allowRecord(boolean allow) {
                recordButton.setEnabled(allow);
            }

            @Override
            public void setMediaActionSwitchVisible(boolean visible) {
                mediaActionSwitchView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        });

        cameraFragment.setTextListener(new CameraFragmentVideoRecordTextAdapter() {
            @Override
            public void setRecordSizeText(long size, String text) {
                recordSizeText.setText(text);
            }

            @Override
            public void setRecordSizeTextVisible(boolean visible) {
                recordSizeText.setVisibility(visible ? View.VISIBLE : View.GONE);
            }

            @Override
            public void setRecordDurationText(String text) {
                recordDurationText.setText(text);
            }

            @Override
            public void setRecordDurationTextVisible(boolean visible) {
                recordDurationText.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });

    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(Msg.FRAGMENT_TAG);
    }
}
