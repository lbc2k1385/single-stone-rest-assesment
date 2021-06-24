package com.singlestone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.singlestone.demo.model.Address;
import com.singlestone.demo.model.AddressId;

@Repository
public interface AddressRepository extends JpaRepository<Address, AddressId> {

}

