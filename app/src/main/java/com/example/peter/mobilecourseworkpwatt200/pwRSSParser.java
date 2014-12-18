package com.example.peter.mobilecourseworkpwatt200;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 12/12/2014.
 */
public class pwRSSParser {

    private List<pwRSSDataItem> RSSDataItemList = new ArrayList<pwRSSDataItem>();
    pwRSSDataItem RSSDataItem;

    public void setRSSDataItem(String sItemData){
        RSSDataItem.setItemTitle(sItemData);
        RSSDataItem.setItemDesc(sItemData);
        RSSDataItem.setItemLink(sItemData);
    }

    public List<pwRSSDataItem> getRSSDataItem(){ return this.RSSDataItemList;}

    public pwRSSParser(){
       this.RSSDataItem = new pwRSSDataItem();
        setRSSDataItem(null);
    }

    public void parseRSSDataItem(XmlPullParser theParser, int theEventType){
        try{
            while(theEventType != XmlPullParser.END_DOCUMENT) {
                //Found a start tag
                if (theEventType == XmlPullParser.START_TAG) {
                    //check which tag has been found
                    if (theParser.getName().equalsIgnoreCase("title")) {
                        //Now just get the associated text
                        String temp = theParser.nextText();
                        // store the data in class
                        RSSDataItem.setItemTitle(temp);
                    } else
                        //Check which tag we have
                        if (theParser.getName().equalsIgnoreCase("description")) {
                            String temp = theParser.nextText();
                            RSSDataItem.setItemDesc(temp);
                        } else if (theParser.getName().equalsIgnoreCase("link")) {
                            String temp = theParser.nextText();
                            RSSDataItem.setItemLink(temp);
                        } else if(theParser.getName().equalsIgnoreCase(("item"))){
                                   RSSDataItemList.add(RSSDataItem);
                                   RSSDataItem = new pwRSSDataItem();
                    }
                }
            // get he next event
            theEventType = theParser.next();
            }
        }
        catch (XmlPullParserException parserExp1){
            Log.e("MyTag", "Parsing error" + parserExp1.toString());
        }
        catch(IOException e){
            Log.e("MyTag", "IO error during parsing");
        }
        RSSDataItemList.add(RSSDataItem);
    }

    public void parseRSSData(String RSSItemsToParse) throws MalformedURLException{
        URL rssURL = new URL(RSSItemsToParse);
        InputStream rssInputStream;
        try{
            XmlPullParserFactory parserRSSfactory = XmlPullParserFactory.newInstance();
            parserRSSfactory.setNamespaceAware(true);
            XmlPullParser RSSxmlPP = parserRSSfactory.newPullParser();
            String xmlRSS = getStringFromInputStream(getInputStream(rssURL), "UTF-8");
            RSSxmlPP.setInput(new StringReader(xmlRSS));
            int eventType = RSSxmlPP.getEventType();

            parseRSSDataItem(RSSxmlPP,eventType);
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");
    }

    public InputStream getInputStream(URL url) throws IOException
    {
        return url.openConnection().getInputStream();
    }

    public static String getStringFromInputStream(InputStream stream , String charsetName) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, charsetName);
        StringWriter writer = new StringWriter();
        while(-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }

}
