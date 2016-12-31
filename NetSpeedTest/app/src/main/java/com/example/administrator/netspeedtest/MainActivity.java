package com.example.administrator.netspeedtest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.administrator.netspeedtest.utils.SPUtils;

public class MainActivity extends AppCompatActivity {


    public final static String DEFALUT="DEFALUT";
    public final static String COLOR="COLOR";
    public final static String MOVE="MOVE";
    private SeekBar mSbLocation;
    private SeekBar mSbColor;
    private SPUtils mSpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpUtils = new SPUtils("chushihua");
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(MainActivity.this, ManagerService.class);
        intent.setAction(MainActivity.DEFALUT);
        //位置
        int progress = mSpUtils.getInt("progress", 50);
        intent.putExtra("progress",progress);
        //颜色
        int color = mSpUtils.getInt("color",000000);
        intent.putExtra("color",color);
        startService(intent);
        mSbLocation = (SeekBar) findViewById(R.id.sb_location);
        mSbLocation.setMax(100);
        mSbLocation.setProgress(progress);
        mSbLocation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("weizhi",progress+"onProgressChanged");
                intent.setAction(MainActivity.MOVE);
                intent.putExtra("progress",progress);
                startService(intent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("weizhi","onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("weizhi","onStopTrackingTouch");
                mSpUtils.putInt("progress",seekBar.getProgress());
            }
        });
        mSbColor = (SeekBar) findViewById(R.id.sb_color);
        mSbColor.setMax(100);
        mSbColor.setProgress(color);
        mSbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int color, boolean fromUser) {
                Log.e("color",color+"onProgressChanged");
                intent.setAction(MainActivity.COLOR);
                intent.putExtra("color",color);
                startService(intent);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("color","onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSpUtils.putInt("color",seekBar.getProgress());
                Log.e("color","onStopTrackingTouch");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
