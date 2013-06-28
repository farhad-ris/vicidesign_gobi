//package com.example.gobi;
//
//import java.sql.Date;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//
//public class Project extends GobiData {
//	
//	/*
//	 * public static Strings for easy access to SQL information
//	 */
//	
//	//SQL for creating the table
////	public static final String SQLSTATEMENT = "CREATE TABLE " + TABLE_PROJECT + "("
////     		+ KEY_PROJECTID + " TEXT PRIMARY KEY, "
////     		+ KEY_PROJECTNAME + " TEXT UNIQUE, "
////     		+ KEY_WORKSPACEID + " TEXT UNIQUE, "
////     		//+ KEY_PROJECTICON + " NONE, "
////     		+ KEY_USERID + " TEXT, "
////     		+ KEY_dueDate + " NUMERIC, "
////     		+ KEY_TIMEFLAG + " NUMERIC, "
////     		+ KEY_NOTE + " TEXT, "
////     		+ "FOREIGN KEY(" + KEY_USERID + ") REFERENCES " + TABLE_USER + "(" + KEY_USERID + "), "
////     		+ "FOREIGN KEY(" + KEY_WORKSPACEID + ") REFERENCES " + TABLE_WORKSPACE + "(" + KEY_WORKSPACEID + "))";
//	
//	//A reference to the name used for the table in SQL
//	public static final String TABLE = DatabaseHandler.TABLE_PROJECT;
//   
//	//A reference to the name used for the primary key in SQL
//	public static final String PRIMARYKEY = DatabaseHandler.KEY_PROJECTID;
//	
//	
////	@Override
////	public String getSQLStatement() {
////		return SQLSTATEMENT;
////	}
//	
//	@Override
//	public String getTable() {
//		return DatabaseHandler.TABLE_PROJECT;
//	}
//	
//	@Override
//	public String getPrimaryKey() {
//		return DatabaseHandler.KEY_PROJECTID;
//	}
//	
//	@Override
//	public int getID() {
//		return projectID;
//	}
//	
//	/*
//	 * Class information
//	 */
//	//The class attributes
//	private int projectID;
//	private String note;
//	private String projectName;
//	private int workspaceID;
//	//private projectIcon;
//	private int userID;
//	private Date dueDate;
//	private boolean timeFlag;
//	
//	/*
//	 * Constructors
//	 */
//	
//	
//	public Project(int projectID, String note, String projectName,
//			int workspaceID, int userID, Date dueDate, boolean timeFlag) {
//		super();
//		this.projectID = projectID;
//		this.note = note;
//		this.projectName = projectName;
//		this.workspaceID = workspaceID;
//		this.userID = userID;
//		this.dueDate = dueDate;
//		this.timeFlag = timeFlag;
//	}
//	
//	/**
//	 * Constructor from Cursor data.
//	 * @param cursor Cursor made from an appropriate SQL query
//	 */
//	public Project(Cursor cursor) {
//		this.projectID = Integer.parseInt(cursor.getString(0));
//		this.note = cursor.getString(1);
//		this.projectName = cursor.getString(1);
//		this.workspaceID = Integer.parseInt(cursor.getString(3));
//		this.userID = Integer.parseInt(cursor.getString(4));
//		this.dueDate = Date.valueOf(cursor.getString(5));
//		this.timeFlag = (cursor.getInt(9) == 1);
//	}
//	
//	/*
//	 * Getters and setters
//	 */
//	
//
//	public String getNote() {
//		return note;
//	}
//
//	public void setNote(String note) {
//		this.note = note;
//	}
//
//	public String getProjectName() {
//		return projectName;
//	}
//
//	public void setProjectName(String projectName) {
//		this.projectName = projectName;
//	}
//
//	public int getWorkspaceID() {
//		return workspaceID;
//	}
//
//	public void setWorkspaceID(int workspaceID) {
//		this.workspaceID = workspaceID;
//	}
//
//	public int getUserID() {
//		return userID;
//	}
//
//	public void setUserID(int userID) {
//		this.userID = userID;
//	}
//
//	public Date getdueDate() {
//		return dueDate;
//	}
//
//	public void setdueDate(Date dueDate) {
//		this.dueDate = dueDate;
//	}
//
//	public boolean isTimeFlag() {
//		return timeFlag;
//	}
//
//	public void setTimeFlag(boolean timeFlag) {
//		this.timeFlag = timeFlag;
//	}
//
//	public int getProjectID() {
//		return projectID;
//	}
//	
//	/*
//	 * ContentValues getter
//	 */
//	
//	@Override
//	public ContentValues getValues() {
//		ContentValues values = new ContentValues();
//		values.put(DatabaseHandler.KEY_PROJECTID, projectID);
//    	values.put(DatabaseHandler.KEY_NOTE, note);
//    	values.put(DatabaseHandler.KEY_PROJECTNAME,  projectName);
//    	values.put(DatabaseHandler.KEY_WORKSPACEID, workspaceID);
//    	values.put(DatabaseHandler.KEY_USERID, userID);
//    	values.put(DatabaseHandler.KEY_DUEDATE, dueDate.toString());
//    	values.put(DatabaseHandler.KEY_TIMEFLAG, timeFlag);
//		return values;
//	}
//
//
//}
