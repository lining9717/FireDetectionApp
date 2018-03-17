package com.example.lining.fdclient;

import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by lining on 2018/3/17.
 */
/**
 * 接收视频数据的线程
 */
public class ReceiveThread extends Thread  {
    private Socket s;          //接收的Socket
    private Message msg = new Message();   //处理消息
    private PlayThread play;    //定义播放视频的线程
    private static final int COMPLETED = 0x114;  //用于判断是否传递成功
    private String serverIp;             //IP地址

    public ReceiveThread(SurfaceHolder mHolder,String serverIp){
        this.serverIp = serverIp;
        play = new PlayThread(mHolder);
    }


    public void run() {
        byte buffer[] = new byte[4 * 1024];   //数据缓冲
        int len = 0;
        try {
            s = new Socket(serverIp, 8888);
            Log.d("State", "connect server!!!");
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        play.start();    //开启播放线程，只能在循环之外

        //获取视频数据
        InputStream ins = null;
        try {
            ins = s.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            while ((len = ins.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            ins.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte data[] = outStream.toByteArray();   //每张bitmap的数据
        //消息传递
        msg = play.getPlayHandler().obtainMessage();   //获取信息
        msg.what = COMPLETED;
        msg.obj = data;    //获取视频数据

        play.getPlayHandler().sendMessage(msg);   //传递到PlayThread处理信息
        try {
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
