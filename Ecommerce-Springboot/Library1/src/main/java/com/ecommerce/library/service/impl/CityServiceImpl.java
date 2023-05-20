package com.ecommerce.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.library.model.City;
import com.ecommerce.library.repository.CityRepository;
import com.ecommerce.library.service.CityService;
@Service
public class CityServiceImpl implements CityService{

	@Autowired
	private CityRepository cityRepository;
	
	@Override
	public List<City> getAllCities() {

		return cityRepository.findAll();
	}

}
