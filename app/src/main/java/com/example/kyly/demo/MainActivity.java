package com.example.kyly.demo;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,GestureDetector.OnGestureListener {
    private GestureDetector mGestureDetector;

    private ArrayList<RelativeLayout> images;
    private ArrayList<TextView> tips;
    private AppCompatButton addTips;
    private AppCompatButton addPics;
    private AppCompatButton addBackground;
    private AppCompatImageView background;
    private FrameLayout root;

    private View v;

    private int screenWidth;
    private int screenHeight;

    private int lastX;
    private int lastY;
    private int dx;
    private int dy;

    private boolean isMoving = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        images = new ArrayList<>(3);
        tips = new ArrayList<>(2);
        mGestureDetector = new GestureDetector(this, this);
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
        background = (AppCompatImageView) findViewById(R.id.background);
        root = (FrameLayout) findViewById(R.id.rootlayout);

        addPics.setOnClickListener(this);
        addTips.setOnClickListener(this);
        addBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (screenHeight == 0){
            screenHeight = root.getHeight();
            screenWidth = root.getWidth();
        }
        switch (v.getId()){
            case R.id.addTip:
                addNewTip();
                break;
            case R.id.addpic:
                addImage();
                break;
            case R.id.addbackground:
                break;
        }
    }

    private void addNewTip(){
        TextView textView = new TextView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        textView.setText(tips.size() + "标签");
        params.setMargins(50, 50, 50, 50);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.color.colorAccent);


        textView.setOnTouchListener(this);
        textView.setClickable(true);

        tips.add(textView);
        root.addView(textView);
        textView.postInvalidate();
    }

    private void addImage(){
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.image_layout,null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300,200);
        view.setLayoutParams(params);
        view.setOnTouchListener(this);
        root.addView(view);
        images.add(view);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (0 == screenWidth){
            screenWidth = root.getWidth();
            screenHeight = root.getHeight();
            Log.e("screen",screenWidth + ","+screenHeight );
        }

        int left,right,top,bottom;

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                Log.e("","width="+v.getWidth() +",height="+v.getHeight()+",x="+event.getX()+",y="+event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                dx = (int)event.getRawX() - lastX;
                dy = (int)event.getRawY() - lastY;

                if(!isMoving && (Math.abs(dx) > 3 || Math.abs(dy) > 3)){
                    isMoving = true;
                }


                left = v.getLeft() + dx;
                right = v.getRight() + dx;
                top = v.getTop() + dy;
                bottom = v.getBottom() + dy;

                if (left <= 0){
                    left = 0;
                    right =  v.getWidth();
                    Log.e("warn","left < 0");
                }

                if (left >= screenWidth - v.getWidth()){
                    left = screenWidth - v.getWidth();
                    right = screenWidth;
                    Log.e("warn","right < 0");
                }

                if(top <= 0){
                    top = 0;
                    bottom = v.getHeight();
                    Log.e("warn", "top < 0");
                }

                if (top >= screenHeight - v.getHeight()){
                    bottom = screenHeight;
                    top = screenHeight - v.getHeight();
                    Log.e("warn","bottom < 0");
                }

                v.layout(left, top, right,bottom);
                Log.e("", "right=" + right + ",bottom=" + bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                left = v.getLeft();
                top = v.getTop();
                right = screenWidth - left - v.getWidth();
                bottom = screenHeight - top - v.getHeight();

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                params.setMargins(left,top,right,bottom);
                v.setLayoutParams(params);
                isMoving = false;
                break;
        }
        this.v = v;
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.e("","isMoving="+isMoving);
        if(!isMoving) {
            if (v instanceof TextView) {
                tips.remove(v);
            } else {
                images.remove(v);
            }

            v.setVisibility(View.GONE);
            v = null;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

}
