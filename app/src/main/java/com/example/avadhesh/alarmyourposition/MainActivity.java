package com.example.avadhesh.alarmyourposition;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    ImageView mapBtn,mapShow,bookShow;
    int request_code = 1;
    private LocremainDBHelper remainDB;
    private String ReminCont="";
    private AlertDialog.Builder ab1;
    int id;
    private Bundle extras1;
    private NotificationManager nm;
    Intent itt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        extras1 = new Bundle();
//        extras1.putString("RemainContent","hello");
        if(remainDB == null)
            remainDB = new LocremainDBHelper(this);
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        itt= getIntent();
        Log.d("Intentvalue ","is "+itt);
        if(itt!=null) {
            id =itt.getIntExtra("RemainId",1);
            Log.d("RemainIDp ","is "+id);
            ReminCont = itt.getStringExtra("RemainContent");
            ab1 = new AlertDialog.Builder(this);
            ab1.setTitle("Your Remainder\n Click Ok to delete it");
            ab1.setMessage(ReminCont);
            MyListener1 ml1 = new MyListener1();
            ab1.setPositiveButton("Ok", ml1);
            if(ReminCont!=null)
                ab1.show();
        }
        mapBtn = (ImageView)findViewById(R.id.btnMap);
        mapShow = (ImageView)findViewById(R.id.btnShow);
        bookShow = (ImageView)findViewById(R.id.bookShow);
        mapBtn.setOnClickListener(this);
        mapShow.setOnClickListener(this);
        bookShow.setOnClickListener(this);

        Intent ser = new Intent(this, ServiceForLoc.class);
        startService(ser);

    }
    class MyListener1 implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(remainDB == null)
                remainDB = new LocremainDBHelper(getApplicationContext());
            Log.d("justafterclick", "clicked ");
            Cursor c=remainDB.retrieveRemainder(id);
            Log.d("Cursorretrieved","clicked "+c);
            Log.d("msg"," "+c.getInt(5));
            c.moveToFirst();
            Log.d("notificationidinmain", " " + c.getInt(5));
            nm.cancel(c.getInt(5));
            Log.d("notificationdeleted","deleted");
            Boolean bl = remainDB.deleteRemain(id);
            Log.d("success","retrieved "+bl);
                //Log.d("NotificationIDis"," "+itt.getIntExtra("NotificationID",1));
                //Toast.makeText(getBaseContext(),"1 Reminder has been deleted.",Toast.LENGTH_LONG);

            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.btnMap)) {
            Intent i = new Intent(this, GoogleeMap.class);
            startActivityForResult(i, request_code);
        }
        if(v == findViewById(R.id.btnShow)){
            Intent i=new Intent(this, AllRemainders.class);
            startActivity(i);
        }
        if(v == findViewById(R.id.bookShow)){
            Intent i = new Intent(this,AllBookmarks.class);
            startActivity(i);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        if(requestCode == request_code){
//            if(resultCode == RESULT_OK){
//                int id=Integer.parseInt(data.getData().toString());
//                Cursor c = remainDB.retrieveRemainders();
//                if(c.moveToNext()) {
//                    Intent myIntent=new Intent(this, ServiceForLoc.class);
//                    startService(myIntent);
////                    Toast.makeText(getBaseContext(), "remainderId " + c.getString(0) + "\n Address " + c.getString(1) + "\n flag is  " + c.getInt(2), Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getBaseContext(), "No reminder Record is found for this id "+id, Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }
}
