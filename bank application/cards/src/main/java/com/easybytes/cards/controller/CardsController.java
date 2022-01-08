package com.easybytes.cards.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.easybytes.cards.model.Properties;
import com.easybytes.cards.config.CardsServiceConfig;
import com.easybytes.cards.model.Cards;
import com.easybytes.cards.model.Customer;
import com.easybytes.cards.repository.CardsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
public class CardsController {
	
	private static final Logger logger = LoggerFactory.getLogger(CardsController.class);
	
	@Autowired
	private CardsRepository cardsRepository;
	
	@Autowired
	private CardsServiceConfig cardsConfig;
	
	@PostMapping("/myCards")
	public List<Cards> getCardDetails(@RequestHeader(name = "easybank-correlation-id", required = false) String correlationId,
			                         @RequestBody Customer customer) {
		logger.info("getCardDetails() method started");
		List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
		logger.info("getCardDetails() method started");

		return cards;
	}
	
	@GetMapping("/cards/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(),
				cardsConfig.getMailDetails(), cardsConfig.getActiveBranches());
		String result = ow.writeValueAsString(properties);
		return result;
	}
}
