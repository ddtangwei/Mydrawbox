package com.example.ddtangwei.mydrawbox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends Activity {

    private Paint paint;
    private Bitmap copyBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("hehe");

        //[1]找到显示我们画的内容
        final ImageView imageView = (ImageView) findViewById(R.id.iv);

        //[2]把bg转换成bitmap
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

        //[2.1]创建模板
        copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        //[2.2]以copybitmap为模板  创建一个画布
        final Canvas canvas = new Canvas(copyBitmap);

        //[2.3]创建一个画笔
        paint = new Paint();

        //[2.4]开始作画
        canvas.drawBitmap(srcBitmap,new Matrix(),paint);

        //[3]把copybitmap显示到iv上
        imageView.setImageBitmap(copyBitmap);

        //[4]给iv设置一个触摸事件
        imageView.setOnTouchListener(new View.OnTouchListener() {
            float startX = 0;
            float startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //[5]获取手指触摸的事件类型
                int action = event.getAction();

                //[6]具体判断一下是什么事件类型
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        //[7]获取手指按下坐标
                        startX = event.getX();
                        startY = event.getY();
                        System.out.println(startX+"---"+startY);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        //[8]获取停止的坐标
                        float stopX = event.getX();
                        float stopY = event.getY();
                        System.out.println(stopX+"---"+stopY);

                        //[9]画线
                        canvas.drawLine(startX,startY,stopX,stopY,paint);

                        //[9.1]更新一下起点坐标
                        startX = stopX;
                        startY = stopY;

                        //[10]记得更新ui
                        imageView.setImageBitmap(copyBitmap);

                        break;

                    case MotionEvent.ACTION_UP: //抬起;

                        break;

                }

                return true;
            }
        });
    }

    //点击按钮让画笔的颜色 变成红色
    public void click1(View view){
        //设置画笔颜色
        paint.setColor(Color.BLUE);
    }

    public void click2(View view){
        //让画笔颜色变粗
        paint.setStrokeWidth(16);
    }

    //save
    public void click3(View view){

        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "hehe.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            System.out.println("wozaizheli");

            copyBitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

            //发送一条sd卡挂载上来的广播 欺骗一下系统图库应用 说sd卡被挂载了 你去加载图片吧
            Intent intent = new Intent();
            //设置action
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);

            //设置data
            intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));

            //发送无序广播
            sendBroadcast(intent);

            fileOutputStream.close();

            System.out.println("owesome");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
