package com.example.peter.mobilecourseworkpwatt200;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Peter on 15/12/2014.
 */
public class pwAsyncRSSParser extends AsyncTask<String, Integer, List<pwRSSDataItem>> {
    private Context appContext;
    private String urlRSSToParse;
    private List<pwRSSDataItem> data;

    public pwAsyncRSSParser(Context currentAppContext, String urlRSS){
        appContext = currentAppContext;
        urlRSSToParse = urlRSS;
    }

    @Override
    protected void onPreExecute(){
        Toast.makeText(appContext, "Parsing started! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected List<pwRSSDataItem> doInBackground(String...params){
        List<pwRSSDataItem> parsedData;
        pwRSSParser rssParser = new pwRSSParser();
        try{
            rssParser.parseRSSData(urlRSSToParse);
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        parsedData = rssParser.getRSSDataItem();

        return parsedData;
    }

    @Override
    protected void onPostExecute(List<pwRSSDataItem> result){
        Toast.makeText(appContext,"Parsing Finished !", Toast.LENGTH_SHORT).show();
    }
}
