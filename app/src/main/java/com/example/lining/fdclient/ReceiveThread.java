package com.example.lining.fdclient;


/**
 * Created by lining on 2018/3/17.
 */
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 建立服务端套接字并接受视频数据
 */
public class ReceiveThread extends Thread {
    private Socket socket;
    private ServerSocket serverSocket;
    private Message msg = new Message();   //处理消息
    private PlayThread play;    //定义播放视频的线程
    private int port;
    private static final int COMPLETED = 0x114;  //用于判断是否传递成功
    public SurfaceHolder mHolder;


    public ReceiveThread(SurfaceHolder mHolder, int port) {
        this.port = port;
        play = new PlayThread(mHolder,msg);
    }

    @Override
    public void run() {
        byte buffer[] = new byte[4 * 1024];   //数据缓冲
        int len = 0;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        play.start();
        while (true) {
            InputStream ins;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            try {
                socket = serverSocket.accept();
                Log.i("Client Connected", "!!!!!!!!!!!!");
                ins = socket.getInputStream();
                while ((len = ins.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                ins.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            byte data[] = outStream.toByteArray();   //每张bitmap的数据

            //消息传递
            msg=play.getPlayHandler().obtainMessage();   //获取信息
            msg.what = COMPLETED;
            msg.obj=data;    //获取视频数据
            play.getPlayHandler().sendMessage(msg);   //传递到PlayThread处理信息
            try {
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
