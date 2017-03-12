package com.siehuai.smartdrugbox.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.siehuai.smartdrugbox.R;
import com.siehuai.smartdrugbox.controller.AlarmService;

public class OffAlarmActivity extends AppCompatActivity {

    AlarmService mAlarmService;
    boolean mAlarmStatus = true;
    TextView mTextView;
    int mAlarmId;


//TODO : Pass alarmData object here and delete
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_alarm);

        mAlarmId = getIntent().getExtras().getInt("alarmId");

        mAlarmService = new AlarmService(OffAlarmActivity.this);
        mTextView = (TextView) findViewById(R.id.text_status);
        setStatusText();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cancel_alarm);
        setFloatingActionBtnClick(fab);
    }

    public void setStatusText() {
        String status;
        status = mAlarmStatus ? "On":"Off";
        mTextView.setText(status);
    }

    public void setFloatingActionBtnClick(FloatingActionButton fab){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarmStatus = !mAlarmStatus;
                setStatusText();
            }
        });
    }
}