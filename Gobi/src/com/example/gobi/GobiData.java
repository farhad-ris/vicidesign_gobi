package com.example.gobi;

import android.content.ContentValues;

/**
 * An abstract class to assist the DatabaseHandler class. 
 * Please note that in addition to getters, setters, and constructors,
 * subclasses should also contain the following:
 * public static final String TABLE: the table name.
 * public static final String PRIMARYKEY: the primary key name.
 * A public constructor which takes Cursor data
 */
public abstract class GobiData {

	/**
	 * Gives consistent access to the name of the SQL table.
	 * @return A string containing the name of the SQL table
	 */
	public abstract String getTable();

	/**
	 * Gives consistent access to the name of the primary key.
	 * @return A string containing the name of the primary key
	 */
	public abstract String getPrimaryKey();

	/**
	 * Gives consistent access to this instance's unique identifier.
	 */
	public abstract int getID();

	/**
	 * Packages an instance of data into a ContentValues thingy so that it can
	 * be easily added to the database.
	 * @return Container for a row of data
	 */
	public abstract ContentValues getValues();
}
