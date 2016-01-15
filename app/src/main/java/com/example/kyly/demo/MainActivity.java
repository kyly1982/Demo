package com.example.kyly.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.example.kyly.demo.widget.StickerView.StickerView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<TextView> tips;
    private AppCompatButton addTips;
    private AppCompatButton addPics;
    private AppCompatButton addBackground;
    private TextView point;


    //帖纸
    private StickerView rootView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //images = new ArrayList<>(3);
        tips = new ArrayList<>(2);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == addPics) {
            initView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
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

        switch (v.getId()) {
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

    private void addBackground() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        rootView.setBitmapBackground(bitmap);
    }

    private void addNewTip() {
        TextView textView = new TextView(this);
        StickerView.LayoutParams params = new StickerView.LayoutParams(200, 50);
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        textView.setText("测试测试仪");
        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        rootView.addView(textView);
    }


    //添加帖纸
    private void addStickerView() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        rootView.addBitMap(bitmap);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.kyly.demo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.kyly.demo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
