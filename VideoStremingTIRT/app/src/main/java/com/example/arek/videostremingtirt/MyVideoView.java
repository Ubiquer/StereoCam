package com.example.arek.videostremingtirt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Arek on 2016-05-14.
 */
public class MyVideoView extends VideoView {

    public MyVideoView(Context context){

        super(context);

    }

    public MyVideoView(Context context, AttributeSet attributeSet){

        super(context, attributeSet);

    }

    public MyVideoView(Context context, AttributeSet attributeSet, int defStyle){

        super(context, attributeSet, defStyle);

    }

    public void setFixedVideoSize(int width, int height)
    {
        getHolder().setFixedSize(width, height);
    }

}