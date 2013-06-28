package com.example.gobi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String message = (String) extras.get("Message");
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	//http://developer.android.com/reference/android/app/AlarmManager.html

	/**
	 * Sets an alarm.
	 * @param id A unique identifier corresponding to the alarm in question.
	 * @param message The message to be displayed when the alarm triggers.
	 */
	public static void setAlarm(Context context, int id, String message, long alarmTime) {
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(context, Alarm.class);
		Bundle b = new Bundle();
		b.putString("Message", message); //TODO prefix for key 
		i.putExtras(b);

		PendingIntent pi = PendingIntent.getBroadcast(context, id, i, 0);

		am.set(AlarmManager.RTC, alarmTime, pi); //TODO: RTC or RTC_WAKEUP?
	}

	/**
	 * Cancels an alarm.
	 * @param id A unique identifier corresponding to the alarm in question.
	 */
	public static void cancelAlarm(Context context, int id) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}
