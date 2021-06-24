package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.singlestone.demo.resource.exceptions.ExceptionMessageConstants;

@Embeddable
public class AddressId implements Serializable {

	private static final long serialVersionUID = -6632874958707813469L;
	
		
	@NotNull
	@Size(min = 2, message = ExceptionMessageConstants.MIN_STREET_NAME)
	@Size(max = 100, message = ExceptionMessageConstants.MAX_STREET_NAME)
	private String street;

	@NotNull
	@Size(min = 2, message = ExceptionMessageConstants.MIN_CITY_NAME)
	@Size(max = 100, message = ExceptionMessageConstants.MAX_CITY_NAME)
	private String city;

	@NotNull
	@Size(min = 2, max = 2, message = ExceptionMessageConstants.MAX_MIN_STATE_CODE)
	private String state;

	@NotNull
	@Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = ExceptionMessageConstants.INVALID_ZIP_CODE)
	private String zip;
	
	
	/*
	 * The foreign key needs to be a part of the primary key. 
	 * If it is not, then separate contacts that happen to share the same address will cause a conflict.
	 */
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "contact")
	@JsonIgnore
	private Contact contact;
	
	public Contact getContact() {
		return contact;
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public AddressId(@NotNull Contact contact,
			@NotNull @Size(min = 2, message = "Street name must be at least 2 characters") @Size(max = 100, message = "Street name must not be more than 100 characters") String street,
			@NotNull @Size(min = 2, message = "City name must be at least 2 characters") @Size(max = 100, message = "City name must not be more than 100 characters") String city,
			@NotNull @Size(min = 2, max = 2, message = "State code must be 2 characters") String state,
			@NotNull @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Invalid Zipcode") String zip) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public AddressId(
			@NotNull @Size(min = 2, message = "Street name must be at least 2 characters") @Size(max = 100, message = "Street name must not be more than 100 characters") String street,
			@NotNull @Size(min = 2, message = "City name must be at least 2 characters") @Size(max = 100, message = "City name must not be more than 100 characters") String city,
			@NotNull @Size(min = 2, max = 2, message = "State code must be 2 characters") String state,
			@NotNull @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Invalid Zipcode") String zip) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	

	public AddressId() {
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		AddressId other = (AddressId) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}
	

	
	
	
	
}
