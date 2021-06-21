package com.singlestone.demo.repository.util;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
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
	 * Will assigned HATEOAS links to a single EntityModel<Contact>.
	 * 
	 * @param contact An entity object that represents the resource to be returned.
	 * @return EntityModel<Contact> that will represent the resource to be returned with HATEOAS links
	 */
	public EntityModel<Contact> createHATEOASLinks(Contact contact) {
		
		ContactRequest contactRequest = new ContactRequestMapper().mapToContactRequest(contact);
		EntityModel<Contact> model = EntityModel.of(contact);
		
		if(contact != null && contactRequest != null) {
			
			WebMvcLinkBuilder linkToContacts = linkTo(methodOn(ContactResource.class).getContacts());
			model.add(linkToContacts.withRel("all-contacts"));

			linkToContacts = linkTo(methodOn(ContactResource.class).getContact(contact.getId()));
			model.add(linkToContacts.withRel("get-contact"));

			linkToContacts = linkTo(methodOn(ContactResource.class).createContact(contactRequest));
			model.add(linkToContacts.withRel("create-contact"));

			linkToContacts = linkTo(methodOn(ContactResource.class).deleteContact(contact.getId()));
			model.add(linkToContacts.withRel("delete-contact"));
		}

		return model;
	}
	
	
	/**
	 * Will assigned HATEOAS links to multiple EntityModels<Contact>.
	 * 
	 * @param contacts A list of entity objects that represents the resources to be returned.
	 * @return List<EntityModel<Contact>> that will represent the resource to be returned with HATEOAS links
	 */
	public List<EntityModel<Contact>> createHATEOASLinks(List<Contact> contacts) {

		List<EntityModel<Contact>> entityModelContacts = new ArrayList<>();
		
		for (Contact contact : contacts) {
		
			ContactRequest contactRequest = new ContactRequestMapper().mapToContactRequest(contact);
			
			if(contact != null && contactRequest != null) {
				
				EntityModel<Contact> model = EntityModel.of(contact);

				WebMvcLinkBuilder linkToContacts = linkTo(methodOn(ContactResource.class).getContacts());
				model.add(linkToContacts.withRel("all-contacts"));

				linkToContacts = linkTo(methodOn(ContactResource.class).getContact(contact.getId()));
				model.add(linkToContacts.withRel("get-contact"));

				linkToContacts = linkTo(methodOn(ContactResource.class).createContact(contactRequest));
				model.add(linkToContacts.withRel("create-contact"));

				linkToContacts = linkTo(methodOn(ContactResource.class).deleteContact(contact.getId()));
				model.add(linkToContacts.withRel("delete-contact"));

				entityModelContacts.add(model);
			}

			
		}

		return entityModelContacts;
	}

}
