package com.singlestone.demo.repository.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.ContactResponse;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneId;
import com.singlestone.demo.model.PhoneRequestDTO;
import com.singlestone.demo.model.RequestAddressDTO;


/**
 * Will serve as a utility class to transform Contact data between objects.
 * 
 * @author Lucas Coffey
 *
 */
@Component
public class ContactRequestMapper {

	
	/**
	 * Utility method used to transfer ContactRequest to a Contact entity.
	 * If a null pointer is detected that an empty Contact entity will be returned.
	 * 
	 * @param contactRequest DTO used to represent Contact information.
	 * @return A contact entity based on the contactRequest data.
	 */
	public Optional<Contact>  mapToContact(ContactRequest contactRequest) {
		
		Optional<Contact> optionalContact = Optional.empty();

		List<Phone> returnPhones = new ArrayList<>();
		
		if(contactRequest == null || contactRequest.getAddress() ==null || CollectionUtils.isEmpty(contactRequest.getPhone())) 
			return optionalContact;
			
		for (PhoneRequestDTO contactRequestPhone : contactRequest.getPhone()) {
			
			Phone phone = new Phone();
			phone.setNumber(contactRequestPhone.getNumber());
			PhoneId phoneId = new PhoneId();
			phoneId.setType(contactRequestPhone.getType());
			phone.setPhoneId(phoneId);
			returnPhones.add(phone);
		}
		
		Address address = new Address();
		AddressId addressId = new AddressId();
		
		addressId.setCity(contactRequest.getAddress().getCity());
		addressId.setState(contactRequest.getAddress().getState());
		addressId.setStreet(contactRequest.getAddress().getStreet());
		addressId.setZip(contactRequest.getAddress().getZip());
		address.setAddressId(addressId);
		

		Contact returnContact = new Contact(contactRequest.getName(), null, returnPhones, contactRequest.getEmail());
		//addressId.setContact(returnContact);
		//returnContact.setAddress(address);
		
		returnPhones.forEach(p -> p.getPhoneId().setContact(returnContact));
		
		optionalContact = Optional.of(returnContact);

		return optionalContact;


	}

	
	/**
	 * Utility method used to transfer Contact entity data to a ContactRequest.
	 * If a null pointer is detected that an empty ContactRequest entity will be returned.
	 * 
	 * @param contact An entity that is used to transfer data to a ContactRequest DTO
	 * @return A DTO that is used to represent contact data.
	 */
	public Optional<ContactRequest> mapToContactRequest(Contact contact) {
		
		Optional<ContactRequest> optionalreturnContactRequest = Optional.empty();
			
		if(contact == null || contact.getAddress() ==null || CollectionUtils.isEmpty(contact.getPhone())) 
			return optionalreturnContactRequest;
			
		List<PhoneRequestDTO> returnPhoneRequestDTOs = new ArrayList<>();

		for (Phone p : contact.getPhone()) {
			PhoneRequestDTO phone = new PhoneRequestDTO(p.getNumber(), p.getPhoneId().getType());
			returnPhoneRequestDTOs.add(phone);
		}

		RequestAddressDTO returnRequestAddressDTO = new RequestAddressDTO(contact.getAddress().getAddressId().getStreet(),
				contact.getAddress().getAddressId().getCity(), contact.getAddress().getAddressId().getState(),
				contact.getAddress().getAddressId().getZip());

		ContactRequest returnContactRequest = new ContactRequest(contact.getName(), returnRequestAddressDTO, returnPhoneRequestDTOs,
			   contact.getEmail());
	   
	   optionalreturnContactRequest = Optional.of(returnContactRequest);

		return 	optionalreturnContactRequest;

	}

	
	/**
	 * Utility method used to transfer a contact entities to a ContactResponse.
	 * If a null pointer is detected than an empty Contact entity will be returned.
	 * 
	 * @param contact
	 * @return
	 */
	public Optional<ContactResponse> mapToContactResponse(Contact contact) {
		
		Optional<ContactResponse> optionalContactResponse = Optional.empty();
		
		if(contact == null || contact.getAddress() ==null || CollectionUtils.isEmpty(contact.getPhone())) 
			return optionalContactResponse;
			
		List<PhoneRequestDTO> returnPhoneRequestDTOs = new ArrayList<>();

		for (Phone p : contact.getPhone()) {
			PhoneRequestDTO phone = new PhoneRequestDTO(p.getNumber(), p.getPhoneId().getType());
			returnPhoneRequestDTOs.add(phone);
		}

		RequestAddressDTO returnRequestAddressDTO = new RequestAddressDTO(contact.getAddress().getAddressId().getStreet(),
				contact.getAddress().getAddressId().getCity(), contact.getAddress().getAddressId().getState(),
				contact.getAddress().getAddressId().getZip());

		ContactResponse returnContactRequest = new ContactResponse(contact.getName(), returnPhoneRequestDTOs,  returnRequestAddressDTO, contact.getEmail());
	   
		optionalContactResponse = Optional.of(returnContactRequest);
	   
	   return optionalContactResponse;		

	} 

}
