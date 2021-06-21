package com.singlestone.demo.resource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.hateoas.CollectionModel;
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
import com.singlestone.demo.model.Name;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneType;
import com.singlestone.demo.repository.ContactRepository;
import com.singlestone.demo.repository.util.ContactRequestMapper;
import com.singlestone.demo.repository.util.ResourceUtil;
import com.singlestone.demo.resource.ContactResource;
import com.singlestone.demo.resource.exceptions.ContactNotFoundException;
import com.singlestone.demo.resource.exceptions.ContactNotSavedException;
import com.singlestone.demo.util.TestUtils;

@ExtendWith(MockitoExtension.class)
class ContactResourceTest {

	@Mock
	private ContactRequestMapper contactRequestMapper;

	@Mock
	private ContactRepository contactRepository;

	@Mock
	private ResourceUtil resourceUtil;

	@InjectMocks
	ContactResource contactResource;
	
	
	TestUtils testUtils = new TestUtils();

	@Test
	public void getContacts_ContactFoundExceptionTest() {

		when(contactRepository.findAll()).thenReturn(new ArrayList<Contact>());

		assertThrows(ContactNotFoundException.class, () -> {
			contactResource.getContacts();
		});

	}

	@Test
	public void getContacts_SuccessfulTest() {

		mockContacts(false);

		assertDoesNotThrow(() -> {
			ResponseEntity<CollectionModel<EntityModel<Contact>>> responseEntity = contactResource.getContacts();
			assertEquals("200 OK", responseEntity.getStatusCode().toString());
		});

	}

	
	
	@Test
	public void getContact_ContactFoundExceptionTest() {
		
		int id = 1;
				
		when(contactRepository.findById(id)).thenReturn(Optional.<Contact>empty());
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactResource.getContact(id);
		});
		
	}
	
	@Test
	public void getContact_SuccessfulTest() {
		
		int id = 1;
		
		Contact contact = new Contact();
		contact.setId(id);

	    Optional<Contact> opt = Optional.of(contact);
	    

	    EntityModel<Contact> model = EntityModel.of(contact);

		when(contactRepository.findById(id)).thenReturn(opt);
		when(resourceUtil.createHATEOASLinks(contact)).thenReturn(model);
		
		assertDoesNotThrow(() -> {
			EntityModel<Contact>  entityModel = contactResource.getContact(id);
			assertTrue(entityModel instanceof EntityModel);
		});
	}
	
	@Test 
	public void getCallList_ExceptionTest() {
		
		mockContacts(true);
		
		assertThrows(ContactNotFoundException.class, () -> {
			contactResource.getCallList();
		});		
	}
	
	@Test 
	public void getCallList_SuccessfulTest() {
		
		mockContacts(false);
		
		Name name1 = new Name("John", "Smith", null, "Jeb");
		Name name2 = new Name("Jerry", "Seinfeld", null, "Jeb");
		
		List<CallListContact> expectedList = new ArrayList<>();
		CallListContact callListContact1 = new CallListContact(name1 , "222-111-1111");
		CallListContact callListContact2 = new CallListContact(name2 , "888-111-1111");
		
		expectedList.add(callListContact1);
		expectedList.add(callListContact2);
		
		assertDoesNotThrow(() -> {
			List<CallListContact> actual = contactResource.getCallList();
			
			assertEquals(expectedList.size(), actual.size());
			
			for(int i = 0; i < expectedList.size(); i++) {	
				assertEquals(expectedList.get(i).getName().getFirst(), actual.get(i).getName().getFirst());
				assertEquals(expectedList.get(i).getName().getLast(), actual.get(i).getName().getLast());
				assertEquals(expectedList.get(i).getName().getMiddle(), actual.get(i).getName().getMiddle());
				assertEquals(expectedList.get(i).getPhone(), actual.get(i).getPhone());
				
			}
		});		
	}
	
	@Test
	public void createContact_ContactNotSavedExceptionTest() {
		
		Contact contact = new Contact();
		ContactRequest contactRequest = new ContactRequest();
   
		when(contactRequestMapper.mapToContact(contactRequest)).thenReturn(contact);
				
		assertThrows(ContactNotSavedException.class, () -> {		
			contactResource.createContact(contactRequest);
		});
		
		contact.setId(1);
		
		assertThrows(ContactNotSavedException.class, () -> {		
			contactResource.createContact(contactRequest);
		});
	
	}
	
	

	
	@Test
	public void createContact_SuccessfulTest() {
		
		Contact contact = new Contact();
		contact.setId(1);
		ContactRequest contactRequest = new ContactRequest();
		
		MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
         
		when(contactRequestMapper.mapToContact(contactRequest)).thenReturn(contact);
		
		when(contactRepository.save(contact)).thenReturn(contact);
		
		assertDoesNotThrow(() -> {			
			ResponseEntity<?> responseEntity = contactResource.createContact(contactRequest);
			assertEquals("201 CREATED",responseEntity.getStatusCode().toString());
		});
	
	}
	
	@Test
	public void deleteContact_ContactNotFoundExceptionTest() {
		
		when(contactRepository.existsById(1)).thenReturn(false);
		
		assertThrows(ContactNotFoundException.class, () -> {		
			contactResource.deleteContact(1);
		});
	}
	
	
	@Test
	public void deleteContact_Successful() {
		
		when(contactRepository.existsById(1)).thenReturn(true);
		doNothing().when(contactRepository).deleteById(1);

		assertDoesNotThrow(() -> {			
			ResponseEntity<Contact> responseEntity = contactResource.deleteContact(1);
			assertEquals("204 NO_CONTENT",responseEntity.getStatusCode().toString());
		});
	}
	
	@Test
	public void updateContact_ContactNotFoundExceptionTest() {
		
		Contact requestContact = new Contact();
		
		when(contactRepository.getOne(1)).thenReturn(null);
		
		assertThrows(ContactNotFoundException.class, () -> {		
			contactResource.updateContact(requestContact, 1);
		});
	}
	
	@Test
	public void updateContact_Successful() {
		
		Contact contact = testUtils.createDummyContact(1, "aaa@gmail.com", "123 Somewhere Way,", "Jacksonville", "FL", "32780",
				"111-111-1111", "222-111-1111", "333-111-1111", "Jerry", "Seinfeld", null);
		
		when(contactRepository.getOne(1)).thenReturn(contact);
		
		assertDoesNotThrow(() -> {			
			ResponseEntity<Contact> responseEntity = contactResource.updateContact(contact, 1);
			assertEquals("204 NO_CONTENT",responseEntity.getStatusCode().toString());
		});
	}
	
	
	private void mockContacts(boolean isEmptyList) {
		
		List<Contact> dummyContacts = testUtils.createDummyContacts();

		List<EntityModel<Contact>> entityModels = new ArrayList<>();
		EntityModel<Contact> model = EntityModel.of(new Contact());
		entityModels.add(model);
		
		if(isEmptyList)
			when(contactRepository.findAll()).thenReturn(new ArrayList<Contact>());
			
		else
			when(contactRepository.findAll()).thenReturn(dummyContacts);
		
		
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

	public ContactResource getContactResource() {
		return contactResource;
	}

	public void setContactResource(ContactResource contactResource) {
		this.contactResource = contactResource;
	}

	public ResourceUtil getResourceUtil() {
		return resourceUtil;
	}

	public void setResourceUtil(ResourceUtil resourceUtil) {
		this.resourceUtil = resourceUtil;
	}

}
