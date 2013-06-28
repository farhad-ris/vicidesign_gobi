//package com.example.gobi.test;
//
//import java.sql.Date;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.test.AndroidTestCase;
//import android.util.Log;
//
//import com.example.gobi.DatabaseHandler;
//import com.example.gobi.GobiData;
//import com.example.gobi.Task;
//
//
//
//public class DatabaseHandlerTest extends AndroidTestCase {
//	
//	private DatabaseHandler dh;
//	private Context context;
//	private SQLiteDatabase db;
//	
//	private String SCRATCH = DatabaseHandler.SCRATCH;
//	
//	//A small set of Task objects for use in testing
//	private Task firstTask;
//	private Task secondTask;
//	private Task thirdTask;
//	
//	protected void setUp() throws Exception {
//		super.setUp();
//        context = getContext();
//        dh = DatabaseHandler.getInstance(context);
//        
//		Calendar cal = Calendar.getInstance();
//        cal.set(2013, 3, 14, 4, 30, 0);
//        java.util.Date utilDate = cal.getTime();
//        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//        Timestamp timestamp = new Timestamp(sqlDate.getTime());
//		
//		firstTask = new Task(1, "Task One", 1, false, 1, sqlDate,
//				1015, false, false, "Somewhere", "Apples", 1, "jajajaja", timestamp);
//		secondTask = new Task(2, "Task Two", 2, true, 2, sqlDate,
//				0, true, true, "Nowhere", "Bananas", 2, "yipyipyip", timestamp);
//		thirdTask = new Task(3, "Task Three", 3, false, 3, sqlDate,
//				2359, true, false, "Everywhere", "Carrots", 3, "wubwubwub", timestamp);
//        
//	}
//
//	protected void tearDown() throws Exception {
//		super.tearDown();
//
//		//clear all the entries from the Task table
//		db = dh.getWritableDatabase();
//		//TODO: Need to include any table that you're testing, including scratch
//		db.delete(Task.TABLE, null, null);
//		db.delete(SCRATCH + Task.TABLE, null, null);
//		db.close();
//		
//	}
//
//	public void testGetInstance() {
//		assertNotNull("db is null",dh);
//	}
//
//	public void testOnCreateSQLiteDatabase() {
//		  ArrayList<String> tableNames = new ArrayList<String>();
//          db = dh.getWritableDatabase();
//          String sqlGetAllTables = "SELECT * FROM sqlite_master WHERE type='table' AND name!= 'android_metadata' AND name!= 'sqlite_sequence'";
//          Cursor cursor = db.rawQuery(sqlGetAllTables, null);
//          cursor.moveToFirst();
//          int expectedNumberOfTables = 4; //TODO: change this as necessary
//          int count = cursor.getCount();
//          tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
////          while(cursor.moveToNext()) {
////               //count++;
////               tableNames.add(cursor.getString(cursor.getColumnIndex("name")));
////          }we'll need this later on to see what other tables have been created
//          Log.w("tableNames", tableNames.toString());
//          cursor.close();
//          db.close();
//          //assert true/false
//          assertEquals("Wrong number of tables",expectedNumberOfTables, count);
//	}
//
//	public void testOnUpgradeSQLiteDatabaseIntInt() {
//		fail("Not yet implemented");
//	}
//
//	public void testChooseCursorConstructor() {
//		//add task to the database
//        db = dh.getWritableDatabase();
//        dh.addData(firstTask);
//		
//		//retrieve it from the database
//    	db = dh.getWritableDatabase();
//    	Cursor cursor = db.rawQuery("SELECT * FROM " + SCRATCH + Task.TABLE + " WHERE " 
//        		+ Task.PRIMARYKEY + " = " + firstTask.getTaskID(), null);
//    	cursor.moveToFirst();
//    	Log.w("TAG", cursor.toString());
//    	
//		//use chooseCursorConstructor
//		Task taskReturned = (Task) dh.chooseCursorConstructor("Task", cursor);
//		
//		//test to see if a new task object is returned
//		assertTrue("Object is not a Task", taskReturned instanceof Task);
//		
//		//test to see if null is returned
////		assertNull("Object is no Null and it should be", taskReturned); test this! later...          
//	}
//	
//	public void testAddData() {
//		//add some data
//		if (!dh.addData(firstTask)) {
//			fail("addData method failed during first call");
//		}
//		if (!dh.addData(secondTask)) {
//			fail("addData method failed during second call");
//		}
//		if (!dh.addData(thirdTask)) {
//			fail("addData method failed during third call");
//		}
//		
//		//read it with our own SQL statement
//		db = dh.getReadableDatabase();
//		
//		//count the data in the regular table
//        Cursor cursorRegularCount = db.rawQuery("SELECT * FROM " + Task.TABLE, null);
//        if (cursorRegularCount != null) {
//        	cursorRegularCount.moveToFirst();
//        }
//        assertEquals("Regular table contains data when it shouldn't",
//        		0, cursorRegularCount.getCount());
//        cursorRegularCount.close();
//        
//		//count the data in the scratch table
//        Cursor cursorScratchCount = db.rawQuery("SELECT * FROM " + SCRATCH + Task.TABLE, null);
//        if (cursorScratchCount != null) {
//        	cursorScratchCount.moveToFirst();
//        }
//        assertEquals("Scratch table doesn't contain all the data it should",
//        		3, cursorScratchCount.getCount());
//        cursorScratchCount.close();
//        
//        //read back and check task three
//        Cursor cursorThree = db.rawQuery("SELECT * FROM " + SCRATCH + Task.TABLE + " WHERE " 
//        		+ Task.PRIMARYKEY + " = " + thirdTask.getTaskID(), null);
//        if (cursorThree != null) {
//        	if (!cursorThree.moveToFirst()) {
//        		fail("Cursor 3 was empty - addData failed");
//        	}
//        } else {
//        	fail("Cursor 3 was null");
//        }
//        Task taskThreeReturned = (Task) dh.chooseCursorConstructor("Task", cursorThree);
//        compareTwoTasks(thirdTask, taskThreeReturned);
//        cursorThree.close();
//        
//        //read back and check task one
//        Cursor cursorOne = db.rawQuery("SELECT * FROM " + SCRATCH + Task.TABLE + " WHERE " 
//        		+ Task.PRIMARYKEY + " = " + firstTask.getTaskID(), null);
//        if (cursorOne != null) {
//        	if (!cursorOne.moveToFirst()) {
//        		fail("Cursor 1 was empty - addData failed");
//        	}
//        } else {
//        	fail("Cursor 1 was null");
//        }
//        Task taskOneReturned = (Task) dh.chooseCursorConstructor("Task", cursorOne);
//        compareTwoTasks(firstTask, taskOneReturned);
//        cursorOne.close();
//        
//        //read back and check task two
//        Cursor cursorTwo = db.rawQuery("SELECT * FROM " + SCRATCH + Task.TABLE + " WHERE " 
//        		+ Task.PRIMARYKEY + " = " + secondTask.getTaskID(), null);
//        if (cursorTwo != null) {
//        	if (!cursorTwo.moveToFirst()) {
//        		fail("Cursor 2 was empty - addData failed");
//        	}
//        } else {
//        	fail("Cursor 2 was null");
//        }
//        Task taskTwoReturned = (Task) dh.chooseCursorConstructor("Task", cursorTwo);
//        compareTwoTasks(secondTask, taskTwoReturned);
//        cursorTwo.close();
//        
//        db.close();
//	}
//	
//	public void testUpdateData() {
//		//add data directly to regular table
//		db = dh.getWritableDatabase();
//    	db.insert(Task.TABLE, null, firstTask.getValues());
//    	db.close();
//		
//		//update the object
//		Task changedFirstTask = firstTask;
//		changedFirstTask.setTaskName("Wally"); //Yeah, I went there!
//		dh.updateData(changedFirstTask);
//		
//		//check regular table - data should be absent
//		db = dh.getReadableDatabase();
//		Cursor cursorRegular = db.rawQuery("SELECT * FROM " + Task.TABLE
//				+ " WHERE " + Task.PRIMARYKEY + " = " + 1, null);
//		cursorRegular.moveToFirst();
//		assertEquals("updateData didn't remove data from regular table", 
//				0, cursorRegular.getCount());
//		cursorRegular.close();
//		
//		//check scratch table - data should be changed
//		Cursor cursorScratch = db.rawQuery("SELECT * FROM "
//				+ SCRATCH + Task.TABLE
//				+ " WHERE " + Task.PRIMARYKEY + " = " + 1, null);
//		cursorScratch.moveToFirst();
//		Task taskReturned = (Task) dh.chooseCursorConstructor(Task.TABLE, cursorScratch);
//		compareTwoTasks(changedFirstTask, taskReturned);
//		cursorScratch.close();
//	}
//
//	//Dependant on addData and getDataCount test passing
//	public void testDeleteData() {
//		//add some tasks
//		dh.addData(firstTask);
//		dh.addData(secondTask);
//		dh.addData(thirdTask);
//		
//		//check the number
//		int expectedCount1 = 3;
//		int actualCount1 = dh.getDataCount(Task.TABLE);
//		assertEquals("Wrong count of data rows", expectedCount1, actualCount1);
//		
//		//delete a task, then recheck
//		dh.deleteData(secondTask);
//		int expectedCount2 = 2;
//		int actualCount2 = dh.getDataCount(Task.TABLE);
//		assertEquals("Wrong count of data rows", expectedCount2, actualCount2);
//		
//		//delete the remaining two, the recheck
//		dh.deleteData(firstTask);
//		dh.deleteData(thirdTask);
//		
//		int expectedCount3 = 0;
//		int actualCount3 = dh.getDataCount(Task.TABLE);
//		assertEquals("Wrong count of data rows", expectedCount3, actualCount3);
//		
//		//TODO: Manually add data to regular table (not scratch), then try deleting that
//	}
//
//	//Dependant on addData test passing
//	public void testGetData() {
//		//add more than one task
//		dh.addData(firstTask);
//		dh.addData(secondTask);
//		dh.addData(thirdTask);
//		
//        Task secondTaskReturned = (Task) dh.getData(Task.TABLE, Task.PRIMARYKEY, 2);
//        compareTwoTasks(secondTask, secondTaskReturned);
//        
//        Task firstTaskReturned = (Task) dh.getData(Task.TABLE, Task.PRIMARYKEY, 1);
//        compareTwoTasks(firstTask, firstTaskReturned);
//        
//        Task thirdTaskReturned = (Task) dh.getData(Task.TABLE, Task.PRIMARYKEY, 3);
//        compareTwoTasks(thirdTask, thirdTaskReturned);
//	}
//
//	//Dependant on addData test passing
//	public void testGetAllData() {
//		//add more than one task
//		dh.addData(firstTask);
//		dh.addData(secondTask);
//		dh.addData(thirdTask);
//		
//		List<GobiData> list = dh.getAllData(Task.TABLE);
//		
//        compareTwoTasks(firstTask, (Task) list.get(0));
//        compareTwoTasks(secondTask, (Task) list.get(1));
//        compareTwoTasks(thirdTask, (Task) list.get(2));
//	}
//
//	//Dependant on addData passing
//	public void testGetDataCount() {
//		dh.addData(firstTask);
//		dh.addData(secondTask);
//		dh.addData(thirdTask);
//		
//		int expectedCount = 3;
//		int actualCount = dh.getDataCount(Task.TABLE);
//		
//		assertEquals("Wrong count of data rows", expectedCount, actualCount);
//	}
//	
//	/**
//	 * Compares each field of two given tasks.
//	 * @param one The first task
//	 * @param two The second task
//	 * @ensures Fails the test it's called from if a field does not match.
//	 */
//	private void compareTwoTasks(Task one, Task two) {
//        assertEquals("taskID was not equal", one.getTaskID(), two.getTaskID());
//        assertEquals("taskName was not equal", one.getTaskName(), two.getTaskName());
//        assertEquals("workspaceID was not equal", one.getWorkspaceID(), two.getWorkspaceID());
//        assertEquals("Priority was not equal", one.isPriority(), two.isPriority());
//        assertEquals("UserId was not equal", one.getUserID(), two.getUserID());
//        assertEquals("dueDate was not equal", one.getDueDate().toString(), two.getDueDate().toString());
//        assertEquals("dueTime was not equal", one.getDueTime(), two.getDueTime());
//        assertEquals("TimeFlag was not equal", one.isTimeFlag(), two.isTimeFlag());
//        assertEquals("Status was not equal", one.isStatus(), two.isStatus());
//        assertEquals("GeoLocation was not equal", one.getGeolocation(), two.getGeolocation());
//        assertEquals("Tag was not equal", one.getTag(), two.getTag());
//        assertEquals("ProjectId was not equal", one.getProjectID(), two.getProjectID());
//        assertEquals("taskNote was not equal", one.getTaskNote(), two.getTaskNote());
//        assertEquals("lastUpdated was not equal", one.getLastUpdated(), two.getLastUpdated());
//	}
//	
//}
