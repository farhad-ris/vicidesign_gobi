package com.example.gobi;

public class ChecklistTag {

	private String tagID;
	private String tagText;
	private String checklistID;

	//Constructor
	public ChecklistTag(String tagID, String tagText, String checklistID) {
		super();
		this.tagID = tagID;
		this.tagText = tagText;
		this.checklistID = checklistID;
	}

	public String getTagID() {
		return tagID;
	}

	public String getTagText() {
		return tagText;
	}

	public String getChecklistID() {
		return checklistID;
	}





}
