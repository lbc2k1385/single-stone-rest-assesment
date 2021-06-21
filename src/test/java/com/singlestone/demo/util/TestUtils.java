package com.singlestone.demo.util;

import java.util.ArrayList;
import java.util.List;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;
import com.singlestone.demo.model.Contact;
import com.singlestone.demo.model.ContactRequest;
import com.singlestone.demo.model.Name;
import com.singlestone.demo.model.Phone;
import com.singlestone.demo.model.PhoneRequestDTO;
import com.singlestone.demo.model.PhoneType;
import com.singlestone.demo.model.RequestAddressDTO;

public class TestUtils {
	

	
	public Contact createDummyContact(int id, String email, String streetAddress, String city, String stateCode, 
				String zip, String mobilePhoner, String homePhoner, String workPhoner, String firstName, String lastName,
				String middleNanme){
			
			Contact contact = new Contact();
			contact.setId(id);
			contact.setEmail(email);
			
			AddressId addressId = new AddressId(streetAddress, city, stateCode, zip);
			Address address = new  Address(addressId);
			contact.setAddress(address);
			
			Name name = new Name(firstName, lastName, null, middleNanme);
			contact.setName(name);
			
			List<Phone> phones = new ArrayList<>();
			
			Phone mobilePhone  = new Phone(mobilePhoner, PhoneType.mobile);
			Phone homePhone  = new Phone(homePhoner, PhoneType.home);
			Phone workPhone  = new Phone(workPhoner, PhoneType.work);	
			phones.add(homePhone);
			phones.add(mobilePhone);
			phones.add(workPhone);
			
			contact.setPhone(phones);
			
			return contact;
					
	}
	
	public List<Contact> createDummyContacts() {
			
			List<Contact> dummyContacts = new ArrayList<>();
			
			
			Contact contact1 = createDummyContact(1, "aaa@gmail.com", "123 Somewhere Way,", "Jacksonville", "FL", "32780",
					"111-111-1111", "222-111-1111", "333-111-1111", "John", "Smith", "Jeb");
					
	
			Contact contact2 = createDummyContact(2, "bbbgmail.com", "123 Otherside  Way,", "Tow", "TN", "32782",
					"777-111-1111", "888-111-1111", "999-111-1111","Jerry", "Seinfeld", "Jeb" );
							
			dummyContacts.add(contact1);
			dummyContacts.add(contact2);
			
			return dummyContacts;
	}
	
	public ContactRequest createContactRequst(int id, String email, String streetAddress, String city, String stateCode, 
			String zip, String mobilePhoner, String homePhoner, String workPhoner, String firstName, String lastName,
			String middleNanme) {
		
		ContactRequest contactRequest = new ContactRequest();
		
		Name name = new Name(firstName, lastName,null, middleNanme);
		
		RequestAddressDTO requestAddressDTO = new RequestAddressDTO(streetAddress, city, stateCode, zip);
		
		List<PhoneRequestDTO> phones = new ArrayList<>();
		
		PhoneRequestDTO phone = new PhoneRequestDTO("555-555-5555", PhoneType.home);
		phones.add(phone);
		
		phone = new PhoneRequestDTO("111-111-2222", PhoneType.mobile);
		phones.add(phone);
		
		phone = new PhoneRequestDTO("222-222-2222", PhoneType.work);
		phones.add(phone);
		
		
		contactRequest.setPhone(phones);
		contactRequest.setName(name);
		contactRequest.setAddress(requestAddressDTO);
		contactRequest.setEmail(email);
		
		return contactRequest;
	}


}
