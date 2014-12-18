package com.example.peter.mobilecourseworkpwatt200;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 15/12/2014.
 */
public class pwTemperatureActivity extends Activity {
    List<pwTempInfo> tempInfoList;

    public void setTempInfoList(ArrayList<pwTempInfo> thisList){  tempInfoList = thisList; }
    public List<pwTempInfo> getTempInfoList(){ return tempInfoList; }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_temp_draw_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tempInfoList = new ArrayList<pwTempInfo>();

        pwTempDBMgr tempDB = new pwTempDBMgr(this,"glasgowTemperature.s3db", null,1);
        try{
            tempDB.dbCreate();
        } catch (IOException e){
            e.printStackTrace();
        }
        tempInfoList = tempDB.allTempInfo();

        setContentView(new pwTemperatureSurfaceView(this, tempInfoList));
    }
}
