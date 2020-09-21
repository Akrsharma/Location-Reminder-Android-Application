package com.example.avadhesh.alarmyourposition;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.*;
import android.widget.Toast;
import android.content.Intent;

import org.w3c.dom.Text;

import static android.graphics.Typeface.BOLD_ITALIC;

public class AllRemainders extends Activity implements CompoundButton.OnCheckedChangeListener {

    private LocremainDBHelper remainDB;
    private TextView btndel;
    private CheckBox cb;
    private AlertDialog.Builder ab;
    int arr[][]=new int[100][2];
    private CheckBox cb1;
    long id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("onCreatecalled", "inside oncreate method");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_remainders);
        remainDB = new LocremainDBHelper(this);
        ab=new AlertDialog.Builder(this);
        Cursor c = remainDB.retrieveRemainders();
        LinearLayout remll = (LinearLayout)findViewById(R.id.remll);
        //ScrollView scrollview = (ScrollView)findViewById(R.id.scrollview);
        int tvid=1;
        int i=1,p=1,k=0,l=0;
        if(c.moveToFirst()){
            do{
                Log.d("entereddo", "We have entered in do-while block");
                LinearLayout ll = new LinearLayout(this);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(8,8,8,8);
                TextView tv = new TextView(this);
                TextView tv1 = new TextView(this);
                CheckBox cb = new CheckBox(this);
                tv.setTextColor(Color.BLACK);
                tv1.setTextColor(Color.BLACK);
                tv.setText(c.getString(3));
                tv.setWidth(350);
                tv1.setWidth(200);
                tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv.setTextSize(20);
                tv1.setTextSize(20);
                cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                cb.setOnCheckedChangeListener(this);
                tv.setText(c.getString(0));
                tv1.setText(c.getString(1));
                int tv1id=i+1;
                int tv2id=tv1id+1;
                int cb1id=tv2id+1;
                tv.setId(tv1id);
                tv1.setId(tv2id);
                cb.setId(cb1id);
                arr[k][0]=cb1id;
                arr[k][1]=c.getInt(5);
                k++;
//                tv1.setGravity(Gravity.CENTER);
//                tv2.setGravity(Gravity.CENTER);
//                tv3.setGravity(Gravity.CENTER);
                tv.setPadding(0,0,0,10);
                tv1.setPadding(0,0,0,2);
                ll.addView(tv);
                ll.addView(tv1);
                ll.addView(cb);
                remll.addView(ll);
                ImageView divider = new ImageView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                lp.setMargins(10, 10, 10, 10);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(Color.BLACK);
                remll.addView(divider);
                p=tv1id;
                i=cb1id;
            } while (c.moveToNext());
        }
        else{
            Toast.makeText(getBaseContext(),"No Reminders in the database", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            cb1=(CheckBox)buttonView;
            for(int i=0;i<100;i++){
                if(arr[i][0]==cb1.getId()){
                    id=arr[i][1];
                    break;
                }
            }
            ab.setTitle("Do you want to delete this reminder ? ");
            ab.setMessage("");
            MyListener ml=new MyListener();
            ab.setPositiveButton("YES", ml);
            ab.setNegativeButton("NO", ml);
            ab.show();
        }
    }
    class MyListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        boolean bl=remainDB.deleteRemain(id);
                        cb1.setChecked(false);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        cb1.setChecked(false);
                        break;
                }
                recreate();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }
}
