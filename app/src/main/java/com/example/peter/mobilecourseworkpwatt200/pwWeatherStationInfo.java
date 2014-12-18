package com.example.peter.mobilecourseworkpwatt200;

import java.io.Serializable;

/**
 * Created by Peter on 12/12/2014.
 */
public class pwWeatherStationInfo implements Serializable {

    // *********************************************
    // Declare variables etc.
    // *********************************************
    private int stationID;
    private String stationName;
    private float stationLatitude;
    private float stationLongitude;
    private int stationElevation;

    private static final long serialVersionUID = 0L;

    // *********************************************
    // Declare getters and setters etc.
    // *********************************************
    public int getStationID() {return stationID; };
    public void setStationID(int stationID) {this.stationID = stationID;};

    public String getStationName() {return stationName;};
    public void setStationName(String stationName){this.stationName = stationName;};

    public float getStationLatitude(){return stationLatitude;};
    public void setStationLatitude(float stationLatitude){this.stationLatitude = stationLatitude;};

    public float getStationLongitude(){return stationLongitude;};
    public void setStationLongitude(float stationLongitude){ this.stationLongitude = stationLongitude;};

    public int getStationElevation(){return  stationElevation;};
    public void setStationElevation(int stationElevation) {this.stationElevation = stationElevation;};


    @Override
    public String toString(){
        String weatherStationData;
        weatherStationData = "pwWeatherStationInfo [stationID=" + stationID;
        weatherStationData = ",stationName=" + stationName;
        weatherStationData = ",stationLatitude=" + stationLatitude;
        weatherStationData = ",stationLongitude=" + stationLongitude;
        weatherStationData = ",stationElevation=" + stationElevation + "]";
        return weatherStationData;
    }

}
