package com.singlestone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.singlestone.demo.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

}