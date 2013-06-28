package com.example.gobi;

import java.sql.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class Checklist extends GobiData {

	/*
	 * public references to SQL information, for use with DatabaseHander
	 */

	//A reference to the name used for the table in SQL
	public static final String TABLE = DatabaseHandler.TABLE_CHECKLIST;

	//A reference to the name used for the primary key in SQL
	public static final String PRIMARYKEY = DatabaseHandler.KEY_CHECKLISTID;

	@Override
	public String getTable() {
		return DatabaseHandler.TABLE_CHECKLIST;
	}

	@Override
	public String getPrimaryKey() {
		return DatabaseHandler.KEY_CHECKLISTID;
	}

	@Override
	public int getID() {
		return checklistID;
	}

	/*
	 * Class information
	 */
	//The class attributes
	private int checklistID;
	private String geolocation;
	private String note;
	private int workspaceID;
	private int projectID;
	private String checklistName;
	private int userID;
	private int dueTime;
	private boolean timeFlag;
	private int lastUpdated;
	private Date dueDate;
	private boolean status;
	private boolean priority;
	private String tag;

	/*
	 * Constructors
	 */

	public Checklist(int checklistID, String geolocation, String note,
			int workspaceID, int projectID, String checklistName, int userID,
			int dueTime, boolean timeFlag, int lastUpdated, Date dueDate,
			boolean status, boolean priority, String tag) {
		super();
		this.checklistID = checklistID;
		this.geolocation = geolocation;
		this.note = note;
		this.workspaceID = workspaceID;
		this.projectID = projectID;
		this.checklistName = checklistName;
		this.userID = userID;
		this.dueTime = dueTime;
		this.timeFlag = timeFlag;
		this.lastUpdated = lastUpdated;
		this.dueDate = dueDate;
		this.status = status;
		this.priority = priority;
		this.tag = tag;
	}

	/**
	 * Constructor with default values for booleans.
	 */
	public Checklist(int checklistID, String geolocation, String note,
			int workspaceID, int projectID, String checklistName, int userID,
			int dueTime, int lastUpdated, Date dueDate, String tag) {
		super();
		this.checklistID = checklistID;
		this.geolocation = geolocation;
		this.note = note;
		this.workspaceID = workspaceID;
		this.projectID = projectID;
		this.checklistName = checklistName;
		this.userID = userID;
		this.dueTime = dueTime;
		timeFlag = false;
		this.lastUpdated = lastUpdated;
		this.dueDate = dueDate;
		status = false;
		priority = false;
		this.tag = tag;
	}

	/**
	 * Constructor from Cursor data.
	 * @param cursor Cursor made from an appropriate SQL query
	 */
	public Checklist(Cursor cursor) {
		super();
		int i = 1;
		this.checklistID = Integer.parseInt(cursor.getString(i++));
		this.geolocation = cursor.getString(i++);
		this.note = cursor.getString(i++);
		this.workspaceID = Integer.parseInt(cursor.getString(i++));
		this.projectID = Integer.parseInt(cursor.getString(i++));
		this.checklistName = cursor.getString(i++);
		this.userID = Integer.parseInt(cursor.getString(i++));
		this.dueTime = Integer.parseInt(cursor.getString(i++));
		this.timeFlag = (cursor.getInt(i++) == 1);
		this.lastUpdated = Integer.parseInt(cursor.getString(i++));
		this.dueDate = Date.valueOf(cursor.getString(i++));
		this.status = (cursor.getInt(i++) == 1);
		this.priority = (cursor.getInt(i++) == 1);
		this.tag = cursor.getString(i++);
	}

	/*
	 * Getters and setters
	 */

	public int getChecklistID() {
		return checklistID;
	}

	public void setChecklistID(int checklistID) {
		this.checklistID = checklistID;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getWorkspaceID() {
		return workspaceID;
	}

	public void setWorkspaceID(int workspaceID) {
		this.workspaceID = workspaceID;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getChecklistName() {
		return checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getDueTime() {
		return dueTime;
	}

	public void setDueTime(int dueTime) {
		this.dueTime = dueTime;
	}

	public boolean isTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(boolean timeFlag) {
		this.timeFlag = timeFlag;
	}

	public int getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(int lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}


	/*
	 * ContentValues getter
	 */

	@Override
	public ContentValues getValues() {
		ContentValues values = new ContentValues();
		values.put(DatabaseHandler.KEY_CHECKLISTID, checklistID);
		values.put(DatabaseHandler.KEY_GEOLOCATION, geolocation);
		values.put(DatabaseHandler.KEY_NOTE, note);
		values.put(DatabaseHandler.KEY_WORKSPACEID, workspaceID);
		values.put(DatabaseHandler.KEY_PROJECTID, projectID);
		values.put(DatabaseHandler.KEY_CHECKLISTNAME, checklistName);
		values.put(DatabaseHandler.KEY_USERID, userID);
		values.put(DatabaseHandler.KEY_DUEDATE, dueDate.toString());
		values.put(DatabaseHandler.KEY_STATUS, status);
		values.put(DatabaseHandler.KEY_PRIORITY, priority);
		values.put(DatabaseHandler.KEY_TAG, tag);
		return values;
	}

}