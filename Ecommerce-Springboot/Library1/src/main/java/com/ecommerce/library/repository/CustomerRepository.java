package com.ecommerce.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.library.model.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
		Customer findByUsername(String username);
		
		Optional<Customer> findByUsernameAndProviderId(String username, String providerId);
}
