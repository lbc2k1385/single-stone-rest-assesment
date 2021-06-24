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
	
	
	public ResponseEntity<?> handleContactCreation(ContactRequest contactRequest) {
		
		if(contactRequest == null || contactRequest.getAddress() == null) {
			throw new ContactNotSavedException("Could not create contact.");
		}
		
		
		Optional<Contact> optionalContact =  contactRequestMapper.mapToContact(contactRequest);
		
		if(optionalContact.isPresent()) {
			
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
	
	
	public ResponseEntity<CollectionModel<EntityModel<ContactResponse>>> handleGetContacts() {

		List<Contact> foundContacts = contactRepository.findAll();

		if (foundContacts.isEmpty())
			throw new ContactNotFoundException("No contacts found");

		List<EntityModel<ContactResponse>> entityModels = resourceUtil.createHATEOASLinks(foundContacts);
		
		return ResponseEntity.ok(CollectionModel.of(entityModels));
	}
	
	
	public EntityModel<ContactRequest> handleGetContact(int id) {

		Optional<Contact> optionalContact = contactRepository.findById(id);

		if (optionalContact.isEmpty())
			throw new ContactNotFoundException(String.format("Contact not found", id));

		EntityModel<ContactRequest> model = resourceUtil.createHATEOASLinks(optionalContact.get());

		return model;
	}
	
	public List<CallListContact> handleGetCallList(){
		
		List<CallListContact> callListContacts = new ArrayList<>();

		List<Contact> foundContacts = contactRepository.findAll();
		
		if (foundContacts.isEmpty())
			throw new ContactNotFoundException("No contacts found");

		for (Contact contact : foundContacts) {
			
			if(contact.getName() == null || CollectionUtils.isEmpty(contact.getPhone()))
				throw new ContactNotFoundException("No contacts found");

			Optional<Phone> optionPhone = contact.getPhone()
					.stream()
					.filter(p -> !p.getNumber().equals(PhoneType.home.toString())).findFirst();

			if (optionPhone.isPresent()) {
				
				Name name = new Name(contact.getName().getFirst(), contact.getName().getLast(), contact,
						contact.getName().getMiddle());
				
				CallListContact callListContact = new CallListContact(name, optionPhone.get().getNumber());
				callListContacts.add(callListContact);
				
			}else {
				throw new ContactNotFoundException("No contacts found");
			}
			
		}
		
	
		Collections.sort(callListContacts); 
		
		return callListContacts;
		
	}
	
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
	
	public ResponseEntity<Contact> handleUpdateContact(ContactRequest contactRequest, int id) {
		
		Optional<Contact> optionContact = contactRepository.findById(id);
		
		if(optionContact.isEmpty() || contactRequest == null || contactRequest.getAddress() == null 
			|| CollectionUtils.isEmpty(contactRequest.getPhone())) {
			throw new ContactNotFoundException(String.format("Contact id: %d not found ", id));
		}
		
		Contact contactToUpdate = optionContact.get();
		
		AddressId addressIdToUpdate = contactToUpdate.getAddress().getAddressId();
		
		contactToUpdate.getPhone().clear();
		contactToUpdate.setAddress(null);
				
		Optional<Address> optAddressToDelete = addressRepository.findById(addressIdToUpdate);
				
		if(optAddressToDelete.isPresent())
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
		
		contactToUpdate.setPhone(null);
		
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
