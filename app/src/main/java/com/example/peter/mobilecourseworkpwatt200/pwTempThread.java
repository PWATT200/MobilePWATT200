package com.example.peter.mobilecourseworkpwatt200;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 15/12/2014.
 */
public class pwTempThread extends Thread {

    private int canvasWidth;
    private int canvasHeight;
    private float xPos = 0.0f;
    private float yPos = 0.0f;
    private int i;

    private float HalfAppletHeight;
    private float HalfAppletWidth;

    private boolean first = true;
    private boolean run = false;

    private int dTemp = 0;


    private SurfaceHolder shTempSurface;
    private Paint paintTemp;
    Paint textTemp = new Paint();


    private pwTemperatureSurfaceView tempSF;
    List<pwTempInfo> tempInfoList;

    public pwTempThread(SurfaceHolder surfaceHolder, pwTemperatureSurfaceView tempSurfV, List<pwTempInfo> list){
        this.shTempSurface = surfaceHolder;
        this.tempSF = tempSurfV;
        this.tempInfoList = list;
        paintTemp = new Paint();
    }

    public void doStart(){
        synchronized (shTempSurface){

        }
    }

    public void run(){
        while(run){
            Canvas c = null;
            try{
                c = shTempSurface.lockCanvas(null);
                synchronized (shTempSurface){
                    svDraw(c);
                }
            } finally {
                if(c != null){
                    shTempSurface.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setRunning(boolean b){ run = b;}

    public void setSurfaceSize(int width, int height){
        synchronized (shTempSurface){
            canvasHeight = height;
            canvasWidth = width;
            HalfAppletHeight = canvasHeight/2;
            HalfAppletWidth = canvasWidth/tempInfoList.size();
            doStart();
        }
    }

    private void svDraw(Canvas canvas){
        if(run){
            canvas.save();
            canvas.restore();
            canvas.drawColor(Color.WHITE);
            paintTemp.setStyle(Paint.Style.FILL);
            drawAxes(canvas);
            paintTemp.setColor(Color.RED);
            drawWave(canvas,1);
            paintTemp.setColor(Color.GREEN);
            drawWave(canvas,2);
            paintTemp.setColor(Color.BLUE);
            drawWave(canvas,3);
        }
    }

    public void drawWave(Canvas theCanvas, int type){
        float xPosOld = 1.0f;
        float yPosOld = 0.0f;
        textTemp.setColor(Color.BLACK);
        textTemp.setTextSize(20);


        for(i = 0; i < tempInfoList.size(); i++) {

            xPos = i * HalfAppletWidth;
            switch (type) {
                case 1:
                    yPos = -(canvasHeight / 40 * tempInfoList.get(i).getMaxTemp());
                    theCanvas.drawText(tempInfoList.get(i).getTempID(), xPos, yPos + canvasHeight/100 * 90, textTemp);
                    theCanvas.drawText(Float.toString(tempInfoList.get(i).getMaxTemp()), xPos, yPos+ HalfAppletHeight, textTemp);
                    break;
                case 2:
                    yPos = -(canvasHeight / 40 * tempInfoList.get(i).getAvgTemp());
                    theCanvas.drawText(Float.toString(tempInfoList.get(i).getAvgTemp()), xPos, yPos+ HalfAppletHeight, textTemp);
                    break;
                case 3:
                    yPos = -(canvasHeight / 40 * tempInfoList.get(i).getMinTemp());
                    theCanvas.drawText(Float.toString(tempInfoList.get(i).getMinTemp()), xPos, yPos+ HalfAppletHeight, textTemp);
                    break;
            }

            if (i == 0){
                paintTemp.setStyle(Paint.Style.FILL);
            }else {
                theCanvas.drawLine(xPosOld, (yPosOld + HalfAppletHeight), xPos, (yPos + HalfAppletHeight), paintTemp);

            }


            xPosOld = xPos;
            yPosOld = yPos;
         }
    }

    public void drawAxes(Canvas theCanvas){
        paintTemp.setColor(Color.BLACK);
        textTemp.setColor(Color.BLACK);
        textTemp.setTextSize(20);
        theCanvas.drawLine(0,HalfAppletHeight,tempInfoList.size()*HalfAppletWidth,HalfAppletHeight, paintTemp); // Horizontal X Axes
        theCanvas.drawLine((tempInfoList.size()/2)* HalfAppletWidth,0,(tempInfoList.size()/2)* HalfAppletWidth,canvasHeight, paintTemp); // Vertical Y Axes
        theCanvas.drawText("°C",(tempInfoList.size()/2)* HalfAppletWidth, canvasHeight/100 * 10, textTemp);
        theCanvas.drawText("Day (YYYY/mm/DD)",   canvasWidth/100 * 90, HalfAppletHeight, textTemp);
    }
 }
