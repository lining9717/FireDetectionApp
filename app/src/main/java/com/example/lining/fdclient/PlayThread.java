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
    private  MyHandler playHandler;   //用于处理信息
    private static SurfaceHolder mHolder;// SurfaceView的控制器
    private Message msg;

    public PlayThread(SurfaceHolder mHolder, Message msg){
        this.mHolder = mHolder;
        this.msg = msg;
    }
    public Handler getPlayHandler(){
        return playHandler;
    }
    public void run() {
        super.run();
        Looper.prepare();
        //信息处理，转换为视频，着重修改
        playHandler = new MyHandler(mHolder);
        playHandler.handleMessage(msg);
        Looper.loop();
    }
}
