package com.singlestone.demo.resource.exceptions;

public class ExceptionMessageConstants {

	public ExceptionMessageConstants() {

	}

	public static final String VALIDATION_FAILED = "Validation Failed";
	public static final String MINIMUM_FIRST_NAME_FAILED = "First name must be at least 2 characters";
	public static final String MAX_FIRST_NAME_FAILED = "First name must not be more than 100 characters";
	public static final String MINIMUM_LAST_NAME_FAILED = "Last name must be at least 2 characters";
	public static final String MAX_LAST_NAME_FAILED = "Last name must not be more than 100 characters";
	public static final String INVALID_ZIP_CODE = "Invalid Zipcode";
	public static final String INVALID_PHONE_NUM = "Invalid Phone Number.  Phone number format should be 555-555-5555";

	public static final String MIN_STREET_NAME = "Street name must be at least 2 characters";
	public static final String MAX_STREET_NAME = "Street name must not be more than 100 characters";

	public static final String MAX_CITY_NAME = "City name must not be more than 100 characters";
	public static final String MIN_CITY_NAME = "City name must be at least 2 characters";

	public static final String MAX_MIN_STATE_CODE = "State code must be 2 characters";
}
