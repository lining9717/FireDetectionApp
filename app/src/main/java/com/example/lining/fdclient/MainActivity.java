package com.example.lining.fdclient;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final int sendPort = 8889;
    private ReceiveThread receiveThread;

    public static Semaphore semaphore;
    public static boolean flag;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            flag = true;
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            receiveThread.play.playHandler.sendMessage(null);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                flag = true;
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                receiveThread.play.playHandler.sendMessage(null);
                this.finish(); // back button
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        semaphore = new Semaphore(0);
        flag = false;
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        int sufaceViewHeight;
        int sufaceViewWidth;
        SurfaceView mSurfaceView;  //SurfaceView
        SurfaceHolder mHolder;// SurfaceView的控制器
        String serverIp;             //服务端IP地址

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

        receiveThread = new ReceiveThread(mHolder, sendPort, sufaceViewHeight, sufaceViewWidth);
        receiveThread.start();
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
