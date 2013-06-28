package com.example.gobi;

import java.sql.Date;
import java.sql.Timestamp;

import android.content.ContentValues;
import android.database.Cursor;

public class Task extends GobiData {	
	/*
	 * public references to SQL information, for use with DatabaseHander
	 */

	//A reference to the name used for the table in SQL
	public static final String TABLE = DatabaseHandler.TABLE_TASK;

	//A reference to the name used for the primary key in SQL
	public static final String PRIMARYKEY = DatabaseHandler.KEY_TASKID;

	@Override
	public String getTable() {
		return DatabaseHandler.TABLE_TASK;
	}

	@Override
	public String getPrimaryKey() {
		return DatabaseHandler.KEY_TASKID;
	}

	@Override
	public int getID() {
		return userID;
	}

	/*
	 * Class information
	 */
	//The class attributes
	private int _id;
	private int taskID;
	private String taskName;
	private int workspaceID;
	private boolean priority;
	private int userID;
	//private Date dueDateTime;
	private Date dueDate;
	private String dueTime;
	private boolean timeFlag;
	private boolean status;
	private String geolocation;
	private String tag;
	private int projectID;
	private String taskNote;
	private Timestamp lastUpdated;

	/*
	 * Constructors
	 */

	//This constructor is auto-generated
	public Task(int _id, int taskID, String taskName, int workspaceID, boolean priority,
			int userID, Date dueDate, String dueTime, boolean timeFlag,
			boolean status, String geolocation, String tag, int projectID,
			String taskNote, Timestamp lastUpdated) {
		super();
		this._id = _id;
		this.taskID = taskID;
		this.taskName = taskName;
		this.workspaceID = workspaceID;
		this.priority = priority;
		this.userID = userID;
		this.dueDate = dueDate;
		this.dueTime = dueTime;
		this.timeFlag = timeFlag;
		this.status = status;
		this.geolocation = geolocation;
		this.tag = tag;
		this.projectID = projectID;
		this.taskNote = taskNote;
		this.lastUpdated = lastUpdated;
	}

	/**
	 * Constructor from Cursor data.
	 * @param cursor Cursor made from an appropriate SQL query
	 */
	public Task(Cursor cursor) {
		super();
		int i = 0;
		this._id = Integer.parseInt(cursor.getString(i++));
		this.taskID = cursor.getInt(i++);//make cursor.getInt(i++)
		this.taskName = cursor.getString(i++);
		this.workspaceID = Integer.parseInt(cursor.getString(i++));
		this.priority = (cursor.getInt(i++) == 1);
		this.userID = Integer.parseInt(cursor.getString(i++));
		this.dueDate = Date.valueOf(cursor.getString(i++));
		this.dueTime = cursor.getString(i++);
		this.timeFlag = (cursor.getInt(i++) == 1);
		this.status = (cursor.getInt(i++) == 1);
		this.geolocation = cursor.getString(i++);
		this.tag = cursor.getString(i++);
		this.projectID = Integer.parseInt(cursor.getString(i++));
		this.taskNote = cursor.getString(i++);
		this.lastUpdated = Timestamp.valueOf(cursor.getString(i++));
	}

	/*
	 * Getters and setters
	 */

	public int getTaskID() {
		return taskID;
	}

	public String getTaskName() {
		return taskName;
	}

	public int getWorkspaceID() {
		return workspaceID;
	}

	public boolean isPriority() {
		return priority;
	}

	public int getUserID() {
		return userID;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getDueTime() {
		return dueTime;
	}

	public boolean isTimeFlag() {
		return timeFlag;
	}

	public boolean isStatus() {
		return status;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public String getTag() {
		return tag;
	}

	public int getProjectID() {
		return projectID;
	}

	public String getTaskNote() {
		return taskNote;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setWorkspaceID(int workspaceID) {
		this.workspaceID = workspaceID;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
	}

	public void setTimeFlag(boolean timeFlag) {
		this.timeFlag = timeFlag;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public void setTaskNote(String taskNote) {
		this.taskNote = taskNote;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/*
	 * ContentValues getter
	 */

	@Override
	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_COLUMNID, _id);
		values.put(DatabaseHandler.KEY_TASKID, taskID);
		values.put(DatabaseHandler.KEY_TASKNAME, taskName);
		values.put(DatabaseHandler.KEY_WORKSPACEID, workspaceID);
		values.put(DatabaseHandler.KEY_PRIORITY, priority);
		values.put(DatabaseHandler.KEY_USERID,  userID);
		values.put(DatabaseHandler.KEY_DUEDATE, getDueDate().toString());
		values.put(DatabaseHandler.KEY_DUETIME, dueTime);
		values.put(DatabaseHandler.KEY_TIMEFLAG, timeFlag);
		values.put(DatabaseHandler.KEY_STATUS, status);
		values.put(DatabaseHandler.KEY_GEOLOCATION,  geolocation);
		values.put(DatabaseHandler.KEY_TAG,  tag);
		values.put(DatabaseHandler.KEY_PROJECTID, projectID);
		values.put(DatabaseHandler.KEY_NOTE, taskNote);
		values.put(DatabaseHandler.KEY_LASTUPDATED, lastUpdated.toString());

		return values;
	}

	@Override
	public String toString() {
		String result = "";
		result += "TaskID: " + getID() +
				" _ID: " + this._id +
				" TaskName: " + getTaskName() +
				" WorkspaceID: " + getWorkspaceID()+
				" DueTime: " + getDueTime()+
				" DueDate: " + getDueDate();
		return result;
	}


}