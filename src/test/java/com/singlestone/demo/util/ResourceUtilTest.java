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
			resourceUtil.createHATEOASLinks(expectedContacts);
		});
	}
	
	@Test
	public void createHATEOASLinksListTest() {
		
		Contact expectedContact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Street Rd.", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		assertDoesNotThrow(() -> {
			resourceUtil.createHATEOASLinks(expectedContact);
		});
	}
}
