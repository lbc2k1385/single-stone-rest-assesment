package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.singlestone.demo.resource.exceptions.ExceptionMessageConstants;

@Entity
public class Name implements Serializable {

	private static final long serialVersionUID = -2927058725783429667L;

	@Positive
	@Id
	@GeneratedValue
	@JsonIgnore
	private int id;

	@Size(min = 2, message = ExceptionMessageConstants.MINIMUM_FIRST_NAME_FAILED)
	@Size(max = 100, message = ExceptionMessageConstants.MAX_FIRST_NAME_FAILED)
	@NotNull
	private String first;

	@Size(min = 2, message = ExceptionMessageConstants.MINIMUM_LAST_NAME_FAILED)
	@Size(max = 100, message = ExceptionMessageConstants.MAX_LAST_NAME_FAILED)
	@NotNull
	private String last;

	private String middle;

	public Name() {
	}

	public Name(
			@Size(min = 2, message = "First name must be at least 2 characters") @Size(max = 100, message = "First name must not be more than 100 characters") @NotNull String first,
			@Size(min = 2, message = "Last name must be at least 2 characters") @Size(max = 100, message = "Last name must not be more than 100 characters") @NotNull String last,
			Contact contact, String middle) {
		super();
		this.first = first;
		this.last = last;
		this.middle = middle;
	}
	
	public Name(
			@Size(min = 2, message = "First name must be at least 2 characters") @Size(max = 100, message = "First name must not be more than 100 characters") @NotNull String first,
			@Size(min = 2, message = "Last name must be at least 2 characters") @Size(max = 100, message = "Last name must not be more than 100 characters") @NotNull String last,String middle) {
		super();
		this.first = first;
		this.last = last;
		this.middle = middle;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getMiddle() {
		return middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + id;
		result = prime * result + ((last == null) ? 0 : last.hashCode());
		result = prime * result + ((middle == null) ? 0 : middle.hashCode());
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
		Name other = (Name) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (id != other.id)
			return false;
		if (last == null) {
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		if (middle == null) {
			if (other.middle != null)
				return false;
		} else if (!middle.equals(other.middle))
			return false;
		return true;
	}

}
