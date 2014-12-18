package com.example.peter.mobilecourseworkpwatt200;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 12/12/2014.
 */
public class pwMapActivity extends FragmentActivity {

    List<pwWeatherStationInfo> mapStationList;
    private ArrayList<Marker> mapDataMarkerList = new ArrayList<Marker>();
    private GoogleMap mapWeatherStation;
   // private float markerColours[] = {210.0f, 120.0f, 300.0f, 330.0f, 270.0f};
    private LatLng latlangGlasgowCenter = new LatLng(55.857730, -4.255161);

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pw_map_view);

        mapStationList = new ArrayList<pwWeatherStationInfo>();
        pwWeatherStationDBMgr mapDB = new pwWeatherStationDBMgr(this,"weatherData.s3db",null,1);
        try{
            mapDB.dbCreate();
        }catch (IOException e){
            e.printStackTrace();
        }
        mapStationList = mapDB.allWeatherStation();
        SetUpMap();
        AddMarkers();
    }

    public void SetUpMap(){
        mapWeatherStation = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if(mapWeatherStation != null){
            mapWeatherStation.moveCamera(CameraUpdateFactory.newLatLngZoom(latlangGlasgowCenter,12));
            mapWeatherStation.setMyLocationEnabled(true);
            mapWeatherStation.getUiSettings().setCompassEnabled(true);
            mapWeatherStation.getUiSettings().setMyLocationButtonEnabled(true);
            mapWeatherStation.getUiSettings().setRotateGesturesEnabled(true);
        }
    }

    public void AddMarkers(){
        MarkerOptions marker;
        pwWeatherStationInfo mapData;
        String mrkTitle;
        String mrkText;
        int colour = 0;

        for (int i = 0; i < mapStationList.size(); i++) {
            mapData = mapStationList.get(i);
            mrkTitle = mapData.getStationName() + " Elevation: " + mapData.getStationElevation();
            mrkText = "Latitude " + mapData.getStationLatitude() + " Longitude: " + mapData.getStationLongitude();

           /*    This was  used to cycle a different colour for each marker based on the 5 available
            colour = i;

            while (colour > 4){
                colour = colour % 4;
            }*/

            marker = SetMaker(mrkTitle,mrkText, new LatLng(mapData.getStationLatitude(), mapData.getStationLongitude()), 210.0f ,true);
            mapDataMarkerList.add(i, mapWeatherStation.addMarker(marker));
        }
    }

    public MarkerOptions SetMaker(String title, String snippet, LatLng position, float markerColour, boolean centerAnchor){
        float anchorX;
        float anchorY;

        if(centerAnchor){
            anchorX = 0.5f;
            anchorY = 0.5f;
        }else {
            anchorX = 0.5f;
            anchorY = 1f;
        }


        MarkerOptions marker = new MarkerOptions().title(title).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(markerColour)).anchor(anchorX,anchorY).position(position);

        return marker;
    }

}
