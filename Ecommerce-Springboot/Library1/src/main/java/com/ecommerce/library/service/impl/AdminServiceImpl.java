package com.ecommerce.library.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.repository.AdminRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired 
	private RoleRepository roleRepository;
	

	public AdminServiceImpl(AdminRepository adminRepository, RoleRepository roleRepository) {
		this.adminRepository = adminRepository;
		this.roleRepository = roleRepository;
	}



	@Override
	public Admin findByUsername(String name) {
		return adminRepository.findByUsername(name);
	}

	@Override
	public Admin save(AdminDto adminDto) {
		Admin admin = new Admin();
		admin.setFirstName(adminDto.getFirstName());
		admin.setLastName(adminDto.getLastName());
		admin.setUsername(adminDto.getUsername());
		admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
		admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
		
		return adminRepository.save(admin);
	}
	
}
