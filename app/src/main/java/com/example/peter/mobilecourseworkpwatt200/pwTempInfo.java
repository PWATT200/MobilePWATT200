package com.example.peter.mobilecourseworkpwatt200;

import java.io.Serializable;

/**
 * Created by Peter on 12/12/2014.
 */
public class pwTempInfo implements Serializable {

    // *********************************************
    // Declare variables etc.
    // *********************************************
    private String tempID;
    private float maxTemp;
    private float avgTemp;
    private float minTemp;

    private static final long serialVersionUID = 0L;

    // *********************************************
    // Declare getters and setters etc.
    // *********************************************
    public String getTempID() {return tempID; };
    public void setTempID(String tempID) {this.tempID = tempID;};

    public float getMaxTemp() {return maxTemp;};
    public void setMaxTemp(float maxTemp){this.maxTemp = maxTemp;};

    public float getAvgTemp(){return avgTemp;};
    public void setAvgTemp(float avgTemp){this.avgTemp = avgTemp;};

    public float getMinTemp(){return minTemp;};
    public void setMinTemp(float minTemp){ this.minTemp = minTemp;};

    @Override
    public String toString(){
        String tempInfoData;
        tempInfoData = "pwTempInfo [tempID=" + tempID;
        tempInfoData = ",maxTemp=" + maxTemp;
        tempInfoData = ",avgTemp=" + avgTemp;
        tempInfoData = ",minTemp=" + minTemp + "]";

        return tempInfoData;
    }

}
