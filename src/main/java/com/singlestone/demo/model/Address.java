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

}
