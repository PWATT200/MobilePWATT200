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
public class pwTempDBMgr extends SQLiteOpenHelper {

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/com.example.peter.mobilecourseworkpwatt200/databases/";
    private static final String DB_NAME = "glasgowTemperature.s3db";
    private static final String TBL_GLASGOWTEMPERATURE = "glasgowTemperature";

    public static final String COL_TEMPID = "id";
    public static final String COL_TEMPMAX = "Max";
    public static final String COL_TEMPAVG = "Avg";
    public static final String COL_TEMPMIN = "Min";

    private final Context appContext;

    public pwTempDBMgr(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TEMP_TABLE = "CREATE TABLE IF NOT EXISTS " + TBL_GLASGOWTEMPERATURE + "(" + COL_TEMPID + "INTEGER PRIMARY KEY," + COL_TEMPMAX + " TEXT," + COL_TEMPAVG + " TEXT," + COL_TEMPMIN + " TEXT" + ")";
        db.execSQL(CREATE_TEMP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TBL_GLASGOWTEMPERATURE);
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

    public void addTempInfo(pwTempInfo aTempInfo){

        ContentValues values = new ContentValues();
        values.put(COL_TEMPID, aTempInfo.getTempID());
        values.put(COL_TEMPMAX, aTempInfo.getMaxTemp());
        values.put(COL_TEMPAVG, aTempInfo.getAvgTemp());
        values.put(COL_TEMPMIN, aTempInfo.getMinTemp());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_GLASGOWTEMPERATURE, null, values);
        db.close();
    }

    public pwTempInfo findWeatherStation(String aTempInfo){
        String query = "SELECT * FROM " + TBL_GLASGOWTEMPERATURE + " WHERE " + COL_TEMPID +" = \"" + aTempInfo + "\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        pwTempInfo TempInfo = new pwTempInfo();

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            TempInfo.setTempID(cursor.getString(0));
            TempInfo.setMaxTemp(Float.parseFloat(cursor.getString(1)));
            TempInfo.setAvgTemp(Float.parseFloat(cursor.getString(2)));
            TempInfo.setMinTemp(Float.parseFloat(cursor.getString(3)));

            cursor.close();
        } else {
            TempInfo = null;
        }
        db.close();
        return TempInfo;
    }

    public boolean removeWeatherStation(String aTempInfo){
        boolean result = false;
        String query = "SELECT * FROM " + TBL_GLASGOWTEMPERATURE + " WHERE " + COL_TEMPID + " = \"" + aTempInfo +"\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        pwTempInfo TempInfo = new pwTempInfo();

        if(cursor.moveToFirst()){
            TempInfo.setTempID(cursor.getString(0));
            db.delete(TBL_GLASGOWTEMPERATURE,COL_TEMPID + " = ?", new String[] { String.valueOf(TempInfo.getTempID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public List<pwTempInfo> allTempInfo(){
        String query = "SELECT * FROM " + TBL_GLASGOWTEMPERATURE;
        List<pwTempInfo> pwTempInfosList = new ArrayList<pwTempInfo>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            while(cursor.isAfterLast() == false){
                pwTempInfo TempInfoEntry = new pwTempInfo();
                TempInfoEntry.setTempID(cursor.getString(0));
                TempInfoEntry.setMaxTemp(Float.parseFloat(cursor.getString(1)));
                TempInfoEntry.setAvgTemp(Float.parseFloat(cursor.getString(2)));
                TempInfoEntry.setMinTemp(Float.parseFloat(cursor.getString(3)));

                pwTempInfosList.add(TempInfoEntry);
                cursor.moveToNext();
            }
        } else {
            pwTempInfosList.add(null);
        }
        db.close();
        return pwTempInfosList;
    }

}
