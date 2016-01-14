package com.example.kyly.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

import widget.StickerView.StickerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,GestureDetector.OnGestureListener {
    private GestureDetector mGestureDetector;

    private ArrayList<RelativeLayout> images;
    private ArrayList<TextView> tips;
    private AppCompatButton addTips;
    private AppCompatButton addPics;
    private AppCompatButton addBackground;
    private AppCompatImageView background;
    private RelativeLayout root;
    private Button resizeButton;
    private TextView point;

    private View mCurrentImage;

    private int screenWidth;
    private int screenHeight;

    private int lastX;
    private int lastY;
    private int dx;
    private int dy;

    private int width = 100;
    private int imageWidth=400;
    private int imageHeight=300;

    private boolean isMoving = false;


    //当前的的帖纸
    private StickerView mCurrentView;

    //存储贴纸列表
    private ArrayList<View> mStickers = new ArrayList<>();


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
        point = (TextView) findViewById(R.id.point);

        root.setOnClickListener(this);
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
                addStickerView();
                break;
            case R.id.addbackground:
                if (null != mCurrentView){
                    final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    mCurrentView.setBitmapBackground(bitmap);
                }
                break;
            case R.id.rootlayout:
                if (null != resizeButton) {
                    resizeButton.setVisibility(View.GONE);
                }
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

        if (null != resizeButton){
            resizeButton.setVisibility(View.GONE);
        }
    }

    private void addImage(){
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.image_layout,null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageWidth,imageHeight);
        params.setMargins((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2, (screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2);
        view.setLayoutParams(params);
        view.setOnTouchListener(this);
        images.add(view);
        root.addView(view);
        this.mCurrentImage = view;
        if (null == resizeButton){
            createResizeButton();
        } else {
           showResizeButton();
        }
    }

    //添加帖纸
    private void addStickerView() {
        final StickerView stickerView = new StickerView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        final Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_login_guide);
        stickerView.setWaterMark(bitmap, bgBitmap);
        root.addView(stickerView, lp);
        mStickers.add(stickerView);
        mCurrentView = stickerView;
        //setCurrentEdit(stickerView);
    }


    private void createResizeButton(){
        resizeButton = new Button(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
        params.setMargins((screenWidth + imageWidth) / 2 - width, (screenHeight - imageHeight) / 2 - width, (screenWidth - imageWidth) / 2, (screenHeight + imageHeight) / 2);
        resizeButton.setLayoutParams(params);
        resizeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        resizeButton.setOnTouchListener(this);
        root.addView(resizeButton);
    }

    private void showResizeButton(){
        resizeButton.bringToFront();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) resizeButton.getLayoutParams();
        params.setMargins(mCurrentImage.getLeft() + mCurrentImage.getWidth() - width,mCurrentImage.getTop() - width,screenWidth - mCurrentImage.getRight(),mCurrentImage.getTop());
        resizeButton.setLayoutParams(params);
        resizeButton.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (0 == screenWidth){
            screenWidth = root.getWidth();
            screenHeight = root.getHeight();
            Log.e("screen",screenWidth + ","+screenHeight );
        }

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                if (! (v instanceof Button)){
                    mCurrentImage = v;

                }


                break;
            case MotionEvent.ACTION_MOVE:
                dx =  (int)event.getRawX() - lastX;
                dy = (int)event.getRawY() - lastY;

                if(!isMoving && (Math.abs(dx) + Math.abs(dy) > 3)){
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
                int left = v.getLeft();
                int top = v.getTop();
                int right = screenWidth - left - v.getWidth();
                int bottom = screenHeight - top - v.getHeight();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                params.setMargins(left, top, right, bottom);
                v.setLayoutParams(params);
                if (v instanceof RelativeLayout ) {
                    resizeButton.layout(v.getLeft() + v.getWidth() - width, v.getTop() - width, screenWidth - v.getRight(), screenHeight -v.getTop());
                    params = (RelativeLayout.LayoutParams) resizeButton.getLayoutParams();
                    params.setMargins(resizeButton.getLeft(),resizeButton.getTop(),screenWidth - resizeButton.getLeft() - width,screenHeight - resizeButton.getTop() - width);
                    resizeButton.setLayoutParams(params);
                }
                isMoving = false;
                break;
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private void resizeImage(int dx,int dy){

        Log.e("", "11111111111111111");
        moveView(resizeButton,dx,dy);
        int width = mCurrentImage.getWidth() + dx;
        int height = mCurrentImage.getHeight() - dy;



        int left = mCurrentImage.getLeft();
        int bottom = screenHeight - mCurrentImage.getBottom();
        int top = mCurrentImage.getBottom() - height;
        int right = screenWidth  - mCurrentImage.getLeft() - width;


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width,height);
        layoutParams.setMargins(left, right, top, bottom);
        mCurrentImage.setLayoutParams(layoutParams);
        //mCurrentImage.layout(mCurrentImage.getLeft(),mCurrentImage.getTop() + dy,mCurrentImage.getRight() + dx,mCurrentImage.getBottom());
    }

    private void moveView(View v,int dx,int dy){
        //获取移动后的边距
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;

        //修正坐标点
        if (left < 0){
            left = 0;
            right =  v.getWidth();
            Log.e("warn","left < 0");
        }

        if (right > screenWidth){
            left = screenWidth - v.getWidth();
            right = screenWidth;
            Log.e("warn","right < 0");
        }

        if(top < 0){
            top = 0;
            bottom = v.getHeight();
            Log.e("warn", "top < 0");
        }

        if (bottom > screenHeight){
            bottom = screenHeight;
            top = screenHeight - v.getHeight();
            Log.e("warn","bottom < 0");
        }
        point.setText(getResources().getString(R.string.point, left, top, right, bottom, dx, dy));


//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
//        params.setMargins(left,top,right,bottom);
//        v.setLayoutParams(params);
        v.layout(left, top, right, bottom);
        if (v instanceof RelativeLayout && null != resizeButton) {
            Log.e("","22222222222222");
            resizeButton.layout(v.getLeft() + v.getWidth() - width, v.getTop() - width, v.getRight(), v.getTop());
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
        if (mCurrentImage instanceof RelativeLayout) {
            showResizeButton();
        } else {
            if (null != resizeButton) {
                resizeButton.setVisibility(View.GONE);
            }
        }
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
            if (mCurrentImage instanceof TextView) {
                tips.remove(mCurrentImage);
            } else {
                images.remove(mCurrentImage);

            }
            mCurrentImage.setVisibility(View.GONE);
            root.removeView(mCurrentImage);
            root.removeView(resizeButton);
            resizeButton = null;
            mCurrentImage = null;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }


    /**
     * 设置当前处于编辑模式的贴纸
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            //mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        //stickerView.setInEdit(true);
    }

    private void generateBitmap() {

        Bitmap bitmap = Bitmap.createBitmap(root.getWidth(),
                root.getHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        root.draw(canvas);

        /*String iamgePath = FileUtils.saveBitmapToLocal(bitmap, this);
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtra("image", iamgePath);
        startActivity(intent);*/
    }


}
