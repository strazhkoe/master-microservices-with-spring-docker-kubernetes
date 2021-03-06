package com.easybytes.accounts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.easybytes.accounts.config.AccountsServiceConfig;
import com.easybytes.accounts.model.Accounts;
import com.easybytes.accounts.model.Cards;
import com.easybytes.accounts.model.Customer;
import com.easybytes.accounts.model.CustomerDetails;
import com.easybytes.accounts.model.Loans;
import com.easybytes.accounts.model.Properties;
import com.easybytes.accounts.repository.AccountsRepository;
import com.easybytes.accounts.service.client.CardsFeignClient;
import com.easybytes.accounts.service.client.LoansFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;

@RestController
public class AccountsController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private AccountsServiceConfig accountsConfig;
	
	@Autowired
	private LoansFeignClient loansFeignClient;
	
	@Autowired
	private CardsFeignClient cardsFeignClient;
	
	@PostMapping("/myAccount")
	@Timed(value = "getAccountDetails.time", description = "Time taken to return Account Details")
	public Accounts getAccountDetails(@RequestBody Customer customer) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		if(accounts != null) {
			return accounts;
		} else {
			return null;
		}
	}
	
	@GetMapping("/account/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		String result = ow.writeValueAsString(properties);
		return result;
	}
	
	@PostMapping("/myCustomerDetails")
	@CircuitBreaker(name = "detailsForCustomerSupportApp", 
	                fallbackMethod = "myCustomerDetailFallBack")
	@Retry(name="retryForCustomerDetails", fallbackMethod = "myCustomerDetailFallBack")
	public CustomerDetails myCustomerDetails(@RequestHeader(name = "easybank-correlation-id", required = false) String correlationId,
			                                 @RequestBody Customer customer) {
		logger.info("myCustomerDetails() method started");
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loansDetails = loansFeignClient.getLoansDetails(correlationId, customer);
		List<Cards> cardsDetails = cardsFeignClient.getCardsDetails(correlationId, customer);
		
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setCards(cardsDetails);
		customerDetails.setLoans(loansDetails);
		
		logger.info("myCustomerDetails() method ended");
		return customerDetails;
	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello!";
	}
	
	@SuppressWarnings("unused")
	private CustomerDetails myCustomerDetailFallBack(String correlationId, Customer customer, Throwable t) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loansDetails = loansFeignClient.getLoansDetails(correlationId, customer);
		
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loansDetails);
		
		return customerDetails;
	}
	
	@SuppressWarnings("unused")
	private String sayHelloFallback(Throwable t) {
		return "Say Hello Falling back";
	}

}
