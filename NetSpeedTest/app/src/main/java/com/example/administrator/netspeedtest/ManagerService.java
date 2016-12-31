package com.example.administrator.netspeedtest;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;

import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.netspeedtest.utils.TrafficInfo;
import com.example.administrator.netspeedtest.utils.WidgetUtils;

import java.text.DecimalFormat;


public class ManagerService extends Service {
    private static final String TAG = "ManagerService";

    // 定义浮动窗口布局
    public LinearLayout mFloatLayout;
    public LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    public  WindowManager mWindowManager;
    public  TextView mFloatView;
    private ServiceBinder binder = new ServiceBinder();
    TrafficInfo speed;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (((double) msg.obj) > 1024) {
                    mFloatView.setText(new DecimalFormat("0.0").format((((double) msg.obj)/1024)) + "m/s");
                } else {
                    mFloatView.setText(msg.obj + "k/s");
                }
            }
            super.handleMessage(msg);
        }
    };
    private int mColorScale;
    private int mColor;
    private String mHexString;

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        speed = new TrafficInfo(this, mHandler, 10035);
        speed.startCalculateNetSpeed();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()){
            case MainActivity.COLOR:
                mColorScale = intent.getIntExtra("color", 000000);
                mColor = mColorScale * ((Integer.parseInt("FFFFFF", 16)/100));
                mHexString = Integer.toHexString(mColor);
                Log.e("color", mHexString);
                if (mHexString.length()==1){
                    mFloatView.setTextColor(Color.parseColor("#000000"));
                }else if (mHexString.length()<=5){
                    mFloatView.setTextColor(Color.parseColor("#0"+ mHexString));
                }else {
                    mFloatView.setTextColor(Color.parseColor("#"+ mHexString));
                }
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
                break;
            case MainActivity.DEFALUT:
                mColorScale = intent.getIntExtra("color", 000000);
                mColor = mColorScale * ((Integer.parseInt("FFFFFF", 16)/100));
                mHexString = Integer.toHexString(mColor);
                Log.e("color",mHexString);
                if (mHexString.length()==1){
                    mFloatView.setTextColor(Color.parseColor("#000000"));
                }else if (mHexString.length()<=5){
                    mFloatView.setTextColor(Color.parseColor("#0"+mHexString));
                }else {
                    mFloatView.setTextColor(Color.parseColor("#"+mHexString));
                }
                wmParams.x = intent.getIntExtra("progress",0)*(WidgetUtils.getScreenWidth(this)/100);
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
                break;
            case MainActivity.MOVE:
                wmParams.x = intent.getIntExtra("progress",0)*(WidgetUtils.getScreenWidth(this)/100);
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
                break;
        }
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("test", " onTrimMemory...");

    }

    private void createFloatView() {
        wmParams = new LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams.type = LayoutParams.TYPE_TOAST;// 设置window
        // type为TYPE_SYSTEM_ALERT
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.TYPE_SEARCH_BAR | LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 默认位置：左上角
        wmParams.width = WidgetUtils.dpToPx(getApplicationContext(), 65);
        wmParams.height = LayoutParams.WRAP_CONTENT;
        wmParams.x = 100;// 设置x、y初始值，相对于gravity
        wmParams.y = 0;
        Log.e("daxiao", "状态栏高度：" + getBarHeight() + "hah" + WidgetUtils.dpToPx(getApplicationContext(), 15));
        // 获取浮动窗口视图所在布局
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mWindowManager.addView(mFloatLayout, wmParams);// 添加mFloatLayout
        mFloatView = (TextView) mFloatLayout.findViewById(R.id.speed);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
                Log.i(TAG, "RawX" + event.getRawX());
                Log.i(TAG, "X" + event.getX());
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2;// 减25为状态栏的高度
                Log.i(TAG, "RawY" + event.getRawY());
                Log.i(TAG, "Y" + event.getY());
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
                return false; // 此处必须返回false，否则OnClickListener获取不到监听
            }
        });
        mFloatView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(ManagerService.this, "tt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getBarHeight() {
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    public void setSpeed(String str) {
        mFloatView.setText(str.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null && mWindowManager != null) {
            mWindowManager.removeView(mFloatLayout);// 移除悬浮窗口
        }
        startService(new Intent(this, ManagerService.class));
    }

    class ServiceBinder extends Binder {
        public ManagerService getService() {
            return ManagerService.this;
        }
    }

}