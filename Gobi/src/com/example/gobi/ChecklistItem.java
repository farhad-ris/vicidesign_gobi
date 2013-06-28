package com.example.gobi;

public class ChecklistItem {
	private String itemID;
	private boolean status;
	private String itemText;
	private String checklistID;

	//Constructor
	public ChecklistItem(String itemID, boolean status, String itemText,
			String checklistID) {
		super();
		this.itemID = itemID;
		this.status = status;
		this.itemText = itemText;
		this.checklistID = checklistID;
	}

	public String getItemID() {
		return itemID;
	}

	public boolean isStatus() {
		return status;
	}

	public String getItemText() {
		return itemText;
	}

	public String getChecklistID() {
		return checklistID;
	}



}
