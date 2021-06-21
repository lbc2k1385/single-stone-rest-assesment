package com.singlestone.demo.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.singlestone.demo.model.CallListContact;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.Name;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneType;
import com.singlestone.demo.repository.ContactRepository;
import com.singlestone.demo.repository.util.ContactRequestMapper;
import com.singlestone.demo.repository.util.ResourceUtil;
import com.singlestone.demo.resource.exceptions.ContactNotFoundException;
import com.singlestone.demo.resource.exceptions.ContactNotSavedException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/v1/contacts")
public class ContactResource {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ResourceUtil resourceUtil;
	
	@Autowired
	private ContactRequestMapper contactRequestMapper;

	@GetMapping
	@Operation(summary = "Retrieve all contacts.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found all contacts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "404", description = "No contacts found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "Something went wrong while retrieveing contacts", content = {
					@Content(mediaType = "application/json") }) })
	public ResponseEntity<CollectionModel<EntityModel<Contact>>> getContacts() {

		List<Contact> foundContacts = contactRepository.findAll();

		if (foundContacts.isEmpty())
			throw new ContactNotFoundException("No contacts found");

		List<EntityModel<Contact>> entityModels = resourceUtil.createHATEOASLinks(foundContacts);
		
		return ResponseEntity.ok(CollectionModel.of(entityModels));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Retrieve contact by id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Succesffuly retrieved contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "404", description = "Contact not found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "Something went wrong while retrieveing contact", content = {
					@Content(mediaType = "application/json") }) })
	public EntityModel<Contact> getContact(@PathVariable int id) {

		Optional<Contact> optionalContact = contactRepository.findById(id);

		if (!optionalContact.isPresent())
			throw new ContactNotFoundException(String.format("Contact not found", id));

		EntityModel<Contact> model = resourceUtil.createHATEOASLinks(optionalContact.get());

		return model;
	}

	@GetMapping("/call-list")
	@Operation(summary = "Get call list for all contacts.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Succesffuly retrieved call list", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "404", description = "Call list not found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "Something went wrong while retrieveing call list", content = {
					@Content(mediaType = "application/json") }) })
	public List<CallListContact> getCallList() {

		List<CallListContact> callListContacts = new ArrayList<>();

		List<Contact> contacts = contactRepository.findAll();
		
		if (contacts.isEmpty())
			throw new ContactNotFoundException("No contacts found");

		for (Contact contact : contacts) {

			if(contact.getName() != null && !CollectionUtils.isEmpty(contact.getPhone()) ) {
				
				Optional<Phone> optPhone = contact.getPhone().stream()
						.filter(p -> !p.getNumber().equals(PhoneType.home.toString())).findFirst();

				if (optPhone.isPresent()) {
					
					Name name = new Name(contact.getName().getFirst(), contact.getName().getLast(), contact,
							contact.getName().getMiddle());
					
					CallListContact callListContact = new CallListContact(name, optPhone.get().getNumber());
					callListContacts.add(callListContact);
				}
			}
			
			

		}

		return callListContacts;
	}

	@PostMapping
	@ResponseBody
	@Operation(summary = "Create a new contact.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Succesffuly created contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "500", description = "Could not create contact", content = {
					@Content(mediaType = "application/json") }) })
	public ResponseEntity<?> createContact(@RequestBody ContactRequest contactRequest) {

		Contact contact =  contactRequestMapper.mapToContact(contactRequest);
		
		if(contact == null || contact.getId() == 0) {
			throw new ContactNotSavedException("Could not create contact.");
		}
		
		Contact newContact = contactRepository.save(contact);

		if (newContact == null)
			throw new ContactNotSavedException("Could not create contact");

		URI resourceURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newContact.getId()).toUri();

		return ResponseEntity.created(resourceURI).build();

	}

	@DeleteMapping("/{id}")
	@PostMapping
	@ResponseBody
	@Operation(summary = "Delete a contact by id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Succesffuly deleted contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "404", description = "Contact not found.", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "Something went wrong while deleting contact", content = {
					@Content(mediaType = "application/json") }) })
	public ResponseEntity<Contact> deleteContact(@PathVariable("id") int id) {

		if (!contactRepository.existsById(id)) {
			throw new ContactNotFoundException(String.format("Contact not found $d", id));
		}

		contactRepository.deleteById(id);

		return new ResponseEntity<Contact>(HttpStatus.NO_CONTENT);

	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Update a contact.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Succesffuly updated contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "500", description = "Could not update contact", content = {
					@Content(mediaType = "application/json") }) })
	public ResponseEntity<Contact> updateContact(@RequestBody Contact contact, @PathVariable("id") int id) {

		Contact contactToUpdate = contactRepository.getOne(id);
		
		if (contactToUpdate == null) {
			throw new ContactNotFoundException(String.format("Contact not found $d", id));
		}
	
		if(contact == null || contact.getAddress() == null && CollectionUtils.isEmpty(contact.getPhone())) {
			throw new ContactNotFoundException(String.format("Contact not found $d", id));
		}
		
		contact.setId(id);
		contact.getAddress().setAddressId(contactToUpdate.getAddress().getAddressId());
		contact.getPhone().stream().forEach(phone -> phone.setContact(contactToUpdate));

		contactRepository.save(contact);

		return new ResponseEntity<Contact>(HttpStatus.NO_CONTENT);
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
	
	public ContactRepository getContactRepository() {
		return contactRepository;
	}

	public void setContactRepository(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

}
