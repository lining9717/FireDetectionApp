package com.example.lining.fdclient;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;  //SurfaceView
    private SurfaceHolder mHolder;// SurfaceView的控制器
    private String serverIp;             //服务端IP地址
    private static final int receivePort = 8888;
    private static final int sendPort = 8889;
    private int sufaceViewHeight;
    private int sufaceViewWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // 获取服务端IP地址
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        serverIp = data.getString("ipname");

         /* SurfaceHolder设置 */
        mSurfaceView = findViewById(R.id.surfaceview);

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        sufaceViewWidth = outMetrics.widthPixels;
        sufaceViewHeight = outMetrics.heightPixels;
        Log.i("surfaceWidth1111","-------->"+sufaceViewWidth);
        Log.i("surfaceHeight1111","-------->"+sufaceViewHeight);

        /*开启连接线程*/
        new LinkThread(mHolder,serverIp,receivePort,sendPort,sufaceViewHeight,sufaceViewWidth).start();

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
