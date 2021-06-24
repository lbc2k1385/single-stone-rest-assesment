package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import com.singlestone.demo.resource.exceptions.ExceptionMessageConstants;
import com.sun.istack.NotNull;

@Entity
public class Phone implements Serializable {

	private static final long serialVersionUID = -5286684280649815345L;

	@EmbeddedId
	private PhoneId phoneId;
	
	
	@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}$", message = ExceptionMessageConstants.INVALID_PHONE_NUM)
	@NotNull
	private String number;


	public Phone() {
	}
	
	public Phone(PhoneId phoneId,
			@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}$", message = "Invalid Phone Number.  Phone number format should be 555-555-5555") String number) {
		super();
		this.phoneId = phoneId;
		this.number = number;
	}




	public PhoneId getPhoneId() {
		return phoneId;
	}


	public void setPhoneId(PhoneId phoneId) {
		this.phoneId = phoneId;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}
	
	
	
}