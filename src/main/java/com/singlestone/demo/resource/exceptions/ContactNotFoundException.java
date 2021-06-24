package com.singlestone.demo.resource.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a contact is not found.
 * 
 * @author Lucas Coffey
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContactNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3965071701584114194L;

	public ContactNotFoundException(String message) {
		super(message);
	}

}
