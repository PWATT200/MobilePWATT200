package com.example.peter.mobilecourseworkpwatt200;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 12/12/2014.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button goMap;
    Button goWeather;
    FragmentManager fmAboutDialogue;
    ImageView ivLogo;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        ivLogo = (ImageView) findViewById(R.id.imgWeatherLogo);
        String sImagePath = "drawable/weather_logo";
        Context appContext = getApplicationContext();
        int imgResId = appContext.getResources().getIdentifier(sImagePath, "drawable","com.example.peter.mobilecourseworkpwatt200");
        ivLogo.setImageResource(imgResId);

        goMap = (Button) findViewById(R.id.goTempChart);
        goMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent pwTempThread = new Intent(getApplicationContext(), pwTemperatureActivity.class);
                startActivity(pwTempThread);

            }
        });


        goWeather = (Button) findViewById(R.id.goWeather);
        goWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), pwOutputWeather.class));
            }
        });

        fmAboutDialogue = this.getFragmentManager();

    }

    @Override
    public void onClick(View view){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pw_menu, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.mMap:
                Intent pwMap = new Intent(this, pwMapActivity.class);
                this.startActivity(pwMap);
                return true;
            case R.id.mAbout:
                DialogFragment pwAboutDlg = new pwAboutDialogue();
                pwAboutDlg.show(fmAboutDialogue,"pw_About_Dlg");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
