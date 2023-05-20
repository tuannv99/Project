package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;

public interface MailService {
		void sendMailCreateCustomer(CustomerDto customerDto);
}
