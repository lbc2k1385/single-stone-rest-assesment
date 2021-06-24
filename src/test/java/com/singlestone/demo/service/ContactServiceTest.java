package com.singlestone.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.CallListContact;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.ContactResponse;
import com.singlestone.demo.model.Name;
import com.singlestone.demo.model.RequestAddressDTO;
import com.singlestone.demo.repository.AddressRepository;
import com.singlestone.demo.repository.ContactRepository;
import com.singlestone.demo.repository.util.ContactRequestMapper;
import com.singlestone.demo.repository.util.ResourceUtil;
import com.singlestone.demo.resource.exceptions.ContactNotFoundException;
import com.singlestone.demo.resource.exceptions.ContactNotSavedException;
import com.singlestone.demo.util.TestUtils;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
	
	@InjectMocks
	private ContactService contactService;
	
	@Mock
	private ResourceUtil resourceUtil;
	
	@Mock
	private AddressRepository addressRepository;
	
	@Mock
	private ContactRequestMapper contactRequestMapper;
	
	@Mock
	private ContactRepository contactRepository;
	
	private TestUtils testUtils = new TestUtils();

	@Test
	public void createContactTest_ContactNotSavedException(){
		
		assertThrows(ContactNotSavedException.class, () -> {
			ContactRequest contactRequest = null;
			contactService.handleContactCreation(contactRequest);
			
			contactRequest = new ContactRequest();
			contactService.handleContactCreation(contactRequest);
		});
		
		assertThrows(ContactNotSavedException.class, () -> {
			ContactRequest contactRequest = new ContactRequest();
			contactService.handleContactCreation(contactRequest);
		});	
	}
	
	@Test
	public void createContactTest_Sucessful(){
		
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		ContactRequest contactRequest = testUtils.createContactRequst(1, "aaa@gmail.com", "123 Somewhere Way,", 
				"Jacksonville", "FL", "32780","111-111-1111", "222-111-1111", "333-111-1111", "John", "Blake", "Lloyd");
	
		Contact contact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Somewhere Way", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
			
		Optional<Contact> optContact = Optional.of(contact);
		   
		when(contactRequestMapper.mapToContact(contactRequest)).thenReturn(optContact);
		when(contactRepository.save(contact)).thenReturn(contact);
	
		
		assertDoesNotThrow(() -> {
			ResponseEntity<?> returnStatus = contactService.handleContactCreation(contactRequest);
			assertEquals("201 CREATED", returnStatus.getStatusCode().toString());

		});
	}
	
	@Test
	public void createContactTest_RequestMapperFailed(){
		
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		ContactRequest contactRequest = testUtils.createContactRequst(1, "aaa@gmail.com", "123 Somewhere Way,", 
				"Jacksonville", "FL", "32780","111-111-1111", "222-111-1111", "333-111-1111", "John", "Blake", "Lloyd");
	
		Contact contact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Somewhere Way", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		Optional<Contact> optContact = Optional.empty();
		   
		when(contactRequestMapper.mapToContact(contactRequest)).thenReturn(optContact);

		assertDoesNotThrow(() -> {
			ResponseEntity<?> returnStatus = contactService.handleContactCreation(contactRequest);
			assertEquals("500 INTERNAL_SERVER_ERROR", returnStatus.getStatusCode().toString());

		});
		
	}
	
	@Test
	public void createContactTest_ContactNotFound(){
				
		ContactRequest contactRequest = testUtils.createContactRequst(1, "aaa@gmail.com", "123 Somewhere Way,", 
				"Jacksonville", "FL", "32780","111-111-1111", "222-111-1111", "333-111-1111", "John", "Blake", "Lloyd");
	
		Contact contact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Somewhere Way", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
			
		Optional<Contact> optContact = Optional.of(contact);
		   
		when(contactRequestMapper.mapToContact(contactRequest)).thenReturn(optContact);
		when(contactRepository.save(contact)).thenReturn(null);
		
		assertThrows(ContactNotSavedException.class, () -> {	
			contactService.handleContactCreation(contactRequest);
		});
						
	}
	
	@Test
	public void handleGetContactsTest_SucessfulTest() {
				
		Contact contact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Somewhere Way", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		List<Contact> contacts = testUtils.createDummyContacts();
		
		EntityModel<ContactRequest> model =  EntityModel.of(new ContactRequest());
		
		when(contactRepository.findAll()).thenReturn(contacts);

		when(resourceUtil.createHATEOASLinks(contacts)).thenReturn(new ArrayList<EntityModel<ContactResponse>>() );
					
		assertDoesNotThrow(() -> {
			ResponseEntity<?> returnStatus= contactService.handleGetContacts();
			assertEquals("200 OK", returnStatus.getStatusCode().toString());

		});

		
	}
	
	@Test
	public void handleGetContactsTest_ContactNotFoundExceptionTest() {
			
		when(contactRepository.findAll()).thenReturn(new ArrayList<>());
		
		assertThrows(ContactNotFoundException.class, () -> {	
			contactService.handleGetContacts();
		});
				
	}
	
	@Test
	public void handleGetContacts_ContactNotFoundException() {
	
		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleGetContact(1);
		});
		
	}
	
	@Test
	public void handleGetContacts_SucessfulTest() {
		
		Contact contact = new Contact();
		
		Optional<Contact> optionalContact = Optional.of(contact);
		
		when(contactRepository.findById(1)).thenReturn(optionalContact);
		
		when(resourceUtil.createHATEOASLinks(optionalContact.get()))
		.thenReturn(Mockito.any());
		
		assertDoesNotThrow( () -> {
			contactService.handleGetContact(1);
		});
		
	}
	

	@Test
	public void handleGetCallList_ContactNotFoundExceptionTest() {
		
		Contact contact = new Contact();
		List<Contact> contacts = new ArrayList<>();
		
		when(contactRepository.findAll()).thenReturn(contacts);
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleGetCallList();
		});
		
		contacts.add(contact);
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleGetCallList();
		});
		
		contacts.clear();
		contact.setName(new Name());
		contacts.add(contact);
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleGetCallList();
		});
		
		contacts.clear();
		contact.setPhone(new ArrayList<>());
		contacts.add(contact);

		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleGetCallList();
		});
		
	}
	
	@Test
	public void handleGetCallList_Sucessful() {
		
		List<Contact> contacts = testUtils.createDummyContacts();
		
		when(contactRepository.findAll()).thenReturn(contacts);
	
		assertDoesNotThrow(() -> {
			
			List<CallListContact> callListContacts = contactService.handleGetCallList();
			
			assertEquals("Seinfeld", callListContacts.get(0).getName().getLast());
			assertEquals("Jerry", callListContacts.get(0).getName().getFirst());
			assertEquals("Smith", callListContacts.get(1).getName().getLast());
			assertEquals("John", callListContacts.get(1).getName().getFirst());
			
		});
	}
	
	@Test
	public void handleDelete_ContactNotFoundExceptionTest() {
		
		Optional<Contact> optContact = Optional.empty();
		
		when(contactRepository.findById(1)).thenReturn(optContact);
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleDelete(1);
		});
		
	}
	
	@Test
	public void handleDelete_SucessfulTest() {
		
		Contact contact = new Contact();
		Address address = new Address();
		AddressId addressId = new AddressId();
		address.setAddressId(addressId);
		contact.setAddress(address);
		
		Optional<Contact> optContact = Optional.of(contact);
		
		when(contactRepository.findById(1)).thenReturn(optContact);
		
		Mockito.doNothing().when(addressRepository).deleteById(addressId);
		
		Mockito.doNothing().when(contactRepository).delete(any(Contact.class));
		
		assertDoesNotThrow( () -> {

			ResponseEntity<Contact> result = contactService.handleDelete(1);
			assertEquals("204 NO_CONTENT", result.getStatusCode().toString());

		});
		
	}
	
	@Test
	public void handleUpdateContactTest_ContactNotFoundException() {
		
		Optional<Contact> optContact = Optional.of(new Contact());
		
		when(contactRepository.findById(1)).thenReturn(optContact);
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleUpdateContact(null, 1);
		});
		
		optContact = Optional.empty();
		
		
		when(contactRepository.findById(1)).thenReturn(optContact);
		
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactService.handleUpdateContact(null, 1);
		});
		
		
		assertThrows(ContactNotFoundException.class, () -> {
			ContactRequest contactRequest = new ContactRequest();
			contactService.handleUpdateContact(contactRequest, 1);
		});
		
		assertThrows(ContactNotFoundException.class, () -> {
			ContactRequest contactRequest = new ContactRequest();
			contactRequest.setAddress(new RequestAddressDTO());
			contactService.handleUpdateContact(contactRequest, 1);
		});
		
	}
	
	
	@Test
	public void handleUpdateContactTest() {
		
		ContactRequest contactRequest = testUtils.createContactRequst(1, "aaa@gmail.com", "123 Somewhere Way", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		
		Contact contact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Somewhere Way", "Jacksonville", "FL", 
				"23456", "111-111-1111", "222-222-2222", "333-333-3333", "John", "Blake", "Lloyd");
		
		AddressId addressIdToUpdate = contact.getAddress().getAddressId();
		
		Address address = contact.getAddress();
		
		Optional<Address> optAddressToDelete = Optional.of(address);
		
		Optional<Contact> optContact = Optional.of(contact);
		
		when(contactRepository.findById(1)).thenReturn(optContact);
		
		when(addressRepository.findById(addressIdToUpdate)).thenReturn(optAddressToDelete);
	
		Mockito.doNothing().when(addressRepository).deleteById(any(AddressId.class));
		
		when(contactRepository.save(contact)).thenReturn(contact);
		
		assertDoesNotThrow( () -> {

			ResponseEntity<Contact> result = contactService.handleUpdateContact(contactRequest,1);
			assertEquals("204 NO_CONTENT", result.getStatusCode().toString());

		});
		
	}
}
