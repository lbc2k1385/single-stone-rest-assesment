package com.singlestone.demo.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Email;

public class ContactRequest implements Serializable {

	private static final long serialVersionUID = 2202289758140746827L;

	private Name name;

	private RequestAddressDTO address;

	private List<PhoneRequestDTO> phone;

	@Email(message = "Email is not valid")
	private String email;

	public ContactRequest() {
	}

	public ContactRequest(Name name, RequestAddressDTO address, List<PhoneRequestDTO> phone,
			@Email(message = "Email is not valid") String email) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public RequestAddressDTO getAddress() {
		return address;
	}

	public void setAddress(RequestAddressDTO address) {
		this.address = address;
	}

	public List<PhoneRequestDTO> getPhone() {
		return phone;
	}

	public void setPhone(List<PhoneRequestDTO> phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		ContactRequest other = (ContactRequest) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		return true;
	}

}
