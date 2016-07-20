package com.gson8.myscreenshot;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.gson8.myscreenshot.fileselector.config.FileConfig;
import com.gson8.myscreenshot.fileselector.dialog.FileDialog;
import com.gson8.myscreenshot.video.DpiBean;
import com.gson8.myscreenshot.video.MyShoter;
import com.gson8.myscreenshot.video.SpinnerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    public static final int REQUEST_CODE = 0x123;
    public static final int REQUEST_SDCARD_CODE = 0x1243;
    public static final int REQUEST_SETTING_CODE = 0x1223;
    private static final String TAG = "TAGGG";


    private Button mBtnShotStart;
    private Button mBtnOp;

    private Spinner mDpiSizeSp;
    private SpinnerAdapter mDpiAdapter;

    private Spinner mBitRateSp;
    private SpinnerAdapter mBitRateAdapter;

    private Spinner mFpsSp;
    private SpinnerAdapter mFpsAdapter;

    private Switch mShowTouchSwitch;


    //初始化分辨率
    private DpiBean mDpiSizeBean;
    private List<DpiBean> mSizes;


    //初始化bitRate
    private int mBitRate = 600000;         //6 Mbps
    private Integer[] BIT_RATE_DATA =
            {500000, 1000000, 2000000, 2500000, 3000000, 4000000, 5000000, 6000000, 8000000,
                    10000000, 12000000};

    //初始化FPS
    private int mFps = 5;
    private Integer[] FPS_DATA = {5, 6, 8, 10, 12, 15, 18, 20, 24, 30};

    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;

    private MyShoter mShoter;


    private File mFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();


        initDPIData();

        initEvent();

        mMediaProjectionManager =
                (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        checkSDCardPermission();
    }


    private void initView() {

        mBtnShotStart = (Button) findViewById(R.id.id_shot_video);
        mBtnOp = (Button) findViewById(R.id.id_video_op);
        mDpiSizeSp = (Spinner) findViewById(R.id.id_sp_select_dpi);
        mBitRateSp = (Spinner) findViewById(R.id.id_sp_select_mbps);
        mFpsSp = (Spinner) findViewById(R.id.id_sp_select_fps);
        mShowTouchSwitch = (Switch) findViewById(R.id.id_show_touch);


    }

    private void initDPIData() {
        mSizes = new ArrayList<>();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDpiSizeBean = new DpiBean(metrics.widthPixels, metrics.heightPixels);
        if(mSizes.size() == 0) {
            int h = mDpiSizeBean.getHeight();
            if(h > 1920)
                mSizes.add(mDpiSizeBean);
            if(h == 1920)
                mSizes.add(new DpiBean(1080, 1920));
            if(h >= 1280)
                mSizes.add(new DpiBean(720, 1280));
            if(h >= 960)
                mSizes.add(new DpiBean(540, 960));
            if(h >= 854)
                mSizes.add(new DpiBean(480, 854));
            if(h >= 640)
                mSizes.add(new DpiBean(360, 640));
            if(h >= 426)
                mSizes.add(new DpiBean(240, 426));
        }
    }

    private void initEvent() {
        mBtnShotStart.setOnClickListener(this);
        mBtnOp.setOnClickListener(this);
        mShowTouchSwitch.setOnCheckedChangeListener(this);


        mDpiAdapter = new SpinnerAdapter(this, mSizes.toArray());
        mDpiSizeSp.setAdapter(mDpiAdapter);

        mBitRateAdapter = new SpinnerAdapter(this, BIT_RATE_DATA);
        mBitRateSp.setAdapter(mBitRateAdapter);

        mFpsAdapter = new SpinnerAdapter(this, FPS_DATA);
        mFpsSp.setAdapter(mFpsAdapter);


        mDpiSizeSp.setOnItemSelectedListener(this);
        mBitRateSp.setOnItemSelectedListener(this);
        mFpsSp.setOnItemSelectedListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkSDCardPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            //没有权限,去申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SDCARD_CODE);

        }
    }

    private void checkTwoSettings() {
        boolean retVal = Settings.System.canWrite(this);
        if(!retVal) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    /**
     * 显示触摸操作
     *
     * @param b
     */
    public void showTouchDot(boolean b) {
        if(b) {
            Settings.System.putInt(getContentResolver(),
                    "show_touches", 1);
        } else {
            Settings.System.putInt(getContentResolver(),
                    "show_touches", 0);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == REQUEST_SDCARD_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "没有写入内存卡的权限", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == REQUEST_SETTING_CODE) {
            if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            } else {
                checkTwoSettings();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case R.id.id_show_touch:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.System.canWrite(this)) {
                        mShowTouchSwitch.setChecked(false);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_SETTING_CODE);
                    } else {
                        showTouchDot(isChecked);
                    }
                } else {
                    // < 6.0
                    showTouchDot(isChecked);
                }
                break;
        }
    }


    private void shot() {
        if(mShoter != null) {
            mShoter.stopShot();
            mShoter = null;
            mBtnShotStart.setText("开始录制屏幕");
        } else {
            Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE) {
            startShotNow(resultCode, data);
        }
        if(requestCode == REQUEST_SETTING_CODE) {
            if(Settings.System.canWrite(this)) {
                //检查返回结果
                Toast.makeText(this, "OK",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有修改系统设置的权限",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startShotNow(int resultCode, Intent data) {
        mMediaProjection =
                mMediaProjectionManager.getMediaProjection(resultCode, data);

        if(mMediaProjection == null) {
            Log.e(TAG, "MediaProjection is null");
            return;
        }

        mFile = new File(getRandomFileName());

        Log.e(TAG, "startShotNow: " + mDpiSizeBean.getHeight() + "  " + mBitRate + "  " + mFps);


        mShoter = new MyShoter(mDpiSizeBean.getWidth(), mDpiSizeBean.getHeight(), mBitRate, 1,
                mFps, mMediaProjection, mFile.getAbsolutePath());

        new Thread(mShoter).start();
        mBtnShotStart.setText("停止录制");

        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mShoter != null) {
            mShoter.stopShot();
            mShoter = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.id_shot_video:
                shot();
                break;
            case R.id.id_video_op:
                opTheVideo();
                break;
        }
    }

    private void opTheVideo() {

        FileConfig config = new FileConfig();

        FileDialog fileDialog = new FileDialog(this, config);
        fileDialog.showDialog();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.id_sp_select_dpi:
                mDpiSizeBean = mSizes.get(position);
                break;
            case R.id.id_sp_select_mbps:
                mBitRate = BIT_RATE_DATA[position];
                break;
            case R.id.id_sp_select_fps:
                mFps = FPS_DATA[position];
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 返回一个文件名
     *
     * @return
     */
    public String getRandomFileName() {
        return Environment.getExternalStorageDirectory() + "/Video_" +
                ((int) (System.currentTimeMillis() / 1000)) + ".mp4";
    }
}
