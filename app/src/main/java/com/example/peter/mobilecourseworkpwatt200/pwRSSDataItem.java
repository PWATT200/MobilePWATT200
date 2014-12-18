package com.example.peter.mobilecourseworkpwatt200;

import java.io.Serializable;

/**
 * Created by Peter on 12/12/2014.
 */
public class pwRSSDataItem implements Serializable{

    private String itemTitle;
    private String itemDesc;
    private String itemLink;

    public String getItemTitle(){ return itemTitle;};
    public void setItemTitle(String sitemTitle){ this.itemTitle = sitemTitle;};

    public String getItemDesc(){return itemDesc;};
    public void setItemDesc(String sitemDesc){ this.itemDesc = sitemDesc;};

    public String getItemLink() {return itemLink;};
    public void setItemLink(String sitemLink) { this.itemLink = sitemLink;};

    public pwRSSDataItem(){
        this.itemTitle = "";
        this.itemDesc = "";
        this.itemLink = "";
    }

    @Override
    public String toString(){
        String weatherRSSData;
        weatherRSSData = "pwRSSDataItem [itemTitle="+itemTitle;
        weatherRSSData = ", itemDesc="+ itemDesc;
        weatherRSSData = ", itemLink="+ itemLink+ "]";
        return weatherRSSData;
    }
}
