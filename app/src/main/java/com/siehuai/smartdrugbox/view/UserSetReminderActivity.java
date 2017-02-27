package com.siehuai.smartdrugbox.view;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TimePicker;

import com.siehuai.smartdrugbox.R;
import com.siehuai.smartdrugbox.controller.CustomTimePickerDialogListener;
import com.siehuai.smartdrugbox.controller.PostsDatabaseHelper;
import com.siehuai.smartdrugbox.controller.ReminderListViewAdapter;
import com.siehuai.smartdrugbox.data.AlarmData;
import com.siehuai.smartdrugbox.data.AlarmDataList;
import com.siehuai.smartdrugbox.data.MyTime;

public class UserSetReminderActivity extends AppCompatActivity {

    FloatingActionButton mFloatingActionButton;
    ReminderListViewAdapter reminderListViewAdapter;
    AlarmDialog mAlarmDialog;
    CustomTimePickerDialogListener mCustomTimePickerDialogListener;
    ExpandableListView mExpandableListView;
    PostsDatabaseHelper postsDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_set_reminder);

        postsDbHelper = PostsDatabaseHelper.getInstance(this);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add_alarm);

        mExpandableListView = (ExpandableListView) findViewById(R.id.expandList_reminder);

        reminderListViewAdapter = new ReminderListViewAdapter(this, mExpandableListView, AlarmDataList.mAlarmDataList);

        mExpandableListView.setAdapter(reminderListViewAdapter);

        mCustomTimePickerDialogListener = new CustomTimePickerDialogListener();

        setFloatingActionButton();

    }

    public void setFloatingActionButton() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarmDialog = new AlarmDialog(UserSetReminderActivity.this, mCustomTimePickerDialogListener, 5, 20, true);

                mAlarmDialog.show();

                setDialogBtn(mAlarmDialog);
            }
        });
    }

    @TargetApi(23)
    protected void setDialogOkButton(Button okBtn) {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker mTimePicker = mAlarmDialog.getTimePicker();
                MyTime myTime = new MyTime(mTimePicker.getHour(), mTimePicker.getMinute());
                AlarmData alarmData = new AlarmData(myTime, 0, -1);
                addOrUpdateAlarm(alarmData);
                mAlarmDialog.dismiss();
            }
        });
    }

    public void addOrUpdateAlarm(AlarmData alarmData) {
        boolean result;
        int resultNum = postsDbHelper.addOrUpdateAlarmFrmDb(alarmData);
        result = (resultNum > 0) ? true : false;
        postsDbHelper.addOrUpdateAlarmLocal(alarmData, result);
        //TODO: Think of a way to design that the reminderAdapter is not inside the database helper
        postsDbHelper.notifyAdapterDataChange(reminderListViewAdapter);
    }

    protected void setDialogCancelBtn(Button cancelBtn) {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarmDialog.dismiss();
            }
        });
    }

    protected void setDialogBtn(AlarmDialog alarmDialog) {
        setDialogOkButton(alarmDialog.getOkBtn());
        setDialogCancelBtn(alarmDialog.getCancelButton());
    }

}
