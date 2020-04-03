package com.example.settinglibrary;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.CameraFragmentApi;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentControlsAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentStateAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentVideoRecordTextAdapter;
import com.github.florent37.camerafragment.widgets.CameraSettingsView;
import com.github.florent37.camerafragment.widgets.CameraSwitchView;
import com.github.florent37.camerafragment.widgets.FlashSwitchView;
import com.github.florent37.camerafragment.widgets.MediaActionSwitchView;
import com.github.florent37.camerafragment.widgets.RecordButton;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    //region Camera 변수 생성.
    private CameraFragment cameraFragment;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_camera);

        setReference();
        setControls();
    }

    private void setControls() {
        cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, Msg.CAMERA_FRAGMENT)
                .commit();

        flash_switch_view.setOnClickListener(onFlashSwitch);
        front_back_camera_switcher.setOnClickListener(onCameraSwitcher);
        record_button.setOnClickListener(onCameraRecord);
        settings_view.setOnClickListener(onCameraSetting);
        photo_video_camera_switcher.setOnClickListener(onJpgAviSwitcher);
        addCameraButton.setOnClickListener(onAddCamera);
    }

    private void setReference() {
        addCameraButton = findViewById(R.id.addCameraButton);
        cameraLayout = findViewById(R.id.cameraLayout);
        settings_view = findViewById(R.id.settings_view);
        flash_switch_view = (FlashSwitchView) findViewById(R.id.flash_switch_view);
        front_back_camera_switcher = findViewById(R.id.front_back_camera_switcher);
        record_panel = findViewById(R.id.record_panel);
        record_button = findViewById(R.id.record_button);
        record_duration_text = findViewById(R.id.record_duration_text);
        record_size_mb_text = findViewById(R.id.record_size_mb_text);
        photo_video_camera_switcher = findViewById(R.id.photo_video_camera_switcher);
    }

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
            addCamera();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            addCamera();
        }
    }

    public void addCamera() {
        addCameraButton.setVisibility(View.GONE);
        cameraLayout.setVisibility(View.VISIBLE);

        final CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_REAR).build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, Msg.CAMERA_FRAGMENT)
                .commitAllowingStateLoss();


        if (cameraFragment != null) {
            cameraFragment.setStateListener(new CameraFragmentStateAdapter() {

                @Override
                public void onCurrentCameraBack() {
                    front_back_camera_switcher.displayBackCamera();
                }

                @Override
                public void onCurrentCameraFront() {
                    front_back_camera_switcher.displayFrontCamera();
                }

                @Override
                public void onFlashAuto() {
                    flash_switch_view.displayFlashAuto();
                }

                @Override
                public void onFlashOn() {
                    flash_switch_view.displayFlashOn();
                }

                @Override
                public void onFlashOff() {
                    flash_switch_view.displayFlashOff();
                }

                @Override
                public void onCameraSetupForPhoto() {
                    photo_video_camera_switcher.displayActionWillSwitchVideo();

                    record_button.displayPhotoState();
                    flash_switch_view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCameraSetupForVideo() {
                    photo_video_camera_switcher.displayActionWillSwitchPhoto();

                    record_button.displayVideoRecordStateReady();
                    flash_switch_view.setVisibility(View.GONE);
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
                    front_back_camera_switcher.setVisibility(View.VISIBLE);
                    settings_view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onStartVideoRecord(File outputFile) {
                }
            });

            cameraFragment.setControlsListener(new CameraFragmentControlsAdapter() {
                @Override
                public void lockControls() {
                    front_back_camera_switcher.setEnabled(false);
                    record_button.setEnabled(false);
                    settings_view.setEnabled(false);
                    flash_switch_view.setEnabled(false);
                }

                @Override
                public void unLockControls() {
                    front_back_camera_switcher.setEnabled(true);
                    record_button.setEnabled(true);
                    settings_view.setEnabled(true);
                    flash_switch_view.setEnabled(true);
                }

                @Override
                public void allowCameraSwitching(boolean allow) {
                    front_back_camera_switcher.setVisibility(allow ? View.VISIBLE : View.GONE);
                }

                @Override
                public void allowRecord(boolean allow) {
                    record_button.setEnabled(allow);
                }

                @Override
                public void setMediaActionSwitchVisible(boolean visible) {
                    photo_video_camera_switcher.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
            });

            cameraFragment.setTextListener(new CameraFragmentVideoRecordTextAdapter() {
                @Override
                public void setRecordSizeText(long size, String text) {
                    record_size_mb_text.setText(text);
                }

                @Override
                public void setRecordSizeTextVisible(boolean visible) {
                    record_size_mb_text.setVisibility(visible ? View.VISIBLE : View.GONE);
                }

                @Override
                public void setRecordDurationText(String text) {
                    record_duration_text.setText(text);
                }

                @Override
                public void setRecordDurationTextVisible(boolean visible) {
                    record_duration_text.setVisibility(visible ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
    private CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(Msg.CAMERA_FRAGMENT);
    }
}
