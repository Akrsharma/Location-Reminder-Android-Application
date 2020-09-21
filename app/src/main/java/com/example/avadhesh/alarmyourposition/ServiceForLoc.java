package com.example.avadhesh.alarmyourposition;

/**
 * Created by Avadhesh on 02-04-2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ServiceForLoc extends Service{
    private LocremainDBHelper remainDB;
    private LocationManager lm;
    private Location destLocation;
    private  NotificationManager nm;
    String context,provider;
    double lattitude, longitude;
    float distance1=0;
    private TextToSpeech tts;
    public void onCreate(){
        super.onCreate();
//        Log.d("Locservice4", "Service has been started4");
//        if(tts == null) {
//            Log.d("ininitoftts","TextToSpeech object created "+tts);
//            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                @Override
//                public void onInit(int status) {
//                    if (status != TextToSpeech.ERROR) {
//                        //Log.d("in init of tts","TextToSpeech object created");
//                        tts.setLanguage(Locale.UK);
//                    }
//                }
//            });
//        }
        if(remainDB==null)
            remainDB = new LocremainDBHelper(this);
    }
   private void start(){
       Thread thread = new Thread(){
         public void run(){
                 do {
                     if(remainDB==null)
                         remainDB = new LocremainDBHelper(getApplicationContext());
                     context=Context.LOCATION_SERVICE;
                     if(lm==null) {
                         lm = (LocationManager) getSystemService(context);
                     }
                     List<String> providers = lm.getProviders(true);
                     Location myLocation = null;
                     for (String provider : providers) {
                         Location l = lm.getLastKnownLocation(provider);
                         Log.d("last known l and p", " "+provider+ " "+l);
                         if (l == null) {
                             continue;
                         }
                         if (myLocation == null
                                 || l.getAccuracy() < myLocation.getAccuracy()) {
                             Log.d("last known l: %s", " "+l);
                             myLocation = l;
                         }
                     }
                     Log.d("Locservice4", "Service has been started4 "+myLocation);
                     Cursor c = remainDB.retrieveRemainders();
                     //float distance=Float.MAX_VALUE;
                     if(c.moveToNext()) {
                         do {
                             lattitude = c.getDouble(3);
                             longitude = c.getDouble(4);
                             if (destLocation == null)
                                 destLocation = new Location("");
                             destLocation.setLatitude(c.getDouble(3));
                             destLocation.setLongitude(c.getDouble(4));
                             Log.d("Locservice2", "Service has been started2 " + destLocation);
                             distance1 = myLocation.distanceTo(destLocation);
                             if (Math.abs(distance1) <= 1000f) {
                                 int id=generateRandom();
                                 String str = Context.NOTIFICATION_SERVICE;
                                 nm = (NotificationManager) getSystemService(str);
                                 NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                                 mBuilder.setSmallIcon(R.mipmap.logo);
                                 mBuilder.setContentTitle("Notification Alert, Click Me!");
                                 mBuilder.setContentText(c.getString(1));
                                 long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                                 mBuilder.setVibrate(pattern);
                                 Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                 mBuilder.setSound(alarmSound);
                                 Intent resultIntent = new Intent(ServiceForLoc.this, MainActivity.class);
                                 resultIntent.putExtra("RemainId", c.getInt(5));
                                 resultIntent.putExtra("RemainContent", c.getString(1));
                                 //resultIntent.putExtra("NotificationID", id);
                                 TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                 stackBuilder.addParentStack(MainActivity.class);
                                 stackBuilder.addNextIntent(resultIntent);
                                 PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                 mBuilder.setContentIntent(resultPendingIntent);
                                 Log.d("ininitoftts1", "TextToSpeech object created1 " + tts);
                                 //tts.speak("Approaching To " + c.getString(0) + ". Pleas view your reminder", TextToSpeech.QUEUE_FLUSH, null);
                                 if(c.getInt(2)==0){
                                     Log.d("notificationidinservice"," "+id);
                                     nm.notify(id, mBuilder.build());
                                     remainDB.updateReminder(c.getInt(5),id);

                                 }

                                 Log.d("initial", "notify done");
                             }
                         } while (c.moveToNext());
                     }
                     if(!c.moveToNext()){
                         c.moveToFirst();
                     }
                     try{
                         Thread.sleep(7000);
                     }
                     catch(InterruptedException ie){
                         ie.printStackTrace();
                     }
                 }while (true);
                 //Log.d("Locservice7", "Service has been started7 "+9999);
           }
       };
       thread.start();
    }

    public int generateRandom()
    {
        Random random=new Random();
        return random.nextInt(9999-1000)+1000;
    }
    public int onStartCommand(Intent intent, int flags, int startId){
//        if(tts == null) {
//            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//                @Override
//                public void onInit(int status) {
//                    if (status != TextToSpeech.ERROR) {
//                        tts.setLanguage(Locale.UK);
//                    }
//                }
//            });
//        }
        start();
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onDestroy(){
        //destroy the Service
//        if(tts!=null){
//            tts.stop();
//            tts.shutdown();
//        }
        super.onDestroy();
    }
}
