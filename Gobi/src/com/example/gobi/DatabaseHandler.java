package com.example.gobi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	//Table names
	public static final String SCRATCH = "scratch";

	public static final String TABLE_CHECKLIST = "Checklist";
	public static final String TABLE_CHECKLISTCOMMENT = "ChecklistComment";
	public static final String TABLE_CHECKLIST_ITEM = "ChecklistItem";
	public static final String TABLE_CHECKLISTTAG = "ChecklistTag";
	public static final String TABLE_PROJECT = "Project";
	public static final String TABLE_PROJECTCOMMENT = "ProjectComment";
	public static final String TABLE_SETTINGS = "Settings";
	public static final String TABLE_SHAREDCHECKLIST = "SharedChecklist";
	public static final String TABLE_SHAREDPROJECT = "SharedProject";
	public static final String TABLE_SHAREDWORKSPACE = "SharedWorkspace";
	public static final String TABLE_TASK = "Task";
	public static final String TABLE_TASKASSIGNEE = "TaskAssignee";
	public static final String TABLE_TASKCOMMENT = "TaskComment";
	public static final String TABLE_TASKTAG = "TaskTag";
	public static final String TABLE_USER = "User";
	public static final String TABLE_WORKSPACE = "Workspace";
	public static final String TABLE_LASTUPDATE = "Lastupdate";

	//scratch tables
	public static final String TABLE_SCRATCH_TASK = SCRATCH + TABLE_TASK;
	public static final String TABLE_SCRATCH_CHECKLIST = SCRATCH + TABLE_CHECKLIST;
	public static final String TABLE_SCRATCH_PROJECT = SCRATCH + TABLE_PROJECT;
	public static final String TABLE_SCRATCH_WORKSPACE = SCRATCH + TABLE_WORKSPACE;
	public static final String TABLE_SCRATCH_CHECKLIST_ITEM = SCRATCH + TABLE_CHECKLIST_ITEM;
	public static final String TABLE_SCRATCH_TASKASSIGNEE = SCRATCH + TABLE_TASKASSIGNEE;

	//Key names

	public static final String KEY_COLUMNID = "_id";

	public static final String KEY_ASSIGNEEID = "assigneeID";
	public static final String KEY_CREATORID = "creatorID";
	//public static final String KEY_AUDIO = "audio";
	public static final String KEY_AUTOHIDE = "autohide";
	public static final String KEY_CHECKLISTCOMMENTTEXT = "checklistCommentText";
	public static final String KEY_CHECKLISTID = "checklistID";
	public static final String KEY_CHECKLISTNAME = "checklistName";
	public static final String KEY_COMMENTID = "commentID";
	//public static final String KEY_DUEDATETIME = "dueDateTime";
	public static final String KEY_DUEDATE = "dueDate";
	public static final String KEY_DUETIME = "dueTime";
	public static final String KEY_ALARM = "alarm";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_GEOLOCATION = "geolocation";
	public static final String KEY_ITEMID = "itemID";
	public static final String KEY_ITEMTEXT = "itemText";
	// public static final String KEY_NOTE = "note";
	public static final String KEY_LASTUPDATED = "lastUpdated";
	public static final String KEY_PASSWORD = "password";
	//public static final String KEY_PICTURE = "picture";
	public static final String KEY_PHOTO = "photo";
	public static final String KEY_PRIORITY = "priority";
	public static final String KEY_PRIVACY = "privacy";
	public static final String KEY_PROJECTCOMMENTTEXT = "projectCommentText";
	public static final String KEY_PROJECTICON = "projectIcon";
	public static final String KEY_PROJECTID = "projectID";
	public static final String KEY_PROJECTNAME = "projectName";
	//public static final String KEY_PROJECTNOTE = "projectNote";
	public static final String KEY_SHAREID = "shareID";
	public static final String KEY_STATUS = "status";
	public static final String KEY_TAG = "tag";
	public static final String KEY_TAGID = "tagID";
	public static final String KEY_NOTE = "note";
	public static final String KEY_TAGTEXT = "tagText";
	//public static final String KEY_TASKAUDIO = "taskAudio";
	public static final String KEY_TASKCOMMENTTEXT = "taskCommentText";
	public static final String KEY_TASKID = "taskID";
	public static final String KEY_TASKNAME = "taskName";
	public static final String KEY_TIMEFLAG = "timeFlag";
	public static final String KEY_THEME = "theme";
	public static final String KEY_USEREMAIL = "userEmail";
	public static final String KEY_USERID = "userID";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_WORKSPACEICON = "workspaceIcon";
	public static final String KEY_WORKSPACEID = "workspaceID";
	public static final String KEY_WORKSPACENAME = "workspaceName";

	//The database instance - keep it static
	private static DatabaseHandler instance = null;

	//Database version
	private static final int DATABASE_VERSION = 1;

	//Database name
	private static final String DATABASE_NAME = "gobiDatabase";

	/**
	 * Specifically made private so that no-one can call it directly.
	 * Use getInstance instead to ensure a static instance.
	 * @param ctx the application context
	 */
	private DatabaseHandler(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Used to get instance of the static database.
	 * Call this instead of the constructor.
	 * @param ctx the application context
	 * @return instance instance of the database
	 */
	public static DatabaseHandler getInstance(Context ctx) {
		if (instance == null)
			instance = new DatabaseHandler(ctx.getApplicationContext());

		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//TODO: Add each database table here.



		db.execSQL("CREATE TABLE " + TABLE_TASK + "("
				+ KEY_TASKID + " INTEGER, "
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_TASKNAME + " TEXT, "
				+ KEY_WORKSPACEID + " INTEGER, "
				+ KEY_PRIORITY + " NUMERIC, "
				+ KEY_USERID + " TEXT, "
				+ KEY_DUEDATE + " NUMERIC, "
				+ KEY_DUETIME + " NUMERIC, "
				+ KEY_TIMEFLAG + " NUMERIC, "
				+ KEY_STATUS + " NUMERIC, "
				+ KEY_GEOLOCATION + " NONE, "
				+ KEY_TAG + " TEXT, "
				+ KEY_PROJECTID + " TEXT, "
				+ KEY_NOTE + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT, "
				+ KEY_ALARM + " INTEGER, "
				+ "PRIMARY KEY("+ KEY_TASKID+","+ KEY_COLUMNID +"), "
				+ "FOREIGN KEY(" + KEY_USERID + ") REFERENCES " + TABLE_USER + "(" + KEY_USERID + "), "
				+ "FOREIGN KEY(" + KEY_WORKSPACEID + ") REFERENCES " + TABLE_WORKSPACE + "(" + KEY_WORKSPACEID + "), "
				+ "FOREIGN KEY(" + KEY_PROJECTID + ") REFERENCES " + TABLE_PROJECT + "(" + KEY_PROJECTID + "))"
				);

		db.execSQL("CREATE TABLE " + SCRATCH + TABLE_TASK + "("
				+ KEY_COLUMNID + " INTEGER PRIMARY KEY, "
				+ KEY_TASKID + " INTEGER  DEFAULT "+1 + ", "
				+ KEY_TASKNAME + " TEXT, "
				+ KEY_WORKSPACEID + " INTEGER DEFAULT "+1 + ", "
				+ KEY_PRIORITY + " NUMERIC, "
				+ KEY_USERID + " TEXT, "
				+ KEY_DUEDATE + " NUMERIC DEFAULT CURRENT_DATE"+ ", "
				+ KEY_DUETIME + " NUMERIC DEFAULT CURRENT_TIME" + ", "
				+ KEY_TIMEFLAG + " NUMERIC DEFAULT "+0 + ", "
				+ KEY_STATUS + " NUMERIC DEFAULT "+0 + ", "
				+ KEY_GEOLOCATION + " NONE, "
				+ KEY_TAG + " TEXT, "
				+ KEY_PROJECTID + " TEXT DEFAULT "+0 + ", "
				+ KEY_NOTE + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_ALARM + " INTEGER DEFAULT "+ 0 +")" //radio value is relative to due time
				);

		//TODO: Check these types
		db.execSQL("CREATE TABLE " + TABLE_CHECKLIST + "("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_CHECKLISTID + " TEXT, "
				+ KEY_GEOLOCATION + " NONE, "
				+ KEY_NOTE + " NONE, "
				+ KEY_WORKSPACEID + " TEXT, "
				+ KEY_PROJECTID + " TEXT, "
				+ KEY_CHECKLISTNAME + " TEXT, "
				+ KEY_USERID + " TEXT, "
				+ KEY_DUETIME + " NUMERIC, "
				+ KEY_TIMEFLAG + " NUMERIC, "
				+ KEY_LASTUPDATED + " NUMERIC, "
				+ KEY_DUEDATE + " NUMERIC, "
				+ KEY_STATUS + " NUMERIC, "
				+ KEY_PRIORITY + " NUMERIC, "
				+ KEY_TAG + " TEXT, "
				+ "PRIMARY KEY("+ KEY_CHECKLISTID+","+ KEY_COLUMNID +"), "
				+ "FOREIGN KEY(" + KEY_USERID + ") REFERENCES " + TABLE_USER + "(" + KEY_USERID + "), "
				+ "FOREIGN KEY(" + KEY_WORKSPACEID + ") REFERENCES " + TABLE_WORKSPACE + "(" + KEY_WORKSPACEID + "), "
				+ "FOREIGN KEY(" + KEY_PROJECTID + ") REFERENCES " + TABLE_PROJECT + "(" + KEY_PROJECTID + "))");

		db.execSQL("CREATE TABLE " + SCRATCH + TABLE_CHECKLIST + "("
				+ KEY_COLUMNID + " INTEGER PRIMARY KEY, "
				+ KEY_CHECKLISTID + " TEXT DEFAULT 0, "
				+ KEY_GEOLOCATION + " NONE, "
				+ KEY_NOTE + " TEXT, "
				+ KEY_WORKSPACEID + " TEXT DEFAULT 1, "
				+ KEY_PROJECTID + " TEXT DEFAULT 1, "
				+ KEY_CHECKLISTNAME + " TEXT, "
				+ KEY_USERID + " TEXT DEFAULT 1, "
				+ KEY_DUETIME + " NUMERIC DEFAULT CURRENT_TIME, "
				+ KEY_TIMEFLAG + " NUMERIC DEFAULT 0, "
				+ KEY_LASTUPDATED + " NUMERIC DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_DUEDATE + " NUMERIC DEFAULT CURRENT_DATE, "
				+ KEY_STATUS + " NUMERIC DEFAULT 0, "
				+ KEY_PRIORITY + " NUMERIC, "
				+ KEY_TAG + " TEXT)");

		db.execSQL("CREATE TABLE " + TABLE_CHECKLIST_ITEM +"("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_ITEMID + " INTEGER PRIMARY KEY, "
				+ KEY_STATUS + " NUMERIC, "
				+ KEY_ITEMTEXT + " TEXT, "
				+ KEY_CHECKLISTID + " TEXT, "
				+ KEY_LASTUPDATED + " NUMERIC)");

		db.execSQL("CREATE TABLE " + TABLE_SCRATCH_CHECKLIST_ITEM +"("
				+ KEY_COLUMNID + " INTEGER PRIMARY KEY, "
				+ KEY_ITEMID + " INTEGER, "
				+ KEY_STATUS + " NUMERIC DEFAULT 0, "
				+ KEY_ITEMTEXT + " TEXT, "
				+ KEY_CHECKLISTID + " TEXT DEFAULT 0, "
				+ KEY_LASTUPDATED + " NUMERIC DEFAULT CURRENT_TIMESTAMP)");


		db.execSQL("CREATE TABLE " + TABLE_PROJECT + "("
				+ KEY_COLUMNID + " INTEGER PRIMARY KEY, "
				+ KEY_PROJECTID + " TEXT, "
				+ KEY_PROJECTNAME + " TEXT, "
				+ KEY_WORKSPACEID + " TEXT, "
				+ KEY_PROJECTICON + " TEXT, "
				+ KEY_USERID + " TEXT, "
				+ KEY_DUEDATE + " NUMERIC, "
				+ KEY_DUETIME + " NUMERIC, "
				+ KEY_TIMEFLAG + " NUMERIC, "
				+ KEY_LASTUPDATED + " NUMERIC, "
				+ KEY_NOTE + " TEXT, "
				+ KEY_STATUS + " NUMERIC DEFAULT 0, "
				+ KEY_PRIORITY + " NUMERIC, "
				+ "FOREIGN KEY(" + KEY_USERID + ") REFERENCES " + TABLE_USER + "(" + KEY_USERID + "), "
				+ "FOREIGN KEY(" + KEY_WORKSPACEID + ") REFERENCES " + TABLE_WORKSPACE + "(" + KEY_WORKSPACEID + "))");

		db.execSQL("CREATE TABLE " + SCRATCH + TABLE_PROJECT + "("
				+ KEY_COLUMNID + " INTEGER PRIMARY KEY, "
				+ KEY_PROJECTID + "  TEXT DEFAULT "+0 + ",  "
				+ KEY_PROJECTNAME + " TEXT, "
				+ KEY_WORKSPACEID + " TEXT DEFAULT 1 " + ", "
				+ KEY_PROJECTICON + " TEXT, "
				+ KEY_USERID + " TEXT DEFAULT "+1 + ",  "
				+ KEY_DUEDATE + " NUMERIC DEFAULT CURRENT_DATE"+ ",  "
				+ KEY_DUETIME + " NUMERIC DEFAULT CURRENT_TIME"+ ", "
				+ KEY_TIMEFLAG + "  NUMERIC DEFAULT "+0 + ", "
				+ KEY_NOTE + " TEXT, "
				+ KEY_STATUS + " NUMERIC DEFAULT 0, "
				+ KEY_PRIORITY + " NUMERIC, "
				+ KEY_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP)");


		db.execSQL("CREATE TABLE " + TABLE_WORKSPACE + "("
				+ KEY_COLUMNID + " INTEGER, "		
				+ KEY_WORKSPACEID + " TEXT, "
				+ KEY_WORKSPACEICON + " TEXT, "
				+ KEY_WORKSPACENAME + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT, "
				+ "PRIMARY KEY("+ KEY_WORKSPACEID+", "+ KEY_COLUMNID +"))"
				);

		db.execSQL("CREATE TABLE " + TABLE_SCRATCH_WORKSPACE + "("
				+ KEY_COLUMNID + " INTEGER PRIMARY KEY, "		
				+ KEY_WORKSPACEID + "TEXT DEFAULT "+1 + ", "
				+ KEY_WORKSPACEICON + " INTEGER DEFAULT "+0+", "
				+ KEY_WORKSPACENAME + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP)");




		db.execSQL("CREATE TABLE " + TABLE_USER + "("
				+ KEY_USEREMAIL + " TEXT, "
				+ KEY_USERID + " TEXT)");

		db.execSQL("CREATE TABLE " + TABLE_TASKASSIGNEE + "("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_ASSIGNEEID + " TEXT, "
				+ KEY_TASKID + " INTEGER PRIMARY KEY, "
				+ KEY_USEREMAIL + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT, "
				+ KEY_CREATORID + " INTEGER, "
				//	+ "PRIMARY KEY("+ KEY_ASSIGNEEID+", "+ KEY_TASKID +"), "
				+ "FOREIGN KEY(" + KEY_TASKID + ") REFERENCES " + TABLE_TASK + "(" + KEY_TASKID + "))");

		db.execSQL("CREATE TABLE " + SCRATCH + TABLE_TASKASSIGNEE + "("
				+ KEY_COLUMNID + " INTEGER PRIMARY KEY, "
				+ KEY_ASSIGNEEID + "TEXT DEFAULT "+1 + ", "
				+ KEY_TASKID + " INTEGER DEFAULT "+1 + ", "
				+ KEY_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_CREATORID + " INTEGER, "
				+ KEY_USEREMAIL + " TEXT)");




		//        db.execSQL("CREATE TABLE " + TABLE_LASTUPDATE + "(" + KEY_COLUMNID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		//    			+ KEY_LASTUPDATED + " text not null");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO: Add a reference to each database table here, or find a lazier way to drop all tables
		db.execSQL("DROP TABLE IF EXISTS " + Task.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SCRATCH + Task.TABLE);

		db.execSQL("DROP TABLE IF EXISTS " + Checklist.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SCRATCH + Checklist.TABLE);

		db.execSQL("DROP TABLE IF EXISTS " + DatabaseHandler.TABLE_CHECKLIST_ITEM);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseHandler.TABLE_SCRATCH_CHECKLIST_ITEM);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
		db.execSQL("DROP TABLE IF EXISTS " + SCRATCH + TABLE_PROJECT);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSPACE);
		db.execSQL("DROP TABLE IF EXISTS " + SCRATCH + TABLE_WORKSPACE);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		db.execSQL("DROP TABLE IF EXISTS " + SCRATCH + TABLE_TASKASSIGNEE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKASSIGNEE);


		onCreate(db);
	}

	public void emptyAllTables(){
		SQLiteDatabase dataBase = this.getWritableDatabase();
		dataBase.execSQL("DELETE FROM " + TABLE_TASK);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_TASK);

		dataBase.execSQL("DELETE FROM " + TABLE_CHECKLIST);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_CHECKLIST);

		dataBase.execSQL("DELETE FROM " + TABLE_PROJECT);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_PROJECT);

		dataBase.execSQL("DELETE FROM " + TABLE_WORKSPACE);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_WORKSPACE);

		dataBase.execSQL("DELETE FROM " + TABLE_CHECKLIST_ITEM);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_CHECKLIST_ITEM);

		dataBase.execSQL("DELETE FROM " + TABLE_TASKASSIGNEE);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_TASKASSIGNEE);

		dataBase.execSQL("DELETE FROM " + TABLE_USER);
	}

	public boolean emptyForSync(){
		SQLiteDatabase dataBase = this.getWritableDatabase();
		boolean deleted = false;
		try{
			dataBase.execSQL("DELETE FROM " + TABLE_TASK);
			dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_TASK);

			dataBase.execSQL("DELETE FROM " + TABLE_TASKASSIGNEE);
			dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_TASKASSIGNEE);
			deleted = true;
		}catch(Exception e){
			deleted = false;
		}
		return deleted;
	}

}
