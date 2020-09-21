package com.example.avadhesh.alarmyourposition;

/**
 * Created by Avadhesh on 26-03-2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocremainDBHelper {
    public static final String RemainId="RemainId";
    public static final String lat="latitude";
    public static final String lon="longitude";
    public static final String addressText="addressText";
    public static final String RemainName="RemainName";
    public static final String RemainText="RemainText";
//    public static final String date="date";
    public static final String flag="flag";
    public static final String NotId="NotId";
//    public static final String ="latitude";
    private static final String databasename="RemainderDB";
    private static final String tablename="Remainders";
    private static final int databaseversion=1;
    private static final String create_table="create table Remainders (RemainId integer primary key autoincrement,"+"latitude real not null, longitude real not null, addressText text not null, RemainName text not null, RemainText text not null, flag integer, NotId integer);";
    private final Context ct;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public LocremainDBHelper(Context context){
        this.ct = context;
        dbHelper=new DatabaseHelper(ct);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context ct){
            super(ct,databasename,null,databaseversion);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.d("database object",""+db);
                db.execSQL(create_table);
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Remainders");
            onCreate(db);
        }
    }
//    Declaring the connect() method to connect to the database
    public LocremainDBHelper connect() throws SQLException{
        database = dbHelper.getWritableDatabase();
        return this;
    }
//    Declaring the disconnect() method to close the database
    public void disconnect(){
        dbHelper.close();
    }
//    Declaring the insertRemainder() method to add Remainder details into the database
    public long insertRemainder(String latitude,String longitude, String address, String name, String text){
        ContentValues cv = new ContentValues();
        Double latit=Double.parseDouble(latitude);
        Double longit=Double.parseDouble(longitude);
        cv.put(lat,latit);
        cv.put(lon, longit);
        cv.put(addressText,address);
        cv.put(RemainName, name);
        cv.put(RemainText, text);
        cv.put(flag, 0);
        cv.put(NotId,0);
        this.connect();
        Log.d("completecolumns",latitude+longitude+address+name+text+database);
        try {
            long id = database.insert(tablename, null, cv);
            Log.d("reminderinserted", "id has been created "+id);
            return id;
        }
        catch(Exception ex){
            Log.d("remindernotinserted", "id has not been created");
            ex.printStackTrace();
            return 0;
        }


    }
//    declaring the retrieveRemainder() method to retrieve the details of the remainder from the database
    public Cursor retrieveRemainder(long id) throws SQLException{
        this.connect();
        Cursor c = database.query(tablename,new String[]{RemainId, addressText, flag, lat, lon, NotId}, RemainId + "=" +id,null,null,null,null,null);
        if(c!=null){
            c.moveToFirst();
        }
        return c;
    }
//    Declaring the retrieveRemainders() method to retrieve the detals of all the remainders from the database
    public Cursor retrieveRemainders() throws SQLException{
        this.connect();
        Cursor c = database.query(tablename,new String[]{addressText, RemainText ,flag, lat, lon, RemainId, NotId},null,null,null,null,null);
        return c;
    }
    public int updateReminder(int id,int notid){
        this.connect();
        ContentValues cv = new ContentValues();
        cv.put(flag,1);
        cv.put(NotId,notid);
        return database.update(tablename,cv,RemainId+"=?",new String[]{""+id});
    }
    public boolean deleteRemain(long id){
        this.connect();
        return database.delete(tablename,RemainId+"="+id,null)>0;
    }

}


