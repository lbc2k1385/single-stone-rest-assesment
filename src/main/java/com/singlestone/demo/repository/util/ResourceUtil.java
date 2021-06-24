package com.singlestone.demo.repository.util;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.ContactResponse;
import com.singlestone.demo.resource.ContactResource;

/**
 * Designed to create HATEOAS links for EntityModels related to operations with the contact resource.
 * 
 * @author Lucas Coffey
 *
 */
@Component
public class ResourceUtil {

	/**
	 * Will assigned HATEOAS links to a single EntityModel<ContactRequest>.
	 * 
	 * @param contact An entity object that represents the resource to be returned.
	 * @return EntityModel<ContactRequest> that will represent the resource to be returned with HATEOAS links
	 */
	public EntityModel<ContactRequest> createHATEOASLinks(Contact contact) {
		
		ContactRequestMapper contactRequestMapper = new ContactRequestMapper();
		
		Optional<ContactRequest> optionalContactRequest = contactRequestMapper.mapToContactRequest(contact);
		
		EntityModel<ContactRequest> model =  EntityModel.of(new ContactRequest());
		
		if(optionalContactRequest.isPresent()) {
			
			ContactRequest contactRequest = optionalContactRequest.get();
			
			model = EntityModel.of(optionalContactRequest.get());
			
			if(contact != null) {
				
				WebMvcLinkBuilder linkToContacts = linkTo(methodOn(ContactResource.class).getContacts());
				model.add(linkToContacts.withRel("all-contacts"));

				linkToContacts = linkTo(methodOn(ContactResource.class).getContact(contact.getId()));
				model.add(linkToContacts.withRel("get-contact"));

				linkToContacts = linkTo(methodOn(ContactResource.class).createContact(contactRequest));
				model.add(linkToContacts.withRel("create-contact"));

				linkToContacts = linkTo(methodOn(ContactResource.class).deleteContact(contact.getId()));
				model.add(linkToContacts.withRel("delete-contact"));
			}
		}
		

		return model;
	}
	
	
	/**
	 * Will assigned HATEOAS links to multiple List<EntityModel<ContactResponse>>.
	 * 
	 * @param contacts A list of entity objects that represents the resources to be returned.
	 * @return List<EntityModel<Contact>> that will represent the resource to be returned with HATEOAS links
	 */
	public List<EntityModel<ContactResponse>> createHATEOASLinks(List<Contact> contacts) {
		
		ContactRequestMapper contactRequestMapper = new ContactRequestMapper();

		List<EntityModel<ContactResponse>> entityModelContacts = new ArrayList<>();
		
		
		for (Contact contact : contacts) {
		
			Optional<ContactResponse> optionalContactResponse = contactRequestMapper.mapToContactResponse(contact);
			Optional<ContactRequest> optionalContactRequest = contactRequestMapper.mapToContactRequest(contact);
			
			if(optionalContactResponse.isPresent() && optionalContactRequest.isPresent()) {
				
				EntityModel<ContactResponse> model = EntityModel.of(optionalContactResponse.get());

				WebMvcLinkBuilder linkToContacts = linkTo(methodOn(ContactResource.class).getContacts());
				model.add(linkToContacts.withRel("all-contacts"));

				linkToContacts = linkTo(methodOn(ContactResource.class).getContact(contact.getId()));
				model.add(linkToContacts.withRel("get-contact"));

				linkToContacts = linkTo(methodOn(ContactResource.class).createContact(optionalContactRequest.get()));
				model.add(linkToContacts.withRel("create-contact"));

				linkToContacts = linkTo(methodOn(ContactResource.class).deleteContact(contact.getId()));
				model.add(linkToContacts.withRel("delete-contact"));

				entityModelContacts.add(model);
			}			
		}

		return entityModelContacts;
	}

}
