package com.ecommerce.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.library.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long>{

}
