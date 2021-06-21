package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;

import com.singlestone.demo.resource.exceptions.ExceptionMessageConstants;

public class PhoneRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}$", message = ExceptionMessageConstants.INVALID_PHONE_NUM)
	public String number;

	@Enumerated(EnumType.STRING)
	private PhoneType type;

	public PhoneRequestDTO(
			@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}$", message = "Invalid Phone Number.  Phone number format should be 555-555-5555") String number,
			PhoneType type) {
		super();
		this.number = number;
		this.type = type;
	}

	public PhoneRequestDTO() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		PhoneRequestDTO other = (PhoneRequestDTO) obj;
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
