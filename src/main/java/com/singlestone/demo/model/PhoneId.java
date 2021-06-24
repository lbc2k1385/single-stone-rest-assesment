package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

@Embeddable
public class PhoneId implements Serializable{

	private static final long serialVersionUID = -2909285363619139062L;
	

	@Enumerated(EnumType.STRING)
	@NotNull
	private PhoneType type;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "contact")
	@JsonIgnoreProperties("phone_id")
	public Contact contact;
	
	public PhoneId() {
	}
	

	public PhoneId(PhoneType type, Contact contact) {
		super();
		this.type = type;
		this.contact = contact;
	}

	public PhoneType getType() {
		return type;
	}

	public void setType(PhoneType type) {
		this.type = type;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

}
