package com.easybytes.cards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.easybytes.cards.model.Cards;
import com.easybytes.cards.model.Customer;
import com.easybytes.cards.repository.CardsRepository;

@RestController
public class CardsController {
	
	@Autowired
	private CardsRepository cardsRepository;
	
	@PostMapping("/myCards")
	public List<Cards> getCardDetail(@RequestBody Customer customer) {
		List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());

		return cards;
	}
}
