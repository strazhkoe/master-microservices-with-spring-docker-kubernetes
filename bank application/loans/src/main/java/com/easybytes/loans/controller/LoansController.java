package com.easybytes.loans.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.easybytes.loans.LoansServiceConfig;
import com.easybytes.loans.model.Customer;
import com.easybytes.loans.model.Loans;
import com.easybytes.loans.model.Properties;
import com.easybytes.loans.repository.LoansRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class LoansController {
	
	@Autowired
	private LoansRepository loansRepository;
	
	@Autowired
	private LoansServiceConfig loansConfig;
	
	@PostMapping("/myLoans")
	public List<Loans> getAccountDetails(@RequestHeader(name = "easybank-correlation-id", required = false) String correlationId,
			                             @RequestBody Customer customer) {
		System.out.println("Invoking getAccountsDetails");
		List<Loans> loans = loansRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());

		if (loans != null) {
			return loans;
		} else {
			return null;
		}
	}
	
	@GetMapping("/loans/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writerWithDefaultPrettyPrinter();
		Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
				loansConfig.getMailDetails(), loansConfig.getActiveBranches());
		String result = ow.writeValueAsString(properties);
		return result;
	}
}
