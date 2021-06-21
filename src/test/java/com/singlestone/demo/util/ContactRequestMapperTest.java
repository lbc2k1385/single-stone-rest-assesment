package com.singlestone.demo.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneRequestDTO;
import com.singlestone.demo.model.RequestAddressDTO;
import com.singlestone.demo.repository.util.ContactRequestMapper;

@ExtendWith(MockitoExtension.class)
class ContactRequestMapperTest {
	
	TestUtils testUtils = new TestUtils();
	
	@InjectMocks
	ContactRequestMapper contactRequestMapper;
	
	@Test
	public void mapToContactTest_NullCheck() {
		
	Contact expected = new Contact();
		
	assertDoesNotThrow(() -> {
			ContactRequest contactRequest = null;
			Contact contact = contactRequestMapper.mapToContact(contactRequest);
			assertEquals(expected, contact);
			
			contactRequest = new ContactRequest();
			contact = contactRequestMapper.mapToContact(contactRequest);
			assertEquals(expected, contact);
			
			contactRequest.setAddress(null);
			contact = contactRequestMapper.mapToContact(contactRequest);
			assertEquals(expected, contact);
			
			
			RequestAddressDTO requestAddressDTO = new RequestAddressDTO();
			contactRequest.setAddress(requestAddressDTO);
			contact = contactRequestMapper.mapToContact(contactRequest);
			assertEquals(expected, contact);
			
			List<PhoneRequestDTO> phones = new ArrayList<>();
			contactRequest.setPhone(phones);
			contact = contactRequestMapper.mapToContact(contactRequest);
			assertEquals(expected, contact);
						
		});
	}

	@Test
	public void mapToContactRequestTest_NullCheck() {
		ContactRequest expected = new ContactRequest();
		assertMapToContactRequest(expected);		
	}
	
	@Test
	public void mapToContactRequestTestWithList_NullCheck() {
		ContactRequest expected = new ContactRequest();
		assertMapToContactRequestWithList(expected);
	}
	
