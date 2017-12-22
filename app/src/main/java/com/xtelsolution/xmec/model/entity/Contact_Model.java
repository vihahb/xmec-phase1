package com.xtelsolution.xmec.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Contact_Model extends RealmObject{

	// Getter and setter for contacts
	@PrimaryKey
    private String contactId;
	private String contactName,
			contactHome,
			contactMobile,
			contactWork,
			contactEmail,
			contactOtherDetails;

	public Contact_Model(String contactId, String contactName, String contactHome, String contactMobile, String contactWork, String contactEmail, String contactOtherDetails) {
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactHome = contactHome;
		this.contactMobile = contactMobile;
		this.contactWork = contactWork;
		this.contactEmail = contactEmail;
		this.contactOtherDetails = contactOtherDetails;
	}

	public Contact_Model() {
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactHome() {
		return contactHome;
	}

	public void setContactHome(String contactHome) {
		this.contactHome = contactHome;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactWork() {
		return contactWork;
	}

	public void setContactWork(String contactWork) {
		this.contactWork = contactWork;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactOtherDetails() {
		return contactOtherDetails;
	}

	public void setContactOtherDetails(String contactOtherDetails) {
		this.contactOtherDetails = contactOtherDetails;
	}

	@Override
	public String toString() {
		return "Contact_Model{" +
				"contactId='" + contactId + '\'' +
				", contactName='" + contactName + '\'' +
				", contactHome='" + contactHome + '\'' +
				", contactMobile='" + contactMobile + '\'' +
				", contactWork='" + contactWork + '\'' +
				", contactEmail='" + contactEmail + '\'' +
				", contactOtherDetails='" + contactOtherDetails + '\'' +
				'}';
	}
}