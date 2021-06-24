package com.singlestone.demo.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.singlestone.demo.model.CallListContact;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.ContactResponse;
import com.singlestone.demo.repository.AddressRepository;
import com.singlestone.demo.repository.ContactRepository;
import com.singlestone.demo.repository.NameRepository;
import com.singlestone.demo.repository.util.ContactRequestMapper;
import com.singlestone.demo.repository.util.ResourceUtil;
import com.singlestone.demo.service.ContactService;

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
	private AddressRepository addressRepository;

	@Autowired
	private ResourceUtil resourceUtil;
	
	@Autowired
	private ContactRequestMapper contactRequestMapper;
	
	@Autowired 
	private NameRepository nameRepository;
	
	@Autowired
	private ContactService contactService;

	@GetMapping
	@Operation(summary = "Retrieve all contacts.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found all contacts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "404", description = "No contacts found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "Something went wrong while retrieveing contacts", content = {
					@Content(mediaType = "application/json") }) })
	public ResponseEntity<CollectionModel<EntityModel<ContactResponse>>> getContacts() {
		return contactService.handleGetContacts();
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
	public EntityModel<ContactRequest> getContact(@PathVariable int id) {
		return contactService.handleGetContact(id);
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
		return contactService.handleGetCallList();
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
		return contactService.handleContactCreation(contactRequest);
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
		return contactService.handleDelete(id);
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Update a contact.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Succesffuly updated contact", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Contact.class)) }),
			@ApiResponse(responseCode = "500", description = "Could not update contact", content = {
					@Content(mediaType = "application/json") }) })
	public ResponseEntity<Contact> updateContact(@RequestBody ContactRequest contactRequest, @PathVariable("id") int id) {
		return contactService.handleUpdateContact(contactRequest,id);
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

	public AddressRepository getAddressRepository() {
		return addressRepository;
	}

	public void setAddressRepository(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	public NameRepository getNameRepository() {
		return nameRepository;
	}

	public void setNameRepository(NameRepository nameRepository) {
		this.nameRepository = nameRepository;
	}

	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

}
