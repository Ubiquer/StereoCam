package com.example.arek.videostremingtirt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout.LayoutParams;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int MIN_WIDTH = 100;

    private FrameLayout.LayoutParams layoutParams;

    private MyVideoView myVideoView;

    private ScaleGestureDetector scaleGestureDetector;

    private GestureDetector gestureDetector;


    public void takePhoto (View view) {


        //camera.setImageResource(R.drawable.screenshotButton);

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {

            String myPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            View view1 = getWindow().getDecorView().getRootView();
            view1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
            view1.setDrawingCacheEnabled(false);

            File imageFile = new File(myPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);


        } catch (Throwable e) {

            e.printStackTrace();

        }
    }

        private void openScreenshot(File imageFile){

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(imageFile);
            intent.setDataAndType(uri, "image/*");
            startActivity(intent);

    }


    //}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutParams = (LayoutParams) ((View) findViewById(R.id.root_view)).getLayoutParams();
        myVideoView = (MyVideoView) findViewById(R.id.myVideoView);
        ImageView imageView = (ImageView) findViewById(R.id.imageButtonPhoto);

        // Video Uri
        String uri = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov";
        myVideoView.setVideoURI(Uri.parse(uri));

        //set up media controller
        MediaController mediaController = new MediaController(this);
        myVideoView.setMediaController(mediaController);
        mediaController.setAnchorView(myVideoView);
//        myVideoView.setMediaController(new MediaController(this));

        // set up gesture listeners
        scaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureListener());
        gestureDetector = new GestureDetector(this, new MySimpleOnGestureListener());
        myVideoView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        myVideoView.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        myVideoView.pause();
        super.onPause();
    }

    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (myVideoView == null)
                return false;
            if (myVideoView.isPlaying())
                myVideoView.pause();
            else
                myVideoView.start();
            return true;
        }

    }

    private class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private int myWidth, myHigh;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // scale our video view
            myWidth *= detector.getScaleFactor();
            myHigh *= detector.getScaleFactor();
            if (myWidth < MIN_WIDTH) {
                myWidth = myVideoView.getWidth();
                myHigh = myVideoView.getHeight();
            }
            Log.d("onScale", "scale=" + detector.getScaleFactor() + ", w=" + myWidth + ", h=" + myHigh);
            myVideoView.setFixedVideoSize(myWidth, myHigh);
            layoutParams.width = myWidth;
            layoutParams.height = myHigh;
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            myWidth = myVideoView.getWidth();
            myHigh = myVideoView.getHeight();
            Log.d("onScaleBegin", "scale=" + detector.getScaleFactor() + ", w=" + myWidth + ", h=" + myHigh);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.d("onScaleEnd", "scale=" + detector.getScaleFactor() + ", w=" + myWidth + ", h=" + myHigh);
        }

    }
}
