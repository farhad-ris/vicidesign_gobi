package com.example.gobi;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class GobiContentProvider extends ContentProvider {

	/*
	 * references: Content Provider Tutorials
	 * http://mobile.tutsplus.com/tutorials/android/android-sdk_content-providers/
	 * http://www.vogella.com/articles/AndroidSQLite/article.html
	 */
	private DatabaseHandler dh;

	static HashMap<String, String> sScratchTablesProjectionMap;
	//static HashMap<String,String> sScratchChecklistProjectionMap;

	private static final String AUTHORITY = "com.example.gobi.GobiContentProvider";

	//Tables' + Scratch tables base path
	private static final String TASK_BASE_PATH = DatabaseHandler.TABLE_TASK;
	private static final String SCRATCH_TASK_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_TASK;

	private static final String CHECKLIST_BASE_PATH = DatabaseHandler.TABLE_CHECKLIST;
	private static final String SCRATCH_CHECKLIST_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_CHECKLIST;

	private static final String PROJECT_BASE_PATH = DatabaseHandler.TABLE_PROJECT;
	private static final String SCRATCH_PROJECT_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_PROJECT;

	private static final String WORKSPACE_BASE_PATH = DatabaseHandler.TABLE_WORKSPACE;
	private static final String SCRATCH_WORKSPACE_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_WORKSPACE;

	private static final String CHECKLIST_ITEM_BASE_PATH = DatabaseHandler.TABLE_CHECKLIST_ITEM;
	private static final String SCRATCH_CHECKLIST_ITEM_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_CHECKLIST_ITEM;

	private static final String USER_BASE_PATH = DatabaseHandler.TABLE_USER;

	private static final String TASKASSIGNEE_BASE_PATH = DatabaseHandler.TABLE_TASKASSIGNEE;
	private static final String SCRATCH_TASKASSIGNEE_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_TASKASSIGNEE;


	private static final String SCRATCH_TASK_AND_CHECKLIST_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_TASK + " , " + DatabaseHandler.TABLE_SCRATCH_CHECKLIST;

	//Content URIs
	//Task and ScratchTask
	public static final Uri CONTENT_URI_TASK = Uri.parse("content://" + AUTHORITY + "/" + TASK_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_TASK = Uri.parse("content://" + AUTHORITY + "/" +
			SCRATCH_TASK_BASE_PATH);
	//Checklist and ScratchChecklist
	public static final Uri CONTENT_URI_CHECKLIST = Uri.parse("content://" + AUTHORITY + "/" + 
			CHECKLIST_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_CHECKLIST = Uri.parse("content://" + AUTHORITY + "/" + 
			SCRATCH_CHECKLIST_BASE_PATH);
	//Project and Scratch Project
	public static final Uri CONTENT_URI_PROJECT = Uri.parse("content://" + AUTHORITY + "/" + 
			PROJECT_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_PROJECT = Uri.parse("content://" + AUTHORITY + "/" + 
			SCRATCH_PROJECT_BASE_PATH);
	//Workspace and ScratchWorkspace
	public static final Uri CONTENT_URI_WORKSPACE = Uri.parse("content://" + AUTHORITY + "/" + 
			WORKSPACE_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_WORKSPACE = Uri.parse("content://" + AUTHORITY + "/" + 
			SCRATCH_WORKSPACE_BASE_PATH);
	public static final Uri CONTENT_URI_CHECKLIST_ITEM = Uri.parse("content://" + AUTHORITY + "/" + 
			CHECKLIST_ITEM_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_CHECKLIST_ITEM = Uri.parse("content://" + AUTHORITY + "/" + 
			SCRATCH_CHECKLIST_ITEM_BASE_PATH);
	//scratch Task JOIN scratch Checklist
	public static final Uri CONTENT_URI_SCRATCH_TASK_AND_CHECKLIST = Uri.parse("content://" + AUTHORITY + "/" + 
			SCRATCH_TASK_AND_CHECKLIST_BASE_PATH);
	//USER
	public static final Uri CONTENT_URI_USER = Uri.parse("content://" + AUTHORITY + "/" + 
			USER_BASE_PATH);
	// scratch TaskAssignee and scratch Task Assignee
	public static final Uri CONTENT_URI_TASKASSIGNEE = Uri.parse("content://" + AUTHORITY + "/" + TASKASSIGNEE_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_TASKASSIGNEE = Uri.parse("content://" + AUTHORITY + "/" +
			SCRATCH_TASKASSIGNEE_BASE_PATH);

	//don't really know what these are for, but let's leave them anyway :)
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/task";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";

	//Used for the Uri matcher

	//task and scratch task
	private static final int TASK_COLLECTION = 100;
	private static final int TASK_ID = 110;

	private static final int SCRATCH_TASK_COLLECTION = 120;
	private static final int SCRATCH_TASK_ID = 130;

	//checklist and scratch checklist
	private static final int CHECKLIST_COLLECTION = 200;
	private static final int CHECKLIST_ID = 210;

	private static final int SCRATCH_CHECKLIST_COLLECTION = 220;
	private static final int SCRATCH_CHECKLIST_ID = 230;

	//project and scratch project
	private static final int PROJECT_COLLECTION = 400;
	private static final int PROJECT_ID = 410;

	private static final int SCRATCH_PROJECT_COLLECTION = 420;
	private static final int SCRATCH_PROJECT_ID = 430;

	//workspace and scratch workspace
	private static final int WORKSPACE_COLLECTION = 500;
	private static final int WORKSPACE_ID = 510;

	private static final int SCRATCH_WORKSPACE_COLLECTION = 520;
	private static final int SCRATCH_WORKSPACE_ID = 530;

	//checklist item and scratch checklist item 
	private static final int CHECKLIST_ITEM_COLLECTION = 600;
	private static final int CHECKLIST_ITEM_ID = 610;

	private static final int SCRATCH_CHECKLIST_ITEM_COLLECTION = 620;
	private static final int SCRATCH_CHECKLIST_ITEM_ID = 630;

	//USER
	private static final int USER_COLLECTION = 700;
	private static final int USER_ID = 710;

	// scratch TaskAssignee and scratch Task Assignee
	private static final int TASKASSIGNEE_COLLECTION = 800;
	private static final int TASKASSIGNEE_ID = 810;

	private static final int SCRATCH_TASKASSIGNEE_COLLECTION = 820;
	private static final int SCRATCH_TASKASSIGNEE_ID = 830;

	//scratch Task JOIN scratch Checklist
	private static final int SCRATCH_TASK_AND_CHECKLIST_COLLECTION = 1000;



	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		//task and scratch task
		sURIMatcher.addURI(AUTHORITY, TASK_BASE_PATH, TASK_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, TASK_BASE_PATH + "/#", TASK_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_TASK_BASE_PATH, SCRATCH_TASK_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_TASK_BASE_PATH + "/#", SCRATCH_TASK_ID);

		//checklist and scratch checklist
		sURIMatcher.addURI(AUTHORITY, CHECKLIST_BASE_PATH, CHECKLIST_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, CHECKLIST_BASE_PATH + "/#", CHECKLIST_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_CHECKLIST_BASE_PATH, SCRATCH_CHECKLIST_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_CHECKLIST_BASE_PATH + "/#", SCRATCH_CHECKLIST_ID);

		//project and scratch project
		sURIMatcher.addURI(AUTHORITY, PROJECT_BASE_PATH, PROJECT_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, PROJECT_BASE_PATH + "/#", PROJECT_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_PROJECT_BASE_PATH, SCRATCH_PROJECT_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_PROJECT_BASE_PATH + "/#", SCRATCH_PROJECT_ID);

		//workspace and scratch workspace
		sURIMatcher.addURI(AUTHORITY, WORKSPACE_BASE_PATH, WORKSPACE_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, WORKSPACE_BASE_PATH + "/#", WORKSPACE_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_WORKSPACE_BASE_PATH, SCRATCH_WORKSPACE_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_WORKSPACE_BASE_PATH + "/#", SCRATCH_WORKSPACE_ID);

		//checklist item and scratch checklist item
		sURIMatcher.addURI(AUTHORITY, CHECKLIST_ITEM_BASE_PATH, CHECKLIST_ITEM_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, CHECKLIST_ITEM_BASE_PATH + "/#", CHECKLIST_ITEM_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_CHECKLIST_ITEM_BASE_PATH, SCRATCH_CHECKLIST_ITEM_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_CHECKLIST_ITEM_BASE_PATH + "/#", SCRATCH_CHECKLIST_ITEM_ID);

		//USER
		sURIMatcher.addURI(AUTHORITY, USER_BASE_PATH, USER_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, USER_BASE_PATH + "/#", USER_ID);

		// scratch TaskAssignee and scratch Task Assignee
		sURIMatcher.addURI(AUTHORITY, TASKASSIGNEE_BASE_PATH, TASKASSIGNEE_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, TASKASSIGNEE_BASE_PATH + "/#", TASKASSIGNEE_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_TASKASSIGNEE_BASE_PATH, SCRATCH_TASKASSIGNEE_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_TASKASSIGNEE_BASE_PATH + "/#", SCRATCH_TASKASSIGNEE_ID);

		//scratch task JOIN scratch checklist
		sURIMatcher.addURI(AUTHORITY, SCRATCH_TASK_AND_CHECKLIST_BASE_PATH , SCRATCH_TASK_AND_CHECKLIST_COLLECTION);
	}


	static {
		//Setup projection maps
		sScratchTablesProjectionMap = new HashMap<String, String>();
		sScratchTablesProjectionMap.put(ScratchTaskColumns._ID, ScratchTaskColumns.FULL_ID);
		sScratchTablesProjectionMap.put(ScratchTaskColumns.TASKNAME, ScratchTaskColumns.FULL_TASKNAME);
		sScratchTablesProjectionMap.put(ScratchTaskColumns.DUEDATE, ScratchTaskColumns.FULL_DUEDATE);
		sScratchTablesProjectionMap.put(ScratchTaskColumns.STATUS, ScratchTaskColumns.FULL_STATUS);
		sScratchTablesProjectionMap.put(ScratchTaskColumns.PRIORITY, ScratchTaskColumns.FULL_PRIORITY);
		sScratchTablesProjectionMap.put(ScratchTaskColumns.NOTE, ScratchTaskColumns.FULL_NOTE);
		sScratchTablesProjectionMap.put(ScratchTaskColumns.PROJECT_ID, ScratchTaskColumns.FULL_PROJECT_ID);

		// sScratchChecklistProjectionMap = new HashMap<String, String>();
		sScratchTablesProjectionMap.put(ScratchChecklistColumns._ID, ScratchChecklistColumns.FULL_ID);
		sScratchTablesProjectionMap.put(ScratchChecklistColumns.CHECKLISTNAME, ScratchChecklistColumns.FULL_CHECKLISTNAME);
		sScratchTablesProjectionMap.put(ScratchChecklistColumns.DUEDATE, ScratchChecklistColumns.FULL_DUEDATE);
		sScratchTablesProjectionMap.put(ScratchChecklistColumns.STATUS, ScratchChecklistColumns.FULL_STATUS);
		sScratchTablesProjectionMap.put(ScratchChecklistColumns.PRIORITY, ScratchChecklistColumns.FULL_PRIORITY);
		sScratchTablesProjectionMap.put(ScratchChecklistColumns.NOTE, ScratchChecklistColumns.FULL_NOTE);
		sScratchTablesProjectionMap.put(ScratchChecklistColumns.PROJECT_ID, ScratchChecklistColumns.FULL_PROJECT_ID);

		sScratchTablesProjectionMap.put(ScratchProjectColumns._ID, ScratchProjectColumns.FULL_ID);
		sScratchTablesProjectionMap.put(ScratchProjectColumns.PROJECTNAME, ScratchProjectColumns.FULL_PROJECTNAME);
		sScratchTablesProjectionMap.put(ScratchProjectColumns.DUEDATE, ScratchProjectColumns.FULL_DUEDATE);
		sScratchTablesProjectionMap.put(ScratchProjectColumns.STATUS, ScratchProjectColumns.FULL_STATUS);
		sScratchTablesProjectionMap.put(ScratchProjectColumns.PRIORITY, ScratchProjectColumns.FULL_PRIORITY);
		sScratchTablesProjectionMap.put(ScratchProjectColumns.NOTE, ScratchProjectColumns.FULL_NOTE);
		sScratchTablesProjectionMap.put(ScratchProjectColumns.PROJECT_ID, ScratchProjectColumns.FULL_PROJECT_ID);
	}

	@Override
	public boolean onCreate() {
		dh = DatabaseHandler.getInstance(getContext());
		return false;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}


	public String getTableName(Uri uri) {
		int uriType = sURIMatcher.match(uri);

		switch (uriType) {
		case TASK_COLLECTION:
			return DatabaseHandler.TABLE_TASK;
		case TASK_ID:
			return DatabaseHandler.TABLE_TASK;
		case SCRATCH_TASK_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_TASK;
		case SCRATCH_TASK_ID:
			return DatabaseHandler.TABLE_SCRATCH_TASK;
		case CHECKLIST_COLLECTION:
			return DatabaseHandler.TABLE_CHECKLIST;
		case CHECKLIST_ID:
			return DatabaseHandler.TABLE_CHECKLIST;
		case SCRATCH_CHECKLIST_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_CHECKLIST;
		case SCRATCH_CHECKLIST_ID:
			return DatabaseHandler.TABLE_SCRATCH_CHECKLIST;
		case PROJECT_COLLECTION:
			return DatabaseHandler.TABLE_PROJECT;
		case PROJECT_ID:
			return DatabaseHandler.TABLE_PROJECT;
		case SCRATCH_PROJECT_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_PROJECT;
		case SCRATCH_PROJECT_ID:
			return DatabaseHandler.TABLE_SCRATCH_PROJECT;
		case WORKSPACE_COLLECTION:
			return DatabaseHandler.TABLE_WORKSPACE;
		case WORKSPACE_ID:
			return DatabaseHandler.TABLE_WORKSPACE;
		case SCRATCH_WORKSPACE_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_WORKSPACE;
		case SCRATCH_WORKSPACE_ID:
			return DatabaseHandler.TABLE_SCRATCH_WORKSPACE;
		case CHECKLIST_ITEM_COLLECTION:
			return DatabaseHandler.TABLE_CHECKLIST_ITEM;
		case CHECKLIST_ITEM_ID:
			return DatabaseHandler.TABLE_CHECKLIST_ITEM;
		case SCRATCH_CHECKLIST_ITEM_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_CHECKLIST_ITEM;
		case SCRATCH_CHECKLIST_ITEM_ID:
			return DatabaseHandler.TABLE_SCRATCH_CHECKLIST_ITEM;
		case USER_COLLECTION:
			return DatabaseHandler.TABLE_USER;
		case USER_ID:
			return DatabaseHandler.TABLE_USER;
		case TASKASSIGNEE_COLLECTION:
			return DatabaseHandler.TABLE_TASKASSIGNEE;
		case TASKASSIGNEE_ID:
			return DatabaseHandler.TABLE_TASKASSIGNEE;
		case SCRATCH_TASKASSIGNEE_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_TASKASSIGNEE;
		case SCRATCH_TASKASSIGNEE_ID:
			return DatabaseHandler.TABLE_SCRATCH_TASKASSIGNEE;
		case SCRATCH_TASK_AND_CHECKLIST_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_TASK + ", " + DatabaseHandler.TABLE_SCRATCH_CHECKLIST;
		default:
			return null;
		}

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		String tableName = getTableName(uri);
		queryBuilder.setTables(tableName);




		int uriType = sURIMatcher.match(uri);

		switch(uriType) {
		case TASK_COLLECTION:
			break;
		case TASK_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;//ASK ENRIQUE
		case SCRATCH_TASK_COLLECTION:
			break;
		case SCRATCH_TASK_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case CHECKLIST_COLLECTION:
			break;
		case CHECKLIST_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case SCRATCH_CHECKLIST_COLLECTION:
			break;
		case SCRATCH_CHECKLIST_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case PROJECT_COLLECTION:
			break;
		case PROJECT_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case SCRATCH_PROJECT_COLLECTION:
			break;
		case SCRATCH_PROJECT_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case WORKSPACE_COLLECTION:
			break;
		case WORKSPACE_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID+ "=" + 
					uri.getLastPathSegment());
			break;
		case SCRATCH_WORKSPACE_COLLECTION:
			break;
		case SCRATCH_WORKSPACE_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case CHECKLIST_ITEM_COLLECTION:
			break;
		case CHECKLIST_ITEM_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case SCRATCH_CHECKLIST_ITEM_COLLECTION:
			break;
		case SCRATCH_CHECKLIST_ITEM_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case USER_COLLECTION:
			break;
		case USER_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_USERID + "=" + 
					uri.getLastPathSegment());
			break;
		case TASKASSIGNEE_COLLECTION:
			break;
		case TASKASSIGNEE_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_ASSIGNEEID + DatabaseHandler.KEY_TASKID + "=" + 
					uri.getLastPathSegment());//NOT SURE HOW THIS WORKS WITH COMBINED PRIMARY KEYS
			break;
		case SCRATCH_TASKASSIGNEE_COLLECTION:
			break;
		case SCRATCH_TASKASSIGNEE_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case SCRATCH_TASK_AND_CHECKLIST_COLLECTION:
			queryBuilder.setTables(ScratchTaskColumns.TABLENAME + " LEFT OUTER JOIN " + 
					ScratchChecklistColumns.TABLENAME + " ON " + ScratchTaskColumns.FULL_PROJECT_ID + "=" + ScratchChecklistColumns.FULL_PROJECT_ID);
			//				queryBuilder.setProjectionMap(sScratchTablesProjectionMap);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		Cursor result = queryBuilder.query(dh.getReadableDatabase(), 
				projection, selection, selectionArgs, null, null, sortOrder);
		result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		String tableName = getTableName(uri);
		int uriType = sURIMatcher.match(uri);

		long id = 0;
		switch(uriType) {
		case TASK_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_TASK_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case CHECKLIST_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_CHECKLIST_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case PROJECT_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_PROJECT_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case WORKSPACE_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_WORKSPACE_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case CHECKLIST_ITEM_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_CHECKLIST_ITEM_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case USER_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case TASKASSIGNEE_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_TASKASSIGNEE_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		}

		getContext().getContentResolver().notifyChange(uri,null);
		return ContentUris.withAppendedId(uri,id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int uriType = sURIMatcher.match(uri);
		int rowsUpdated = 0;
		String tableName = getTableName(uri);
		String id = uri.getLastPathSegment();

		switch(uriType) {
		case TASK_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case TASK_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + " = " + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_TASK_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_TASK_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case CHECKLIST_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case CHECKLIST_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_CHECKLIST_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_CHECKLIST_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case PROJECT_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case PROJECT_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_PROJECT_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_PROJECT_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case WORKSPACE_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case WORKSPACE_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_WORKSPACE_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_WORKSPACE_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case CHECKLIST_ITEM_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case CHECKLIST_ITEM_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_CHECKLIST_ITEM_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_CHECKLIST_ITEM_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case USER_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case USER_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_USERID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_USERID + "=" + id, null);
			}
			break;
		case TASKASSIGNEE_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case TASKASSIGNEE_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_ASSIGNEEID + DatabaseHandler.KEY_TASKID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_ASSIGNEEID + DatabaseHandler.KEY_TASKID + "=" + id, null);
			}
			break;
		case SCRATCH_TASKASSIGNEE_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_TASKASSIGNEE_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsDeleted = 0;
		SQLiteDatabase db = dh.getWritableDatabase();
		int uriType = sURIMatcher.match(uri);
		String tableName = getTableName(uri);
		String id = uri.getLastPathSegment();

		switch(uriType) {
		case TASK_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case TASK_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_TASK_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_TASK_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case CHECKLIST_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case CHECKLIST_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_CHECKLIST_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_CHECKLIST_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case PROJECT_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case PROJECT_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_PROJECT_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_PROJECT_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case WORKSPACE_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case WORKSPACE_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_WORKSPACE_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_WORKSPACE_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case CHECKLIST_ITEM_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case CHECKLIST_ITEM_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_CHECKLIST_ITEM_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_CHECKLIST_ITEM_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case USER_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case USER_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_USERID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_USERID + "=" + id, null);
			}
			break;
		case TASKASSIGNEE_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case TASKASSIGNEE_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_ASSIGNEEID + DatabaseHandler.KEY_TASKID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_ASSIGNEEID + DatabaseHandler.KEY_TASKID + "=" + id, null);
			}
			break;
		case SCRATCH_TASKASSIGNEE_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_TASKASSIGNEE_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}
}
