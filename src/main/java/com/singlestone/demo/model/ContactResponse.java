package com.singlestone.demo.model;

import java.util.List;

public class ContactResponse {
	
	private Name name;
	private List<PhoneRequestDTO> phone;
	private RequestAddressDTO address;
	private String email;
	
	public ContactResponse(Name name, List<PhoneRequestDTO> phone, RequestAddressDTO address, String email) {
		super();
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.email = email;
	}
	
	public ContactResponse() {
	}


	

	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public List<PhoneRequestDTO> getPhone() {
		return phone;
	}
	public void setPhone(List<PhoneRequestDTO> phone) {
		this.phone = phone;
	}
	public RequestAddressDTO getAddress() {
		return address;
	}
	public void setAddress(RequestAddressDTO address) {
		this.address = address;
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
		ContactResponse other = (ContactResponse) obj;
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
