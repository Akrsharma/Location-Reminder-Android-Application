package com.example.avadhesh.alarmyourposition;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AllBookmarks extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
    private Bookmarks bm;
    private LocremainDBHelper ldb;
    private AlertDialog.Builder ab,ab1;
    private EditText et;
    Double latitude;
    Double longitude;
    String addressText;
    long p;
    CheckBox forcheck;
    Map<Integer,Pair<Double,Double> > mapadd = new HashMap<Integer,Pair<Double,Double> >();
    Map<Integer,Integer> mapdel= new HashMap<Integer,Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bookmarks);
        Log.d("initial1", "Oncreate called");
        bm = new Bookmarks(this);
        ldb = new LocremainDBHelper(this);
        et = new EditText(this);
        ab = new AlertDialog.Builder(this);
        ab1 = new AlertDialog.Builder(this);
        Cursor c = bm.retrieveBookmarks();
        LinearLayout rootll = (LinearLayout)findViewById(R.id.rootll);
        //ScrollView scrollView = (ScrollView)findViewById(R.id.scrollview);
        int addid=1;
        int delid=5000;
//        Map<Integer,Pair<Double,Double> > mapadd = new HashMap<Integer,Pair<Double,Double> >();
//        Map<Integer,Integer> mapdel= new HashMap<Integer,Integer>();
        if(c.moveToNext()){
            do {
                Log.d("initial1", "has entered in loop");
                LinearLayout ll = new LinearLayout(this);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(8,8,8,8);
                TextView tv = new TextView(this);
                TextView tv1 = new TextView(this);
                CheckBox cb = new CheckBox(this);
                tv.setId(addid);
                tv.setTextColor(Color.BLACK);
                tv.setText(c.getString(3));
                tv.setWidth(550);
                tv.setOnClickListener(this);
                cb.setId(delid);
                mapadd.put(addid, Pair.create(c.getDouble(1), c.getDouble(2)));
                mapdel.put(delid, c.getInt(0));
                tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv.setTextSize(20);
                cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                cb.setOnCheckedChangeListener(this);
                ll.addView(tv);
                ll.addView(tv1);
                ll.addView(cb);
                ImageView divider = new ImageView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                lp.setMargins(10, 10, 10, 10);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(Color.BLACK);
                rootll.addView(ll);
                rootll.addView(divider);

            }while(c.moveToNext());


        }
        else{
            Toast.makeText(getApplicationContext(),"No Bookmarks found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Pair<Double,Double> p = mapadd.get(v.getId());
        latitude = p.first;
        longitude = p.second;
        addressText = ((TextView)v).getText().toString();
        ab.setTitle("Add a Reminder");
        ab.setMessage(((TextView)v).getText());
        ab.setView(et);
        MyListener ml=new MyListener();
        ab.setPositiveButton("Add", ml);
        ab.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("check1234","checkbox has been checked "+isChecked);
        if(isChecked==true){
            ab.setTitle("Do you want to delete this Bookmark? ");
            ab.setMessage("");
           // buttonView.setChecked(false);
            forcheck = (CheckBox)buttonView;
            MyListener1 ml1=new MyListener1();
            ab.setPositiveButton("YES", ml1);
            ab.setNegativeButton("NO", ml1);
            ab.show();
            p = mapdel.get(buttonView.getId());
            ((CheckBox)buttonView).setEnabled(false);
//            Boolean bl = bm.deleteRemain(p);
//            if(bl==true){
////                Toast.makeText(getApplicationContext(),"Bookmark has been deleted",Toast.LENGTH_SHORT);
//
//            }
        }
    }
    class MyListener1 implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            long id;
            try {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Boolean bl = bm.deleteRemain(p);
                        if (bl == true) {
                            Toast.makeText(getApplicationContext(), "Bookmark has been deleted", Toast.LENGTH_SHORT);
                        }
                        forcheck.setChecked(false);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        forcheck.setChecked(false);
                        break;
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            recreate();
        }
    }
    class MyListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            long id;
            try {
                id = ldb.insertRemainder("" + latitude, "" + longitude, addressText, "Home", et.getText().toString());
                System.out.println("id is "+id);
                Log.d("remainderreturn ","the id of reminder is "+id);
                Toast.makeText(getApplicationContext(), "Your Reminder has been saved successfully ", Toast.LENGTH_LONG).show();
                Intent data = new Intent();
                //data.setData(Uri.parse("" + id));
                setResult(RESULT_OK, data);
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}
