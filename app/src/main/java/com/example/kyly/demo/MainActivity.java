package com.example.kyly.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.kyly.demo.widget.StickerView.StickerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<TextView> tips;
    private AppCompatButton addTips;
    private AppCompatButton addPics;
    private AppCompatButton addBackground;
    private TextView point;



    //帖纸
    private StickerView rootView;

    private FrameLayout l;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //images = new ArrayList<>(3);
        tips = new ArrayList<>(2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == addPics){
            initView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView(){
        addTips = (AppCompatButton) findViewById(R.id.addTip);
        addPics = (AppCompatButton) findViewById(R.id.addpic);
        addBackground = (AppCompatButton) findViewById(R.id.addbackground);
        rootView = (StickerView) findViewById(R.id.rootView);
        point = (TextView) findViewById(R.id.point);


        addPics.setOnClickListener(this);
        addTips.setOnClickListener(this);
        addBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addTip:
                addNewTip();
                break;
            case R.id.addpic:
                addStickerView();
                break;
            case R.id.addbackground:
                    addBackground();
                break;
            case R.id.rootView:
                if (null != rootView) {
                    //取消调整模式
                }
                break;
        }
    }

    private void addBackground(){
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        rootView.setBitmapBackground(bitmap);
    }

    private void addNewTip(){


        point.setDrawingCacheEnabled(true);
        //textView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //textView.buildDrawingCache();
        Bitmap bitmap = point.getDrawingCache();
        if (null != bitmap){
            rootView.addBitMap(bitmap);
        }
    }



    //添加帖纸
    private void addStickerView() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        rootView.addBitMap(bitmap);
    }





}
