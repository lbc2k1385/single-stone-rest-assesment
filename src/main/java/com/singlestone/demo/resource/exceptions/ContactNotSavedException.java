package com.singlestone.demo.resource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a contact can not be saved.
 * 
 * @author Lucas Coffey
 *
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ContactNotSavedException extends RuntimeException {

	private static final long serialVersionUID = 4069633154488204063L;

	public ContactNotSavedException(String message) {
		super(message);
	}

}
