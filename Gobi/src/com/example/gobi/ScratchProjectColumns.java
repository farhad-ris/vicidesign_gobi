package com.example.gobi;



public class ScratchProjectColumns {

	private ScratchProjectColumns() {};


	public static final String TABLENAME = DatabaseHandler.TABLE_SCRATCH_PROJECT;


	public static final String _ID = DatabaseHandler.KEY_COLUMNID;


	public static final String FULL_ID =  TABLENAME + "." + DatabaseHandler.KEY_COLUMNID;   


	public static final String PROJECTNAME = DatabaseHandler.KEY_PROJECTNAME;    


	public static final String FULL_PROJECTNAME =  TABLENAME + "." + PROJECTNAME;  


	public static final String DUEDATE = DatabaseHandler.KEY_DUEDATE;       


	public static final String FULL_DUEDATE =  TABLENAME + "." + DUEDATE;


	public static final String DUETIME = DatabaseHandler.KEY_DUETIME;       


	public static final String FULL_DUETIME =  TABLENAME + "." + DUETIME;


	public static final String STATUS = DatabaseHandler.KEY_STATUS;


	public static final String FULL_STATUS = TABLENAME + "." + STATUS;


	public static final String PRIORITY = DatabaseHandler.KEY_PRIORITY;


	public static final String FULL_PRIORITY = TABLENAME + "." + PRIORITY;


	//    public static final String NOTE = DatabaseHandler.KEY_PROJECTNOTE;
	public static final String NOTE = DatabaseHandler.KEY_NOTE;


	public static final String FULL_NOTE = TABLENAME + "." + NOTE;


	public static final String PROJECT_ID = DatabaseHandler.KEY_PROJECTID;


	public static final String FULL_PROJECT_ID = TABLENAME + "." + PROJECT_ID;

	public static final String WORKSPACE_ID = DatabaseHandler.KEY_WORKSPACEID;

	public static final String FULL_WORKSPACE_ID = TABLENAME + "." + WORKSPACE_ID;

}

