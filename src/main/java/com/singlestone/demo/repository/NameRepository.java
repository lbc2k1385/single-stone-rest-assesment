package com.singlestone.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.singlestone.demo.model.Name;

public interface NameRepository extends JpaRepository<Name, Integer> {

}