		@Test
	public void mapToContactTest() {
		
		Contact expectedContact = testUtils.createDummyContact(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		ContactRequest contactRequest = testUtils.createContactRequst(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		assertDoesNotThrow(() -> {
			
			Contact contact = contactRequestMapper.mapToContact(contactRequest);
			
			assertEquals(expectedContact.getId(), contact.getId());
			assertEquals(expectedContact.getEmail(), contact.getEmail());
			assertEquals(expectedContact.getName(), contact.getName());
			assertEquals(expectedContact.getAddress().getAddressId().getState(),contact.getAddress().getAddressId().getState());
			assertEquals(expectedContact.getAddress().getAddressId().getCity(),contact.getAddress().getAddressId().getCity());
			assertEquals(expectedContact.getAddress().getAddressId().getStreet(),contact.getAddress().getAddressId().getStreet());
			assertEquals(expectedContact.getAddress().getAddressId().getZip(),contact.getAddress().getAddressId().getZip());
			
			List<Phone> expectedPhones = expectedContact.getPhone();
			List<Phone> actualPhones = contact.getPhone();
			
			for(int i = 0; i < expectedPhones.size(); i++) {
				assertEquals(expectedPhones.get(i).getNumber(),actualPhones.get(i).getNumber()); 
				assertEquals(expectedPhones.get(i).getType(),actualPhones.get(i).getType()); 
				
			}
		});		
	}
	
	@Test
	public void mapToContactRequestTest() {
		Contact contact = testUtils.createDummyContact(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		ContactRequest expectedContactRequest = testUtils.createContactRequst(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		assertDoesNotThrow(() -> {
			
			ContactRequest actualContactRequest = contactRequestMapper.mapToContactRequest(contact);
			
			assertEquals(actualContactRequest.getEmail(), expectedContactRequest.getEmail());
			assertEquals(actualContactRequest.getName(), expectedContactRequest.getName());
			assertEquals(actualContactRequest.getAddress().getState(), expectedContactRequest.getAddress().getState());
			
			assertEquals(actualContactRequest.getAddress().getCity(),expectedContactRequest.getAddress().getCity());
			assertEquals(actualContactRequest.getAddress().getStreet(),expectedContactRequest.getAddress().getStreet());
			assertEquals(actualContactRequest.getAddress().getZip(),expectedContactRequest.getAddress().getZip());
			
			List<PhoneRequestDTO> expectedPhones = expectedContactRequest.getPhone();
			List<PhoneRequestDTO> actualPhones = actualContactRequest.getPhone();
			
			for(int i = 0; i < expectedPhones.size(); i++) {
				assertEquals(expectedPhones.get(i).getNumber(),actualPhones.get(i).getNumber()); 
				assertEquals(expectedPhones.get(i).getType(),actualPhones.get(i).getType()); 
				
			}
			
		});
		
	}
	
	@Test
	public void mapToContactRequestWithListTest() {
		
		List<Contact> contacts = new ArrayList<>();
		List<ContactRequest> contactRequests = new ArrayList<>();
		
		Contact contact1 = testUtils.createDummyContact(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		Contact contact2 = testUtils.createDummyContact(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		contacts.add(contact1);
		contacts.add(contact2);
		
		ContactRequest contactReques1 = testUtils.createContactRequst(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		ContactRequest contactReques2 = testUtils.createContactRequst(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
	
		ContactRequest expectedContactRequest = testUtils.createContactRequst(0, "aaa@gmail.com", "111 Street Rd.", "Jacksonville","FL", "37821",
				"111-111-2222", "555-555-5555", "222-222-2222", "John", "Smith","Blake");
		
		contactRequests.add(contactReques1);
		contactRequests.add(contactReques2);
		
		assertDoesNotThrow(() -> {
			
			List<ContactRequest> actualContactRequest = contactRequestMapper.mapToContactRequest(contacts);
			
			for(int i =0; i < contactRequests.size(); i++) {
				assertEquals(actualContactRequest.get(i).getEmail(), contacts.get(i).getEmail());
				assertEquals(actualContactRequest.get(i).getName(), contacts.get(i).getName());
				assertEquals(actualContactRequest.get(i).getAddress().getState(), contacts.get(i).getAddress().getAddressId().getState());
				
				assertEquals(actualContactRequest.get(i).getAddress().getCity(),contacts.get(i).getAddress().getAddressId().getCity());
				assertEquals(actualContactRequest.get(i).getAddress().getStreet(),contacts.get(i).getAddress().getAddressId().getStreet());
				assertEquals(actualContactRequest.get(i).getAddress().getZip(),contacts.get(i).getAddress().getAddressId().getZip());
				
				List<PhoneRequestDTO> expectedPhones = expectedContactRequest.getPhone();
				List<PhoneRequestDTO> actualPhones = actualContactRequest.get(i).getPhone();
				
				for(int j = 0; j < expectedPhones.size(); j++) {
					assertEquals(expectedPhones.get(j).getNumber(),actualPhones.get(j).getNumber()); 
					assertEquals(expectedPhones.get(j).getType(),actualPhones.get(j).getType()); 
					
				}
			}		
		});
		
	}
	
private void assertMapToContactRequestWithList(ContactRequest expected) {
		
		List<Contact> contacts = new ArrayList<>();
		Contact contact1 = new Contact();
		Contact contact2 = new Contact();
		contacts.add(contact1);
		contacts.add(contact2);
		
		for(int i = 0; i < contacts.size(); i++) {
			
			assertDoesNotThrow(() -> {
				
				Contact contact  = null;
				ContactRequest contactRequest = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(expected, contactRequest);
				
				contact = new Contact();
				contactRequest = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(expected, contactRequest);
		
			
				Address address = new Address();
				contact.setAddress(address);
				contactRequest = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(expected, contactRequest);
				
				AddressId addressId = new  AddressId();
				address.setAddressId(addressId);
				contactRequest = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(expected, contactRequest);
				
				List<Phone> phones = null;
				contact.setPhone(phones);
				contactRequest = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(expected, contactRequest);
				
				phones = new ArrayList<>();
				contact.setPhone(phones);
				contactRequest = contactRequestMapper.mapToContactRequest(contact);
				assertEquals(expected, contactRequest);
							
			});
		}
		
		
	}
	
	

	private void assertMapToContactRequest(ContactRequest expected) {
		assertDoesNotThrow(() -> {
			
			Contact contact  = null;
			ContactRequest contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertEquals(expected, contactRequest);
			
			contact = new Contact();
			contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertEquals(expected, contactRequest);
	
		
			Address address = new Address();
			contact.setAddress(address);
			contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertEquals(expected, contactRequest);
			
			AddressId addressId = new  AddressId();
			address.setAddressId(addressId);
			contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertEquals(expected, contactRequest);
			
			List<Phone> phones = null;
			contact.setPhone(phones);
			contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertEquals(expected, contactRequest);
			
			phones = new ArrayList<>();
			contact.setPhone(phones);
			contactRequest = contactRequestMapper.mapToContactRequest(contact);
			assertEquals(expected, contactRequest);
						
		});
	}
	


	public ContactRequestMapper getContactRequestMapper() {
		return contactRequestMapper;
	}

	public void setContactRequestMapper(ContactRequestMapper contactRequestMapper) {
		this.contactRequestMapper = contactRequestMapper;
	}
	
	

}
