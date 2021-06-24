package com.singlestone.demo.resource.exceptions;

import java.util.Date;

/**
 * POJO used to provide a standard format for exception responses.
 * 
 * @author Lucas Coffey
 *
 */
public class ExceptionResponse {

	private Date dateTime;
	private String message;
	private String details;

	public ExceptionResponse(Date dateTime, String message, String details) {
		super();
		this.dateTime = dateTime;
		this.message = message;
		this.details = details;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
