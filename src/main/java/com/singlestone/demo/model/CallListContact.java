package com.singlestone.demo.model;

public class CallListContact implements Comparable<CallListContact> {

	private Name name;
	public String phone;
	

	public CallListContact(Name name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		CallListContact other = (CallListContact) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		return true;
	}
	

	@Override
	public int compareTo(CallListContact callListContact) {
		int last = this.name.getLast().compareTo(callListContact.name.getLast());
	    return last == 0 ? this.name.getFirst().compareTo(callListContact.name.getLast()) : last;
	}

}
