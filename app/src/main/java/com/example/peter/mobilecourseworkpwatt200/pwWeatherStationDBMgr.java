package com.example.peter.mobilecourseworkpwatt200;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Peter on 12/12/2014.
 */
public class pwWeatherStationDBMgr extends SQLiteOpenHelper {

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/com.example.peter.mobilecourseworkpwatt200/databases/";
    private static final String DB_NAME = "weatherData.s3db";
    private static final String TBL_WEATHERDATA = "weatherData";

    public static final String COL_WEATHERID = "ID";
    public static final String COL_WEATHERNAME = "Name";
    public static final String COL_WEATHERLAT = "Latitude";
    public static final String COL_WEATHERLONG = "Longitude";
    public static final String COL_WEATHERELEV = "Elevation";

    private final Context appContext;

    public pwWeatherStationDBMgr(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_WEATHER_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_WEATHERDATA + "(" + COL_WEATHERID + "INTEGER PRIMARY KEY," + COL_WEATHERNAME + " TEXT," + COL_WEATHERLAT + " TEXT," + COL_WEATHERLONG + " TEXT," + COL_WEATHERELEV + " TEXT" + ")";
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TBL_WEATHERDATA);
            onCreate(db);
        }
    }

    // ================================================================================
    // Creates a empty database on the system and rewrites it with your own database.
    // ================================================================================
    public void dbCreate() throws IOException{
        boolean dbExist = dbCheck();

        if(!dbExist){
            //By calling this method an empty database will be created into the default system path
            //of your application so we can overwrite that database with our database.
            this.getReadableDatabase();
            try{
                copyDBFromAssets();
            } catch (IOException e){
                throw new Error("Error copying database");
            }
        }
    }
    // ============================================================================================
    // Check if the database already exist to avoid re-copying the file each time you open the application.
    // @return true if it exists, false if it doesn't
    // ============================================================================================
    private boolean dbCheck(){
        SQLiteDatabase db = null;

        try{
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READONLY);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);
        } catch (SQLiteException e){
            Log.e("SQLHelper" , "Database not Found!");
        }

        if(db != null){
            db.close();
        }

        return db != null ? true : false;
    }
    // ============================================================================================
    // Copies your database from your local assets-folder to the just created empty database in the
    // system folder, from where it can be accessed and handled.
    // This is done by transfering bytestream.
    // ============================================================================================
    private void copyDBFromAssets() throws IOException{

        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;

        try{
            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while((length = dbInput.read(buffer)) > 0){
                dbOutput.write(buffer,0,length);
            }

            //Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch (IOException e){
            throw new Error("Problems copying DB!");
        }
    }

    public void addWeatherStation(pwWeatherStationInfo aWeatherStation){

        ContentValues values = new ContentValues();
        values.put(COL_WEATHERID, aWeatherStation.getStationID());
        values.put(COL_WEATHERNAME, aWeatherStation.getStationName());
        values.put(COL_WEATHERLAT, aWeatherStation.getStationLatitude());
        values.put(COL_WEATHERLONG, aWeatherStation.getStationLongitude());
        values.put(COL_WEATHERELEV, aWeatherStation.getStationElevation());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_WEATHERDATA, null, values);
        db.close();
    }

    public pwWeatherStationInfo findWeatherStation(String aWeatherStation){
        String query = "SELECT * FROM " + TBL_WEATHERDATA + " WHERE " + COL_WEATHERNAME +" = \"" + aWeatherStation + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        pwWeatherStationInfo WeatherStationInfo = new pwWeatherStationInfo();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            WeatherStationInfo.setStationID(Integer.parseInt(cursor.getString(0)));
            WeatherStationInfo.setStationName(cursor.getString(1));
            WeatherStationInfo.setStationLatitude(Float.parseFloat(cursor.getString(2)));
            WeatherStationInfo.setStationLongitude(Float.parseFloat(cursor.getString(3)));
            WeatherStationInfo.setStationElevation(Integer.parseInt(cursor.getString(4)));
            cursor.close();
        } else {
            WeatherStationInfo = null;
        }
        db.close();
        return WeatherStationInfo;
    }

    public boolean removeWeatherStation(String aWeatherStation){
        boolean result = false;
        String query = "SELECT * FROM " + TBL_WEATHERDATA + " WHERE " + COL_WEATHERID + " = \"" + aWeatherStation +"\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        pwWeatherStationInfo WeatherStationInfo = new pwWeatherStationInfo();

        if(cursor.moveToFirst()){
            WeatherStationInfo.setStationID(Integer.parseInt(cursor.getString(0)));
            db.delete(TBL_WEATHERDATA,COL_WEATHERID + " = ?", new String[] { String.valueOf(WeatherStationInfo.getStationID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public List<pwWeatherStationInfo> allWeatherStation(){
        String query = "SELECT * FROM " + TBL_WEATHERDATA;
        List<pwWeatherStationInfo> pwWeatherStationList = new ArrayList<pwWeatherStationInfo>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            while(cursor.isAfterLast() == false){
                pwWeatherStationInfo WeatherStationEntry = new pwWeatherStationInfo();
                WeatherStationEntry.setStationID(Integer.parseInt(cursor.getString(0)));
                WeatherStationEntry.setStationName(cursor.getString(1));
                WeatherStationEntry.setStationLatitude(Float.parseFloat(cursor.getString(2)));
                WeatherStationEntry.setStationLongitude(Float.parseFloat(cursor.getString(3)));
                WeatherStationEntry.setStationElevation(Integer.parseInt(cursor.getString(4)));
                pwWeatherStationList.add(WeatherStationEntry);
                cursor.moveToNext();
            }
        } else {
            pwWeatherStationList.add(null);
        }
        db.close();
        return pwWeatherStationList;
    }

}
