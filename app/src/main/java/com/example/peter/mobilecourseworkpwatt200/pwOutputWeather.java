package com.example.peter.mobilecourseworkpwatt200;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Peter on 15/12/2014.
 */
public class pwOutputWeather extends MainActivity {

    TextView tvWeather;
    Button btnDone;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_output_weather);

        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        List<pwRSSDataItem> weatherData = new ArrayList<pwRSSDataItem>();
        String weatherTemp  = "";
        String RSSFeedURL = "http://open.live.bbc.co.uk/weather/feeds/en/2648579/3dayforecast.rss";
        pwAsyncRSSParser rssAsyncParse = new pwAsyncRSSParser(this,RSSFeedURL);
        try{
            weatherData = rssAsyncParse.execute("").get();
        } catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }

        tvWeather = (TextView) findViewById(R.id.tvWeather);
        tvWeather.setMovementMethod(new ScrollingMovementMethod());
        for(int i = 0; i < weatherData.size(); i++ ) {
            weatherTemp = weatherTemp + weatherData.get(i).getItemTitle() + "\n"+ "\n" + weatherData.get(i).getItemDesc() + "\n" +
                    " \n" + weatherData.get(i).getItemLink() + "\n" +
                    " \n";
        }

        tvWeather.setText(weatherTemp);
    }

    public void onClick(View view){
        setResult(Activity.RESULT_OK);
        finish();
    }
}
