package com.singlestone.demo.model;

public class CallListContact {

	private Name name;
	public String phone;
	

	public CallListContact(Name name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

}
