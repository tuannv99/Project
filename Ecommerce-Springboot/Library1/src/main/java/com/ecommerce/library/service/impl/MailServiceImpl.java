package com.ecommerce.library.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.service.MailService;
import com.ecommerce.library.service.ThymeleafService;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService{
	
	@Autowired(required = true)
	public JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String email;
	
	@Autowired
     private ThymeleafService thymeleafService;

	@Override
	public void sendMailCreateCustomer(CustomerDto customerDto) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(
					message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,StandardCharsets.UTF_8.name());
			helper.setTo(customerDto.getUsername());
			
			Map<String, Object> variables = new HashMap<>();
			variables.put("firstName", customerDto.getFirstName());
			variables.put("lastName", customerDto.getLastName());
			variables.put("username", customerDto.getUsername());
			
			helper.setText(thymeleafService.createContent("/create-customer-mail-template.html", variables), true);
			helper.setFrom(email);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
