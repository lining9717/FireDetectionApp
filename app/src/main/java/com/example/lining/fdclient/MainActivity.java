package com.example.lining.fdclient;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;  //SurfaceView
    private SurfaceHolder mHolder;// SurfaceView的控制器
    private String serverIp;             //服务端IP地址
    private String localIp;              //本机IP
    private static final int receivePort = 8888;
    private static final int sendPort = 8889;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        /**
         * 获取ip地址并显示
         */
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启,wifi未开启时，返回的ip为0.0.0.0
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        localIp = intToIp(wifiInfo.getIpAddress());

        // 获取服务端IP地址
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        serverIp = data.getString("ipname");

         /* SurfaceHolder设置 */
        mSurfaceView = findViewById(R.id.surfaceview);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Thread th = new LinkThread(mHolder,serverIp,localIp,receivePort,sendPort);
        th.start();
    }

    //转换IP地址
    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
