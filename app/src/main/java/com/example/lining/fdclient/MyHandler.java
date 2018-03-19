package com.example.lining.fdclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Message;
import android.view.SurfaceHolder;

/**
 * Created by lining on 2018/3/17.
 */

public class MyHandler extends android.os.Handler {
    private Canvas canvas;   //画布
    private int orientationDegree = 0;
    private Matrix m;
    private Paint paint;  //画笔
    private final int COMPLETED = 0x114;  //用于判断是否传递成功
    private SurfaceHolder mHolder;// SurfaceView的控制器
    private int sufaceViewHeight;
    private int sufaceViewWidth;

    public MyHandler(SurfaceHolder mHolder, int surfaceHeigth, int surfaceWidth) {
        this.sufaceViewHeight = surfaceHeigth;
        this.sufaceViewWidth = surfaceWidth;
        m = new Matrix();
        paint = new Paint();
        this.mHolder = mHolder;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == COMPLETED) {
            byte data[] = (byte[]) msg.obj;   //接收图像数据
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            //设置图片方向
            m.setRotate(orientationDegree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            float scaleX = sufaceViewWidth / bitmap.getWidth();
            float scaleY = sufaceViewHeight / bitmap.getHeight();
            bitmap = bitMapScale(bitmap, scaleX, scaleY);

            canvas = mHolder.lockCanvas(); // 通过lockCanvas加锁并得到該SurfaceView的画布
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            canvas.drawBitmap(bitmap, m, paint);
            mHolder.unlockCanvasAndPost(canvas); // 释放锁并提交画布进行重绘

            bitmap.recycle();   //释放bitmap
            super.handleMessage(msg);
        }

    }

    public Bitmap bitMapScale(Bitmap bitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
}
