package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;

import com.singlestone.demo.resource.exceptions.ExceptionMessageConstants;
import com.sun.istack.NotNull;

@Entity
public class Phone implements Serializable {

	private static final long serialVersionUID = -5286684280649815345L;

	@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}$", message = ExceptionMessageConstants.INVALID_PHONE_NUM)
	@NotNull
	public String number;

	@Id
	@Enumerated(EnumType.STRING)
	@NotNull
	private PhoneType type;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "contact_id", referencedColumnName = "id")
	public Contact contact;

	public Phone() {
	}

	public Phone(
			@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}$", message = "Invalid Phone Number.  Phone number format should be 555-555-5555") String number,
			PhoneType type) {
		super();
		this.number = number;
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phone other = (Phone) obj;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}