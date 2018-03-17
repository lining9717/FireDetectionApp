package com.example.lining.fdclient;

/**
 * Created by lining on 2018/3/17.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.SurfaceHolder;

/**
 * 用于播放的线程
 */
public class PlayThread extends Thread {
    private static Handler playHandler;   //用于处理信息
    private Canvas canvas;   //画布
    private int orientationDegree = 90;
    private Matrix m = new Matrix();
    private Paint paint = new Paint();  //画笔
    private static final int COMPLETED = 0x114;  //用于判断是否传递成功
    private SurfaceHolder mHolder;// SurfaceView的控制器

    public PlayThread(SurfaceHolder mHolder){
        this.mHolder = mHolder;
    }

    public Handler getPlayHandler(){
        return playHandler;
    }


    public void run() {
        super.run();
        Looper.prepare();
        //信息处理，转换为视频，着重修改
        playHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COMPLETED) {
                    byte data[] = (byte[]) msg.obj;   //接收图像数据
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                    //设置图片方向
                    m.setRotate(orientationDegree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                    float targetX, targetY;
                    if (orientationDegree == 90) {
                        targetX = bitmap.getHeight();
                        targetY = 0;
                    } else {
                        targetX = bitmap.getHeight();
                        targetY = bitmap.getWidth();
                    }
                    final float[] values = new float[9];
                    m.getValues(values);
                    float x1 = values[Matrix.MTRANS_X];
                    float y1 = values[Matrix.MTRANS_Y];
                    m.postTranslate(targetX - x1, targetY - y1);
                    canvas = mHolder.lockCanvas(); // 通过lockCanvas加锁并得到該SurfaceView的画布
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    canvas.drawBitmap(bitmap, m, paint);
                    mHolder.unlockCanvasAndPost(canvas); // 释放锁并提交画布进行重绘
                    bitmap.recycle();   //释放bitmap
                    super.handleMessage(msg);
                }
                else return;
            }
        };
        Looper.loop();
    }
}
