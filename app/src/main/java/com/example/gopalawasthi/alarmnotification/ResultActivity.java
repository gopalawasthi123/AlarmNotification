package com.example.gopalawasthi.alarmnotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textView = findViewById(R.id.message);

        Intent intent = getIntent();
        String action = intent.getAction();
        if(action == Intent.ACTION_SEND){
           String a = intent.getStringExtra(Intent.EXTRA_TEXT);
            textView.setText(a);
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }


}

