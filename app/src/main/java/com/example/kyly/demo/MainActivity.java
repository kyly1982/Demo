package com.example.kyly.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
    private RelativeLayout root;
    private Button resize;

    private View view;

    private int screenWidth;
    private int screenHeight;

    private int lastX;
    private int lastY;
    private int dx;
    private int dy;

    private int width = 60;

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
        root = (RelativeLayout) findViewById(R.id.rootlayout);
        resize = new Button(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
        resize.setLayoutParams(params);
        resize.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        resize.setVisibility(View.GONE);
        resize.setOnTouchListener(this);
        root.addView(resize);


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
        textView.setLayoutParams(params);
        textView.setPadding(50, 50, 50, 50);
        textView.setBackgroundResource(R.color.colorAccent);
        textView.setOnTouchListener(this);
        textView.setClickable(true);

        tips.add(textView);
        root.addView(textView);
        textView.postInvalidate();
    }

    private void addImage(){
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.image_layout,null);
       /* RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300,200);
        params.setMargins(screenWidth / 2 - 150,screenHeight / 2 -100,screenWidth / 2 + 150,screenHeight / 2 + 100);
        view.setLayoutParams(params);*/
        view.setOnTouchListener(this);
        //view.setVisibility(View.GONE);
        root.addView(view);
        view.invalidate();

        view.layout(screenWidth / 2 - 150,screenHeight / 2 -100,screenWidth / 2 + 150,screenHeight / 2 + 100);
        resize.layout(view.getWidth() - width, view.getTop(), view.getRight(), view.getTop() + width);
        resize.setVisibility(View.VISIBLE);
        images.add(view);

        this.view = view;
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
                if (! (v instanceof Button)){
                    view = v;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                dx = (int)event.getRawX() - lastX;
                dy = (int)event.getRawY() - lastY;

                if(!isMoving && (Math.abs(dx) > 3 || Math.abs(dy) > 3)){
                    isMoving = true;
                }

                if (v instanceof Button){
                    resizeImage(dx,dy);
                } else {
                    moveView(v,dx,dy);
                }



                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                left = v.getLeft();
                top = v.getTop();
                right = screenWidth - left - v.getWidth();
                bottom = screenHeight - top - v.getHeight();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                params.setMargins(left, top, right, bottom);
                if (v instanceof RelativeLayout ) {
                    resize.layout(v.getLeft() + v.getWidth() - width, v.getTop(), v.getRight(), v.getTop() + width);
                    params = (RelativeLayout.LayoutParams) resize.getLayoutParams();
                    params.setMargins(resize.getLeft(),resize.getTop(),screenWidth - resize.getLeft() - width,screenHeight - resize.getTop() - width);
                    resize.setLayoutParams(params);
                }
                isMoving = false;
                break;
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private void resizeImage(int dx,int dy){

    }

    private void moveView(View v,int dx,int dy){
        int left,right,top,bottom;
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
        if (v instanceof RelativeLayout ) {
            resize.layout(v.getLeft() + v.getWidth() - width, v.getTop(), v.getRight(), v.getTop() + width);
        }

        if (v instanceof Button){

        }
        Log.e("", "right=" + right + ",bottom=" + bottom);
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
            if (view instanceof TextView) {
                tips.remove(view);
            } else {
                images.remove(view);
            }

            view.setVisibility(View.GONE);
            view = null;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

}
