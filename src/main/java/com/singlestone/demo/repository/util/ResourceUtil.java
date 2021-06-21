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

@Component
public class ResourceUtil {

	public EntityModel<Contact> createHATEOASLinks(Contact contact, ContactRequest contactRequest) {

		EntityModel<Contact> model = EntityModel.of(contact);

		WebMvcLinkBuilder linkToContacts = linkTo(methodOn(ContactResource.class).getContacts());
		model.add(linkToContacts.withRel("all-contacts"));

		linkToContacts = linkTo(methodOn(ContactResource.class).getContact(contact.getId()));
		model.add(linkToContacts.withRel("get-contact"));

		linkToContacts = linkTo(methodOn(ContactResource.class).createContact(contactRequest));
		model.add(linkToContacts.withRel("create-contact"));

		linkToContacts = linkTo(methodOn(ContactResource.class).deleteContact(contact.getId()));
		model.add(linkToContacts.withRel("delete-contact"));

		return model;
	}

	public List<EntityModel<Contact>> createHATEOASLinks(List<Contact> contacts) {

		List<EntityModel<Contact>> entityModelContacts = new ArrayList<>();

		for (Contact c : contacts) {

			ContactRequest contactRequest = new ContactRequestMapper().mapToContactRequest(c);

			EntityModel<Contact> model = EntityModel.of(c);

			WebMvcLinkBuilder linkToContacts = linkTo(methodOn(ContactResource.class).getContacts());
			model.add(linkToContacts.withRel("all-contacts"));

			linkToContacts = linkTo(methodOn(ContactResource.class).getContact(c.getId()));
			model.add(linkToContacts.withRel("get-contact"));

			linkToContacts = linkTo(methodOn(ContactResource.class).createContact(contactRequest));
			model.add(linkToContacts.withRel("create-contact"));

			linkToContacts = linkTo(methodOn(ContactResource.class).deleteContact(c.getId()));
			model.add(linkToContacts.withRel("delete-contact"));

			entityModelContacts.add(model);
		}

		return entityModelContacts;
	}

}
