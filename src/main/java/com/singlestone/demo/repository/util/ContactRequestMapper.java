package com.singlestone.demo.repository.util;

import java.util.ArrayList;
import java.util.List;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneRequestDTO;
import com.singlestone.demo.model.RequestAddressDTO;

public class ContactRequestMapper {

	public Contact mapToContact(ContactRequest contactRequest) {

		List<Phone> phones = new ArrayList<>();

		for (PhoneRequestDTO p : contactRequest.getPhone()) {
			Phone phone = new Phone();
			phone.setNumber(p.getNumber());
			phone.setType(p.getType());
			phones.add(phone);
		}

		AddressId addressId = new AddressId(contactRequest.getAddress().getStreet(),
				contactRequest.getAddress().getCity(), contactRequest.getAddress().getState(),
				contactRequest.getAddress().getZip());

		Address address = new Address(addressId);

		Contact contact = new Contact(contactRequest.getName(), address, phones, contactRequest.getEmail());

		phones.forEach(p -> p.setContact(contact));

		return contact;

	}

	public ContactRequest mapToContactRequest(Contact contact) {

		List<PhoneRequestDTO> phoneRequestDTOs = new ArrayList<>();

		for (Phone p : contact.getPhone()) {
			PhoneRequestDTO phone = new PhoneRequestDTO(p.getNumber(), p.getType());
			phoneRequestDTOs.add(phone);
		}

		RequestAddressDTO requestAddressDTO = new RequestAddressDTO(contact.getAddress().getAddressId().getStreet(),
				contact.getAddress().getAddressId().getCity(), contact.getAddress().getAddressId().getState(),
				contact.getAddress().getAddressId().getZip());

		ContactRequest contactRequest = new ContactRequest(contact.getName(), requestAddressDTO, phoneRequestDTOs,
				contact.getEmail());

		return contactRequest;

	}

	public List<ContactRequest> mapToContactRequest(List<Contact> contacts) {

		List<ContactRequest> contactRequests = new ArrayList<>();

		for (Contact contact : contacts) {

			List<PhoneRequestDTO> phoneRequestDTOs = new ArrayList<>();

			for (Phone p : contact.getPhone()) {
				PhoneRequestDTO phone = new PhoneRequestDTO(p.getNumber(), p.getType());
				phoneRequestDTOs.add(phone);
			}

			RequestAddressDTO requestAddressDTO = new RequestAddressDTO(contact.getAddress().getAddressId().getStreet(),
					contact.getAddress().getAddressId().getCity(), contact.getAddress().getAddressId().getState(),
					contact.getAddress().getAddressId().getZip());

			ContactRequest contactRequest = new ContactRequest(contact.getName(), requestAddressDTO, phoneRequestDTOs,
					contact.getEmail());

			contactRequests.add(contactRequest);

		}

		return contactRequests;

	}

}
