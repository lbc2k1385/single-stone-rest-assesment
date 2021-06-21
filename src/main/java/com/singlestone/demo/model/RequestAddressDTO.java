package com.singlestone.demo.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.singlestone.demo.resource.exceptions.ExceptionMessageConstants;

public class RequestAddressDTO implements Serializable {

	private static final long serialVersionUID = -4517098494018713416L;

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

	public RequestAddressDTO(
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
	
	public RequestAddressDTO() {
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

}
