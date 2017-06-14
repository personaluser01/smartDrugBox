package com.siehuai.smartdrugbox.Generic.controller.Service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.siehuai.smartdrugbox.User.controller.AlarmReceiver;
import com.siehuai.smartdrugbox.User.data.AlarmData;

import java.util.Calendar;


public class SetAlarmService {

    private AlarmManager mAlarmManager;
    private Context mContext;
    private Intent mIntent;

    public SetAlarmService(Context context) {
        mContext = context;
        mIntent = new Intent(this.mContext, AlarmReceiver.class);
        mAlarmManager = (AlarmManager) this.mContext.getSystemService(Context.ALARM_SERVICE);
    }

    @TargetApi(19)
    public void setAlarmOn(AlarmData alarmData) {
        Calendar calendar = Calendar.getInstance();

        int hour = alarmData.getHour();
        int minute = alarmData.getMinute();
        int alarmId = (int) alarmData.getAlarmID();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        //Get the current time
        Calendar now = Calendar.getInstance();

        //Pass in the state of the request, yes for activate alarm
        mIntent.putExtra("extra", "yes");
        mIntent.putExtra("alarmId", alarmId);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, alarmId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.getTimeInMillis() < now.getTimeInMillis()) {
            mAlarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY,
                    mPendingIntent);
        } else {
            mAlarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    mPendingIntent);
        }
    }

    public void cancelAlarm(AlarmData alarmData) {

        int alarmId = (int) alarmData.getAlarmID();

        turnOffAlarmMusic();

        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, alarmId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (mPendingIntent == null) {
            Log.d("UserReminder", "This pendingIntent is null");
        }

        mAlarmManager.cancel(mPendingIntent);
    }

    public void turnOffAlarmMusic() {
        mIntent.putExtra("extra", "no");
        mContext.sendBroadcast(mIntent);
    }


}
