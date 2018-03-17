package com.example.lining.fdclient;

/**
 * Created by lining on 2018/3/17.
 */

import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.net.Socket;

/**
 * 用于连接到客户端并创建服务端socket
 *
 */

public class LinkThread extends Thread{
    private String localIp;
    private String serverIp;
    private Socket socket;
    private SurfaceHolder mHolder;
    private int serverPort;
    private int clientPort;

    public LinkThread(SurfaceHolder mHolder,String serverIp,String localIp,int receivePort,int sendPort){
        this.serverIp = serverIp;
        this.localIp = localIp;
        this.mHolder = mHolder;
        this.serverPort = receivePort;
        this.clientPort = sendPort;
        new ReceiveThread(mHolder,clientPort).start();
    }

    @Override
    public void run() {
        try{
            Log.i("开始连接到服务端","================================");
            socket = new Socket(serverIp,serverPort);
            Log.i("连接成功","开始发送本地IP到服务端");
            Log.i("本地IP",localIp);
            socket.close();

        }catch (IOException e2) {
            e2.printStackTrace();
            Log.e("连接到服务端失败","--------------------------------");
        }
    }
}
