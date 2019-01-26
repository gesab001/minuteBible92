package com.example.minutebible9;

import android.database.Cursor;
import android.os.CountDownTimer;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.resisthedevil5947.minutebible9.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    String DB_NAME = "kjv.db"; //an sqlite database that contains the whole bible
    String TABLE_NAME = "BIBLE4";
    DatabaseHelper myDBHelper;
    ScrollView scrollView;
    TextView result;
    TextView timer;
//    ProgressBar progressBar;

    private int getSeconds(){
        String date = new SimpleDateFormat("E, MMM, dd, yyyy h:mm:ss a", Locale.getDefault()).format(new Date());
        String seconds = new SimpleDateFormat("ss", Locale.getDefault()).format(new Date());
        //int secondsInt= 60 - Integer.parseInt(seconds);
        int secondsInt = Integer.parseInt(seconds);
        return secondsInt;

    }
    private String setCurrentID(){
        Long currentID;
        Date date = new Date();
        long time = date.getTime();
        java.sql.Timestamp startDate = java.sql.Timestamp.valueOf("2018-06-23 14:45:00.0");
        Timestamp current = new Timestamp(date.getTime());
        long then = startDate.getTime();
        long now = current.getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now - then)+1;
        currentID = minutes;
        while (currentID > 31102){
            currentID = currentID - 31102;
        }
        return Long.toString(currentID);
    }

    private void getVerse(){
        StringBuffer buffer = new StringBuffer();
        Cursor res = myDBHelper.getAVerse(TABLE_NAME, Integer.parseInt(setCurrentID()));
        if (res.getCount() == 0) {

            result.setText("No Data found, sorry!");
        } else {

            // res.moveToPosition(resultStartingIndex);
            while (res.moveToNext()) {
                buffer.append(res.getString(4) + "\n"); //text
                buffer.append("(" + res.getString(1)); //book
                buffer.append(res.getString(2) + ":"); //chapter
                buffer.append(res.getString(3) + ")\n\n"); //verse
            }
        }
        result.setText(buffer);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetDatabaseOpenHelper assetDatabaseOpenHelper = new AssetDatabaseOpenHelper(this, DB_NAME);
        assetDatabaseOpenHelper.copyDatabaseFromAssets(this, DB_NAME, false);
        myDBHelper = new DatabaseHelper(this, DB_NAME);

        result = (TextView) findViewById(R.id.resultView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        timer = (TextView) findViewById(R.id.timerTextView);

        TextViewCompat.setAutoSizeTextTypeWithDefaults(result, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

//        progressBar = (ProgressBar) findViewById(R.id.progressBar_id);

        getVerse();

        //counts the time in seconds and is displayed in the form of a progress bar
        final CountDownTimer timerCountdown = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timerSecondsRemaining = Math.round(millisUntilFinished/1000);

                String date = new SimpleDateFormat("E, MMM, dd, yyyy h:mm:ss a", Locale.getDefault()).format(new Date());
                String seconds = new SimpleDateFormat("ss", Locale.getDefault()).format(new Date());
                int secondsLeft = 60 - Integer.parseInt(seconds);
//                int progress = (int) (millisUntilFinished/100);
                timer.setText(Integer.toString(secondsLeft));
//                progressBar.incrementProgressBy(1);

                if (secondsLeft==60){
//                    progressBar.setProgress(0);
                    //the bible verse changes after each minute
                    getVerse();
                }

            }

            @Override
            public void onFinish() {
                start();


            }
        }.start();

    }
}
