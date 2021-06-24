package com.singlestone.demo.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.repository.util.ResourceUtil;

@ExtendWith(MockitoExtension.class)
public class ResourceUtilTest {
	
	@InjectMocks
	ResourceUtil resourceUtil;
	
	TestUtils testUtils = new TestUtils();
	
	@Test
	public void createHATEOASLinksTest() {
		
		List<Contact> expectedContacts = new ArrayList<>();
		
		Contact expectedContact1 = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Street Rd.", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		Contact expectedContact2 = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Street Rd.", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		expectedContacts.add(expectedContact1);
		expectedContacts.add(expectedContact2);
		
		assertDoesNotThrow(() -> {
		
			//List<EntityModel<Contact>> modelResults = resourceUtil.createHATEOASLinks(expectedContacts);
			
			//for(int i = 0; i < expectedContacts.size(); i++) {
			//	assertModelResults(expectedContacts.get(i), modelResults.get(i));
			//}
		});
	}
	
	@Test
	public void createHATEOASLinksListTest() {
		
		Contact expectedContact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Street Rd.", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		ContactRequest contactRequest = testUtils.createContactRequst(1, "aaa@gmail.com", "123 Street Rd.", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		assertDoesNotThrow(() -> {
			EntityModel<ContactRequest> modelResult = resourceUtil.createHATEOASLinks(expectedContact);
			//assertModelResults(expectedContact, modelResult);
		});
	}

	private void assertModelResults(Contact expectedContact, EntityModel<Contact> modelResult) {
		
		assertEquals(expectedContact.getEmail(), modelResult.getContent().getEmail());
		assertEquals(expectedContact.getId(),modelResult.getContent().getId());
		
		AddressId resultAddressKey = modelResult.getContent().getAddress().getAddressId();
		AddressId expectedAddressKey = expectedContact.getAddress().getAddressId();
		
		assertEquals(expectedAddressKey.getCity(),resultAddressKey.getCity());
		assertEquals(expectedAddressKey.getStreet(), resultAddressKey.getStreet());
		assertEquals(expectedAddressKey.getState(), resultAddressKey.getState());
		assertEquals(expectedAddressKey.getZip(), resultAddressKey.getZip());
		
		List<Phone> modelResultPhones = modelResult.getContent().getPhone();
		List<Phone> expectedPhones = expectedContact.getPhone();
		
		for(int i = 0; i < expectedPhones.size(); i++) {
			assertEquals(expectedPhones.get(i).getNumber(),modelResultPhones.get(i).getNumber()); 
			assertEquals(expectedPhones.get(i).getPhoneId().getType(),modelResultPhones.get(i).getPhoneId().getType()); 
			
		}
		
		Optional<Link> optAllContactsLink = modelResult.getLink("all-contacts");
		Optional<Link> optGetContactLink = modelResult.getLink("get-contact");
		Optional<Link> optCreateContact = modelResult.getLink("create-contact");
		Optional<Link> optDeleteContact= modelResult.getLink("delete-contact");
		
		assertEquals("all-contacts",optAllContactsLink.get().getRel().value());
		assertEquals("get-contact",optGetContactLink.get().getRel().value());
		assertEquals("create-contact",optCreateContact.get().getRel().value());
		assertEquals("delete-contact",optDeleteContact.get().getRel().value());
		
	}
}
