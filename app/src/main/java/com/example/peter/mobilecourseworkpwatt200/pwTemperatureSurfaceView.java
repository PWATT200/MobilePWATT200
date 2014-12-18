package com.example.peter.mobilecourseworkpwatt200;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Peter on 15/12/2014.
 */
public class pwTemperatureSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder shTempSurface;
    pwTempThread drawingThread = null;
    List<pwTempInfo> tempInfoList;

    public pwTemperatureSurfaceView(Context context, List<pwTempInfo> list){
        super(context);
        tempInfoList = list;
        shTempSurface = getHolder();
        shTempSurface.addCallback(this);
        drawingThread = new pwTempThread(getHolder(),this, tempInfoList);
        setFocusable(true);
    }

    public pwTempThread getThread(){ return drawingThread;}

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        drawingThread.setRunning(true);
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        drawingThread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        drawingThread.setRunning(false);
        while(retry){
            try{
                drawingThread.join();
                retry = false;
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
