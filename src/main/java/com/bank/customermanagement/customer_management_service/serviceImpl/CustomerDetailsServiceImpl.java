
package com.bank.customermanagement.customer_management_service.serviceImpl;

import org.springframework.stereotype.Service;

import com.bank.customermanagement.customer_management_service.dto.CustomerRequestDto;
import com.bank.customermanagement.customer_management_service.model.Customer;
import com.bank.customermanagement.customer_management_service.repository.CustomerRepository;
import com.bank.customermanagement.customer_management_service.service.CustomerDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service   // to denote service layer
public class CustomerDetailsServiceImpl implements CustomerDetailsService {
	private final CustomerRepository customerRepository;
	private static final Logger logger = LogManager.getLogger(CustomerDetailsServiceImpl.class);
	public CustomerDetailsServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	@Override
	public String bankdetails() {
		
		return "SBI Bank";
	}

	@Override
	public Long createCustomer(CustomerRequestDto dto) {
		
		logger.info("Create customer request received. customerCode={}", dto.getCustomerCode());

		try {
			logger.debug("Checking if customer already exists. customerCode={}", dto.getCustomerCode());

			if (customerRepository.existsByCustomerCode(dto.getCustomerCode())) {
				logger.warn("Customer already exists. customerCode={}", dto.getCustomerCode());
				throw new IllegalArgumentException("Customer code already exists");
			}

			Customer customer = new Customer();
			customer.setCustomerCode(dto.getCustomerCode());
			customer.setFirstName(dto.getFirstName());
			customer.setLastName(dto.getLastName());
			customer.setEmail(dto.getEmail());
			customer.setMobileNumber(dto.getMobileNumber());
			customer.setCustomerType(dto.getCustomerType());
			customer.setStatus("ACTIVE");

			logger.debug("Saving customer entity to database. customerCode={}", dto.getCustomerCode());

			Customer savedCustomer = customerRepository.save(customer);

			logger.info("Customer created successfully. customerId={}, customerCode={}", savedCustomer.getCustomerId(),
					savedCustomer.getCustomerCode());

			return savedCustomer.getCustomerId();

		} catch (Exception ex) {
			logger.error("Error while creating customer. customerCode={}", dto.getCustomerCode(), ex);
			throw ex;
		}
	}
}