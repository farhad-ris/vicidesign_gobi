package com.example.gobi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

public class DueDateActivity extends Activity {

	private DatePicker datePicker1;
	private TimePicker timePicker1;
	private ImageButton editDoneButton;
	public static final int ACTIVITY_ADD = 1;
	public static final int ACTIVITY_EDIT = 2;
	private String dueDate;
	private String dueTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // to get rid of title bar
		// NEEDS TO BE SET BEFORE setContentView
		setContentView(R.layout.due_date);

		TextView duedateHeader = (TextView) findViewById(R.id.taskinfo_title);
		duedateHeader.setText(R.string.duedate_title_taskinfo);

		addListenerOnButton();
		/*addListenerOnSpinnerItemSelection();*/
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		int requestCode = extras.getInt("requestCode");

		if(requestCode == ACTIVITY_ADD) {
		}
		else if(requestCode == ACTIVITY_EDIT) {
			dueDate = extras.getString(DatabaseHandler.KEY_DUEDATE);
			int year = Integer.parseInt(dueDate.substring(0,4));
			int month = Integer.parseInt(dueDate.substring(5,7));
			int day = Integer.parseInt(dueDate.substring(8));

			//todo: set alarm from old choice, passed in

			datePicker1.updateDate(year, month-1, day);

			dueTime = extras.getString(DatabaseHandler.KEY_DUETIME);
			int hour = Integer.parseInt(dueTime.substring(0,2));
			int minute = Integer.parseInt(dueTime.substring(3,5));

			timePicker1.setCurrentHour(hour);
			timePicker1.setCurrentMinute(minute);
		}


	}

	/*public void addListenerOnSpinnerItemSelection() {
		reminder = (Spinner) findViewById(R.id.reminder);
		reminder.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	 */
	//get the selected dropdown list value
	public void addListenerOnButton() {

		datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
		datePicker1.setCalendarViewShown(false);
		timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
		//			reminder = (Spinner) findViewById(R.id.reminder);
		//			location = (Spinner) findViewById(R.id.location);

		editDoneButton = (ImageButton) findViewById(R.id.editDoneButton);

		editDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = getIntent();

				//				    String chosenAlarmType = String.valueOf(reminder.getSelectedItem());
				//				    String[] alarmTypes = getResources().getStringArray(R.array.alarm_arrays);
				//				    
				//				    Log.v("ALARM", "ALARM type: " + chosenAlarmType);
				//				    
				//					if (chosenAlarmType.equals(alarmTypes[0])) {
				//						i.putExtra("alarmSet", false);
				//					} else if (chosenAlarmType.equals(alarmTypes[1])) {
				//						i.putExtra("alarmSet", true);
				//						i.putExtra(DatabaseHandler.KEY_ALARM, 0);
				//					} else if (chosenAlarmType.equals(alarmTypes[2])) {
				//						i.putExtra("alarmSet", true);
				//						i.putExtra(DatabaseHandler.KEY_ALARM, 5);
				//					} else if (chosenAlarmType.equals(alarmTypes[3])) {
				//						i.putExtra("alarmSet", true);
				//						i.putExtra(DatabaseHandler.KEY_ALARM, 10);
				//					} else if (chosenAlarmType.equals(alarmTypes[4])) {
				//						i.putExtra("alarmSet", true);
				//						i.putExtra(DatabaseHandler.KEY_ALARM, 30);
				//					} else if (chosenAlarmType.equals(alarmTypes[5])) {
				//						i.putExtra("alarmSet", true);
				//						i.putExtra(DatabaseHandler.KEY_ALARM, 60);
				//					}

				i.putExtra("dueDateSet", true);
				i.putExtra("dueYear", datePicker1.getYear());
				i.putExtra("dueMonth", datePicker1.getMonth() + 1);
				i.putExtra("dueDay", datePicker1.getDayOfMonth());
				i.putExtra("dueHour", timePicker1.getCurrentHour());
				i.putExtra("dueMinute", timePicker1.getCurrentMinute());

				setResult(AddTaskActivity.ACTIVITY_DATE, i);
				finish();
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.due_date, menu);
		return true;
	}

}
