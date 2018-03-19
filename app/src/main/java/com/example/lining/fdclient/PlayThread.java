package com.example.lining.fdclient;

/**
 * Created by lining on 2018/3/17.
 */
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceHolder;

/**
 * 用于播放的线程
 */
public class PlayThread extends Thread {
    public   MyHandler playHandler;   //用于处理信息
    private static SurfaceHolder mHolder;// SurfaceView的控制器
    private Message msg;
    private int sufaceViewHeight;
    private int sufaceViewWidth;

    public PlayThread(SurfaceHolder mHolder, Message msg,int surfaceHeigth,int surfaceWidth){
        this.mHolder = mHolder;
        this.msg = msg;
        this.sufaceViewHeight =surfaceHeigth;
        this .sufaceViewWidth = surfaceWidth;
    }
    public Handler getPlayHandler(){
        return playHandler;
    }
    public void run() {
        super.run();
        Looper.prepare();
        playHandler = new MyHandler(mHolder,sufaceViewHeight,sufaceViewWidth);
        playHandler.handleMessage(msg);
        Looper.loop();
    }
}
