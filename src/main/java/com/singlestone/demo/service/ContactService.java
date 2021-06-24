package com.singlestone.demo.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.CallListContact;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.ContactResponse;
import com.singlestone.demo.model.Name;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneId;
import com.singlestone.demo.model.PhoneType;
import com.singlestone.demo.repository.AddressRepository;
import com.singlestone.demo.repository.ContactRepository;
import com.singlestone.demo.repository.NameRepository;
import com.singlestone.demo.repository.util.ContactRequestMapper;
import com.singlestone.demo.repository.util.ResourceUtil;
import com.singlestone.demo.resource.exceptions.ContactNotFoundException;
import com.singlestone.demo.resource.exceptions.ContactNotSavedException;

/**
 * This class serves as the main business logic layer of the application.
 * It interacts with both the end point and data layer of the app.
 * 
 * @author Lucas Coffey
 *
 */
@Component
public class ContactService {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ResourceUtil resourceUtil;
	
	@Autowired
	private ContactRequestMapper contactRequestMapper;
	
	@Autowired 
	private NameRepository nameRepository;
	
	
	/**
	 * Will handle contact creation.
	 * 
	 * @param contactRequest Holds data from the client request.
	 * @return ResponseEntity<> that returns the resource.
	 */
	public ResponseEntity<?> handleContactCreation(ContactRequest contactRequest) {
		
		if(contactRequest == null || contactRequest.getAddress() == null) {
			throw new ContactNotSavedException("Could not create contact.");
		}
		
		//Convert object from ContactRequest to Contact 
		Optional<Contact> optionalContact =  contactRequestMapper.mapToContact(contactRequest);
		
		if(optionalContact.isPresent()) {
			
			/*
			 * It is necessary to save here so you can place this entity in the Address foreign key.
			 * The contact entity id part of the primary key in address entity. Without contact
			 * the address insert will fail.
			 */
			Contact returnContact = contactRepository.save(optionalContact.get());
			
			if (returnContact == null)
				throw new ContactNotSavedException("Could not create contact");
			
			AddressId returnAddressId = new AddressId(contactRequest.getAddress().getStreet(),
					contactRequest.getAddress().getCity(), contactRequest.getAddress().getState(),
					contactRequest.getAddress().getZip());
	
			Address returnAddress = new Address(returnAddressId);
			returnAddress.setAddressId(returnAddressId);
			returnAddressId.setContact(returnContact);
			
			returnContact.setAddress(returnAddress);
			
			contactRepository.save(returnContact);
	
			URI resourceURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(returnContact.getId()).toUri();

		return ResponseEntity.created(resourceURI).build();

		}
		
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	
	/**
	 * Will return all contacts in the database.
	 * 
	 * @return ResponseEntity<CollectionModel<EntityModel<ContactResponse>>> that represents the resource.
	 */
	public ResponseEntity<CollectionModel<EntityModel<ContactResponse>>> handleGetContacts() {

		List<Contact> foundContacts = contactRepository.findAll();

		if (foundContacts.isEmpty())
			throw new ContactNotFoundException("No contacts found");

		List<EntityModel<ContactResponse>> entityModels = resourceUtil.createHATEOASLinks(foundContacts);
		
		return ResponseEntity.ok(CollectionModel.of(entityModels));
	}
	
	
	/**
	 * Will return a single contact by the id provided.
	 * 
	 * @param id Represents the primary key of the resource.
	 * @return EntityModel<ContactRequest>  that represent the resource.
	 */
	public EntityModel<ContactRequest> handleGetContact(int id) {

		Optional<Contact> optionalContact = contactRepository.findById(id);

		if (optionalContact.isEmpty())
			throw new ContactNotFoundException(String.format("Contact not found", id));

		EntityModel<ContactRequest> model = resourceUtil.createHATEOASLinks(optionalContact.get());

		return model;
	}
	
	/**
	 * Will return a list of every contact and their home phone number in the system.
	 * 
	 * @return List<CallListContact> that represent the resource.
	 */
	public List<CallListContact> handleGetCallList(){
		
		List<CallListContact> callListContacts = new ArrayList<>();

		List<Contact> foundContacts = contactRepository.findAll();
		
		if (foundContacts.isEmpty())
			throw new ContactNotFoundException("No contacts found");

		for (Contact contact : foundContacts) {
			
			if(contact.getName() == null || CollectionUtils.isEmpty(contact.getPhone()))
				throw new ContactNotFoundException("No contacts found");

			//Filter out all phones accept for the home phone
			Optional<Phone> optionPhone = contact.getPhone()
					.stream()
					.filter(p -> !p.getNumber().equals(PhoneType.home.toString())).findFirst();

			if (optionPhone.isPresent()) {
				
				Name name = new Name(contact.getName().getFirst(), contact.getName().getLast(), contact, contact.getName().getMiddle());
				CallListContact callListContact = new CallListContact(name, optionPhone.get().getNumber());
				callListContacts.add(callListContact);
				
			}else {
				throw new ContactNotFoundException("No contacts found");
			}
			
		}
		
		
		//Uses the compare by method referenced in CallListContact to sort by last name,first name.
		Collections.sort(callListContacts); 
		
		return callListContacts;
		
	}
	
	/**
	 * Will delete a contact based on the primary key represent as the id parameter.
	 * 
	 * @param id Primary Key of the resource to delete
	 * @return ResponseEntity<Contact> with a No Content 204 status
	 */
	public ResponseEntity<Contact> handleDelete(int id) {
		
		Optional<Contact> optionContact = contactRepository.findById(id);
		
		if(optionContact.isEmpty()) {
			throw new ContactNotFoundException(String.format("Contact id: %d not found ", id));
		}
		
		Contact contactToDelete = optionContact.get();
		
		/*
		 * Completely Detach Address entity.
		 * If entity is not removed the contact entity delete will fail.
		 */
		Address addressToBeDeleted = contactToDelete.getAddress();
		contactToDelete.setAddress(null);
		
		addressRepository.deleteById(addressToBeDeleted.getAddressId());			
		contactRepository.delete(contactToDelete);

		return new ResponseEntity<Contact>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Will update contact based on the id and contactRequst being passed by the client.
	 * 
	 * @param contactRequest A request object that holds contact data.
	 * @param id Primary key of the resource to be deleted.
	 * @return ResponseEntity<Contact> with a No Content 204 status
	 */
	public ResponseEntity<Contact> handleUpdateContact(ContactRequest contactRequest, int id) {
		
		//Find the resource you need to delete
		Optional<Contact> optionContact = contactRepository.findById(id);
		
		if(optionContact.isEmpty() || contactRequest == null || contactRequest.getAddress() == null 
			|| CollectionUtils.isEmpty(contactRequest.getPhone())) {
			throw new ContactNotFoundException(String.format("Contact id: %d not found ", id));
		}
		
		Contact contactToUpdate = optionContact.get();
		
		AddressId addressIdToUpdate = contactToUpdate.getAddress().getAddressId();
		
		/*
		 * Remove the foreign key references so the can be updated with new values.
		 * This is necessary because these entities are foreign keys in the contact 
		 * entity, and because the contact id is used as a composite key for both
		 * the Address and Phone entity. This seems like the CASCADE.ALL annotation
		 * on the Contact entity would handle this issue, however this did not seem
		 * to be the case. Therefore, I just manually remove them.
		 */
		contactToUpdate.getPhone().clear();
		contactToUpdate.setAddress(null);
			
		Optional<Address> optAddressToDelete = addressRepository.findById(addressIdToUpdate);
				
		if(optAddressToDelete.isPresent())
			//It is necessary to remove this due to the comment above referring to foreign keys
			addressRepository.deleteById(optAddressToDelete.get().getAddressId());
		else 
			throw new ContactNotSavedException("Address could not be updated");
		
		Address addressToUpdate = new Address();
		
		addressIdToUpdate.setCity(contactRequest.getAddress().getCity());
		addressIdToUpdate.setState(contactRequest.getAddress().getState());
		addressIdToUpdate.setStreet(contactRequest.getAddress().getStreet());
		addressIdToUpdate.setZip(contactRequest.getAddress().getZip());
		contactToUpdate.setAddress(addressToUpdate);
		addressIdToUpdate.setContact(contactToUpdate);
		addressToUpdate.setAddressId(addressIdToUpdate);
			
		contactToUpdate.setAddress(addressToUpdate);
			
		List<Phone> phonesToUpdate = contactToUpdate.getPhone();
		
		for(int i = 0; i < contactRequest.getPhone().size(); i++) {
			Phone phone = new Phone();
			PhoneId phoneId = new PhoneId();
			phoneId.setType(contactRequest.getPhone().get(i).getType());
			phoneId.setContact(contactToUpdate);
			phone.setNumber(contactRequest.getPhone().get(i).getNumber());
			phone.setPhoneId(phoneId);
			phonesToUpdate.add(phone);
		}
				
		Name name = contactToUpdate.getName();
		name.setFirst(contactRequest.getName().getFirst());
		name.setLast(contactRequest.getName().getLast());
		name.setMiddle(contactRequest.getName().getMiddle());
		contactToUpdate.setName(name);
			
		contactToUpdate.setEmail(contactRequest.getEmail());
		contactToUpdate.setPhone(phonesToUpdate);
		addressIdToUpdate.setContact(contactToUpdate);
		
		contactRepository.save(contactToUpdate);

		return new ResponseEntity<Contact>(HttpStatus.NO_CONTENT);
	}
	
	
	
	public ContactRepository getContactRepository() {
		return contactRepository;
	}

	public void setContactRepository(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	public AddressRepository getAddressRepository() {
		return addressRepository;
	}

	public void setAddressRepository(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	public ResourceUtil getResourceUtil() {
		return resourceUtil;
	}

	public void setResourceUtil(ResourceUtil resourceUtil) {
		this.resourceUtil = resourceUtil;
	}

	public ContactRequestMapper getContactRequestMapper() {
		return contactRequestMapper;
	}

	public void setContactRequestMapper(ContactRequestMapper contactRequestMapper) {
		this.contactRequestMapper = contactRequestMapper;
	}

	public NameRepository getNameRepository() {
		return nameRepository;
	}

	public void setNameRepository(NameRepository nameRepository) {
		this.nameRepository = nameRepository;
	}
	
	
	

}
