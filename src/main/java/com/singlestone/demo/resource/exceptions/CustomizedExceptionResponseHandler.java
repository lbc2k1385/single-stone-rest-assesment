package com.singlestone.demo.resource.exceptions;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Used as a exception mapper to throw custom exceptions with a standard response format.
 * This mapper is enabled through the spring @ControllerAdvice.
 * 
 * @author Lucas Coffey
 *
 */
@RestController
@ControllerAdvice
public class CustomizedExceptionResponseHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Exception {

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(ContactNotFoundException.class)
	public final ResponseEntity<Object> handleContactNotFoundException(ContactNotFoundException ex, WebRequest request)
			throws Exception {

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(ContactNotSavedException.class)
	public final ResponseEntity<Object> handleContactNotSavedException(ContactNotFoundException ex, WebRequest request)
			throws Exception {

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	

	/**
	 * Used to capture exception causes for JPA constraint errors.
	 * 
	 * @param exception A TransactionSystemException that is thrown during JPA constrain errors.
	 * @return A ResponseEntity to return details related to the ConstraintViolationException violation.
	 */
	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<Object> handleTransactionException(TransactionSystemException exception) {

		Throwable exceptionCause = exception.getMostSpecificCause();

		if (exceptionCause instanceof ConstraintViolationException) {

			StringBuilder sb = new StringBuilder("Constraint errors are: ");
			Set<ConstraintViolation<?>> set = ((ConstraintViolationException) exceptionCause).getConstraintViolations();

			for (ConstraintViolation<?> violation : set) {
				sb.append(violation.getMessage() + " ");
			}

			ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
					ExceptionMessageConstants.VALIDATION_FAILED, sb.toString());

			return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
		}

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				ExceptionMessageConstants.VALIDATION_FAILED, exception.getMessage());

		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				ExceptionMessageConstants.VALIDATION_FAILED, ex.getBindingResult().toString());

		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}

}
