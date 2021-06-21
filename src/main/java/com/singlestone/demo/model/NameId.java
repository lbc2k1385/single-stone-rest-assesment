package com.singlestone.demo.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.singlestone.demo.resource.exceptions.ExceptionMessageConstants;

@Embeddable
public class NameId implements Serializable {

	private static final long serialVersionUID = -7155535147463087906L;

	@Size(min = 2, message = ExceptionMessageConstants.MINIMUM_FIRST_NAME_FAILED)
	@Size(max = 100, message = ExceptionMessageConstants.MAX_FIRST_NAME_FAILED)
	@NotNull
	private String first;

	@Size(min = 2, message = ExceptionMessageConstants.MINIMUM_LAST_NAME_FAILED)
	@Size(max = 100, message = ExceptionMessageConstants.MAX_LAST_NAME_FAILED)
	@NotNull
	private String last;

	@OneToOne
	private Contact contact;

	public NameId(
			@Size(min = 2, message = "First name must be at least 2 characters") @Size(max = 100, message = "First name must not be more than 100 characters") @NotNull String first,
			@Size(min = 2, message = "Last name must be at least 2 characters") @Size(max = 100, message = "Last name must not be more than 100 characters") @NotNull String last,
			Contact contact) {
		super();
		this.first = first;
		this.last = last;
		this.contact = contact;
	}

	public NameId(
			@Size(min = 2, message = "First name must be at least 2 characters") @Size(max = 100, message = "First name must not be more than 100 characters") @NotNull String first,
			@Size(min = 2, message = "Last name must be at least 2 characters") @Size(max = 100, message = "Last name must not be more than 100 characters") @NotNull String last) {
		super();
		this.first = first;
		this.last = last;
	}

	public NameId() {
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

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
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
		NameId other = (NameId) obj;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (last == null) {
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		return true;
	}

}
