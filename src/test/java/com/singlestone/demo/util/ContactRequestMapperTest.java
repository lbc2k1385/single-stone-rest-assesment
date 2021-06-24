package com.singlestone.demo.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressDTO;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.ContactResponse;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneId;
import com.singlestone.demo.model.PhoneRequestDTO;
import com.singlestone.demo.model.PhoneType;
import com.singlestone.demo.model.RequestAddressDTO;
import com.singlestone.demo.repository.util.ContactRequestMapper;

@ExtendWith(MockitoExtension.class)
class ContactRequestMapperTest {
	
	TestUtils testUtils = new TestUtils();
	
	@InjectMocks
	ContactRequestMapper contactRequestMapper;
	
	@Test
	public void mapToContactTest_Sucessfull() {
			
		ContactRequest contactRequest = testUtils.createContactRequst(2, "bbbgmail.com", "123 Otherside  Way,", "Tow", "TN", "32782",
					"777-111-1111", "888-111-1111", "999-111-1111","Jerry", "Seinfeld", "Jeb" );
	
		Contact contact = testUtils.createDummyContact(2, "bbbgmail.com", "123 Otherside  Way,", "Tow", "TN", "32782",
				"777-111-1111", "888-111-1111", "999-111-1111","Jerry", "Seinfeld", "Jeb" );
	
		
		assertDoesNotThrow(()->{
			Optional<Contact> returnContact = contactRequestMapper.mapToContact(contactRequest);
			assertEquals(contact.getName().getFirst(), returnContact.get().getName().getFirst());
			assertEquals(contact.getName().getLast(), returnContact.get().getName().getLast());
			assertEquals(contact.getName().getLast(), returnContact.get().getName().getLast());
			assertEquals(contact.getAddress().getAddressId().getCity(), returnContact.get().getAddress().getAddressId().getCity());
			assertEquals(contact.getAddress().getAddressId().getState(), returnContact.get().getAddress().getAddressId().getState());
			assertEquals(contact.getAddress().getAddressId().getZip(), returnContact.get().getAddress().getAddressId().getZip());
			assertEquals(contact.getAddress().getAddressId().getStreet(), returnContact.get().getAddress().getAddressId().getStreet());
			assertEquals(contact.getEmail(), returnContact.get().getEmail());
		});
	}
	
	@Test
	public void mapToContactTest_NullTest() {
			
		assertDoesNotThrow(()->{
			ContactRequest contactRequest = null;
			Optional<Contact> returnContact = contactRequestMapper.mapToContact(contactRequest);
			assertTrue(returnContact.isEmpty());
			
			contactRequest = new ContactRequest();
			returnContact = contactRequestMapper.mapToContact(contactRequest);
			assertTrue(returnContact.isEmpty());
			
			RequestAddressDTO address = null;
			contactRequest.setAddress(address);
			returnContact = contactRequestMapper.mapToContact(contactRequest);
			assertTrue(returnContact.isEmpty());
			
			contactRequest.setAddress(address);
			returnContact = contactRequestMapper.mapToContact(contactRequest);
			assertTrue(returnContact.isEmpty());
			
			contactRequest.setAddress(address);
			PhoneRequestDTO phone = new PhoneRequestDTO();
			List<PhoneRequestDTO> phones = new ArrayList<>();
			phones.add(phone);
			contactRequest.setPhone(phones);
			returnContact = contactRequestMapper.mapToContact(contactRequest);
			assertTrue(returnContact.isEmpty());
		});
	}
	
	@Test
	public void mapToContactRequest_NullTest() {
		
		assertDoesNotThrow(()->{
			
			Contact contact = null;
			Optional<ContactRequest> contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertTrue(contactRequest.isEmpty());
			
			contact = new Contact();
			contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertTrue(contactRequest.isEmpty());
			
			contact.setAddress(new Address());
			contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertTrue(contactRequest.isEmpty());
			
		});
		
	}
	
	@Test
	public void mapToContactRequest_Sucessful() {
		
		assertDoesNotThrow(()->{
			
			Contact contact =  testUtils.createDummyContact(2, "bbbgmail.com", "123 Otherside  Way,", "Tow", "TN", "32782",
					"777-111-1111", "888-111-1111", "999-111-1111","Jerry", "Seinfeld", "Jeb" );
			
			assertDoesNotThrow(()->{
				Optional<ContactRequest> returnContact = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(contact.getName().getFirst(), returnContact.get().getName().getFirst());
				assertEquals(contact.getName().getLast(), returnContact.get().getName().getLast());
				assertEquals(contact.getName().getLast(), returnContact.get().getName().getLast());
				assertEquals(contact.getAddress().getAddressId().getCity(), returnContact.get().getAddress().getCity());
				assertEquals(contact.getAddress().getAddressId().getState(), returnContact.get().getAddress().getState());
				assertEquals(contact.getAddress().getAddressId().getZip(), returnContact.get().getAddress().getZip());
				assertEquals(contact.getAddress().getAddressId().getStreet(), returnContact.get().getAddress().getStreet());
				assertEquals(contact.getEmail(), returnContact.get().getEmail());
			});
			
		});
		
	}
	
	@Test
	public void mapToContactResponse_NullTest() {
		
		assertDoesNotThrow(()->{
			
			Contact contact = null;
			Optional<ContactResponse> contactRequest = contactRequestMapper.mapToContactResponse(contact);
			assertTrue(contactRequest.isEmpty());
			
			contact = new Contact();
			contactRequest = contactRequestMapper.mapToContactResponse(contact);
			assertTrue(contactRequest.isEmpty());
			
			contact.setAddress(new Address());
			contactRequest = contactRequestMapper.mapToContactResponse(contact);
			assertTrue(contactRequest.isEmpty());
			
		});
	}
	
	@Test
	public void mapToContactResponse_Sucessful() {
		
		assertDoesNotThrow(()->{
			
			Contact contact =  testUtils.createDummyContact(2, "bbbgmail.com", "123 Otherside  Way,", "Tow", "TN", "32782",
					"777-111-1111", "888-111-1111", "999-111-1111","Jerry", "Seinfeld", "Jeb" );
			
			assertDoesNotThrow(()->{
				Optional<ContactRequest> returnContact = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(contact.getName().getFirst(), returnContact.get().getName().getFirst());
				assertEquals(contact.getName().getLast(), returnContact.get().getName().getLast());
				assertEquals(contact.getName().getLast(), returnContact.get().getName().getLast());
				assertEquals(contact.getAddress().getAddressId().getCity(), returnContact.get().getAddress().getCity());
				assertEquals(contact.getAddress().getAddressId().getState(), returnContact.get().getAddress().getState());
				assertEquals(contact.getAddress().getAddressId().getZip(), returnContact.get().getAddress().getZip());
				assertEquals(contact.getAddress().getAddressId().getStreet(), returnContact.get().getAddress().getStreet());
				assertEquals(contact.getEmail(), returnContact.get().getEmail());
			});
			
		});
		
	}

	public ContactRequestMapper getContactRequestMapper() {
		return contactRequestMapper;
	}

	public void setContactRequestMapper(ContactRequestMapper contactRequestMapper) {
		this.contactRequestMapper = contactRequestMapper;
	}
	
	

}
