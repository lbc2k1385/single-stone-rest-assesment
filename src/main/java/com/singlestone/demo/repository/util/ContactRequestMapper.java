package com.singlestone.demo.repository.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneRequestDTO;
import com.singlestone.demo.model.RequestAddressDTO;

/**
 * Will serve as a utility class to transform Contact objects to ContractRequest objects.
 * 
 * @author Lucas Coffey
 *
 */
@Component
public class ContactRequestMapper {

	
	/**
	 * Utility method used to transfer ContactRequest to a Contact entity.
	 * If a null pointer is detected that a empty Contact entity will be returned.
	 * @param contactRequest DTO used to represent Contact information.
	 * @return A contact entity based on the contactRequest data.
	 */
	public Contact mapToContact(ContactRequest contactRequest) {

		List<Phone> returnPhones = new ArrayList<>();
		
		if(contactRequest != null && contactRequest.getAddress() !=null && !CollectionUtils.isEmpty(contactRequest.getPhone())) {
			
			for (PhoneRequestDTO contactRequestPhone : contactRequest.getPhone()) {
				Phone phone = new Phone();
				phone.setNumber(contactRequestPhone.getNumber());
				phone.setType(contactRequestPhone.getType());
				returnPhones.add(phone);
			}

			AddressId returnAddressId = new AddressId(contactRequest.getAddress().getStreet(),
					contactRequest.getAddress().getCity(), contactRequest.getAddress().getState(),
					contactRequest.getAddress().getZip());

			Address returnAddress = new Address(returnAddressId);

			Contact returnContact = new Contact(contactRequest.getName(), returnAddress, returnPhones, contactRequest.getEmail());

			returnPhones.forEach(p -> p.setContact(returnContact));

			return returnContact;
			
		}
		
		return new Contact();

	}

	/**
	 * Utility method used to transfer Contact entity data to ContactRequest DTO
	 * If a null pointer is detected that a empty ContactRequest entity will be returned.
	 * @param contact An entity that is used to transfer data to a ContactRequest DTO
	 * @return A DTO that is used to represent contact data.
	 */
	public ContactRequest mapToContactRequest(Contact contact) {
		
		ContactRequest returnContactRequest = new ContactRequest();
		
		if(contact != null && contact.getAddress() != null && contact.getAddress().getAddressId() != null && !CollectionUtils.isEmpty(contact.getPhone())) {
			
			List<PhoneRequestDTO> returnPhoneRequestDTOs = new ArrayList<>();

			for (Phone p : contact.getPhone()) {
				PhoneRequestDTO phone = new PhoneRequestDTO(p.getNumber(), p.getType());
				returnPhoneRequestDTOs.add(phone);
			}

			RequestAddressDTO returnRequestAddressDTO = new RequestAddressDTO(contact.getAddress().getAddressId().getStreet(),
					contact.getAddress().getAddressId().getCity(), contact.getAddress().getAddressId().getState(),
					contact.getAddress().getAddressId().getZip());

		   returnContactRequest = new ContactRequest(contact.getName(), returnRequestAddressDTO, returnPhoneRequestDTOs,
					contact.getEmail());

			return returnContactRequest;
		}
		
		return returnContactRequest;

		

	}

	/**
	 * Utility method used to transfer a list of contact entities to a list of ContactRequest DTOs
	 * If a null pointer is detected than a empty List<ContactRequest> entity will be returned.
	 * @param contacts A List of contact entities that is used to transfer data to a list of ContactRequest DTOs
	 * @return A list of ContactRequest DTOs that are used to represent contact data.
	 */
	public List<ContactRequest> mapToContactRequest(List<Contact> contacts) {

		List<ContactRequest> returnContactRequests = new ArrayList<>();

		for (Contact contact : contacts) {
			
			if(contact != null && contact.getAddress() != null && contact.getAddress().getAddressId() != null&& !CollectionUtils.isEmpty(contact.getPhone())) {
				
				List<PhoneRequestDTO> returnPhoneRequestDTOs = new ArrayList<>();
	
				for (Phone p : contact.getPhone()) {
					PhoneRequestDTO phone = new PhoneRequestDTO(p.getNumber(), p.getType());
					returnPhoneRequestDTOs.add(phone);
				}
	
				RequestAddressDTO returnRequestAddressDTO = new RequestAddressDTO(contact.getAddress().getAddressId().getStreet(),
						contact.getAddress().getAddressId().getCity(), contact.getAddress().getAddressId().getState(),
						contact.getAddress().getAddressId().getZip());
	
				ContactRequest returnContactRequest = new ContactRequest(contact.getName(), returnRequestAddressDTO, returnPhoneRequestDTOs,
						contact.getEmail());
	
				returnContactRequests.add(returnContactRequest);
			}

		}

		return returnContactRequests;

	}

}
