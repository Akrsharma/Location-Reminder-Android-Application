package com.example.avadhesh.alarmyourposition;

/**
 * Created by Avadhesh on 10-04-2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Bookmarks {
    public static final String BookId="BookId";
    public static final String BookLat="latitude";
    public static final String Booklon="longitude";
    public static final String addressText="addressText";
    private static final String databasename="BookmarkDB";
    private static final String tablename="Bookmarks";
    private static final int databaseversion=1;
    private static final String create_table="create table Bookmarks (BookId integer primary key autoincrement, latitude real not null, longitude real not null, addressText text not null);";
    private final Context ct;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public Bookmarks(Context context){
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
    public Bookmarks connect() throws SQLException{
        database = dbHelper.getWritableDatabase();
        return this;
    }
    //    Declaring the disconnect() method to close the database
    public void disconnect(){
        dbHelper.close();
    }
    public long insertBookmark(String latitude,String longitude, String address){
        this.connect();
        Log.d("valueofc1"," fasfasdfas");
        Cursor c = database.query(tablename,new String[]{BookId},BookLat+"="+latitude+" AND "+Booklon+"="+longitude,null,null,null,null);
        Log.d("valueofc"," "+c);
        ContentValues cv = new ContentValues();
        Double latit=Double.parseDouble(latitude);
        Double longit=Double.parseDouble(longitude);
        cv.put(BookLat,latit);
        cv.put(Booklon,longit);
        cv.put(addressText, address);

        //  Log.d("completecolumns",latitude+longitude+address+name+text+database);

        if(!c.moveToFirst()) {
            try {
                long id = database.insert(tablename, null, cv);
                Log.d("reminderinserted", "id has been created " + id);
                return id;
            } catch (Exception ex) {
                Log.d("remindernotinserted", "id has not been created");
                ex.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
    //    Declaring the retrieveRemainders() method to retrieve the detals of all the remainders from the database
    public Cursor retrieveBookmarks() throws SQLException{
        Log.d("database1", "entered");
        this.connect();
        Log.d("database2", "after connection");
        Cursor c = database.query(tablename,new String[]{BookId,BookLat,Booklon,addressText},null,null,null,null,null);
        Log.d("database3", "exit");
        return c;
    }
    public boolean deleteRemain(long id){
        this.connect();
        return database.delete(tablename,BookId+"="+id,null)>0;
    }
}
