package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Address implements Serializable {

	private static final long serialVersionUID = 1502039092352352644L;

	@EmbeddedId
	public AddressId addressId;
	
	
	public Address() {
	}

	public AddressId getAddressId() {
		return addressId;
	}

	public void setAddressId(AddressId addressId) {
		this.addressId = addressId;
	}

	public Address(AddressId addressId) {
		super();
		this.addressId = addressId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressId == null) ? 0 : addressId.hashCode());
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
		Address other = (Address) obj;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		return true;
	}
	
	


}
