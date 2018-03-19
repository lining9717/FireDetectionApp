package com.example.lining.fdclient;

/**
 * Created by lining on 2018/3/17.
 */

import android.util.Log;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 用于连接到客户端并创建服务端socket
 */

public class LinkThread extends Thread {
    private String serverIp;
    private Socket socket;
    private int serverPort;
    private IJudgeIP Judge;

    public LinkThread(IJudgeIP judge, String serverIp, int receivePort) {
        this.Judge = judge;
        this.serverIp = serverIp;
        this.serverPort = receivePort;

    }

    @Override
    public void run() {
        try {
            Log.i("开始连接到服务端", "================================");
            socket = new Socket();
            SocketAddress socAddress = new InetSocketAddress(serverIp, serverPort);
            socket.connect(socAddress, 3000);
            //socket = new Socket(serverIp, serverPort);
            Log.i("连接成功", "开始发送本地IP到服务端");
            Judge.connectStatus(true);
        } catch (IOException e2) {
            Judge.connectStatus(false);
            e2.printStackTrace();
            Log.e("连接到服务端失败", "--------------------------------");
        } finally {
            close(socket);
        }
    }

    public void close(Closeable object) {
        if (object == null) {
            return;
        }
        try {
            object.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
