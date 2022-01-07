package com.easybytes.accounts.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.easybytes.accounts.model.Cards;
import com.easybytes.accounts.model.Customer;

@FeignClient("cards")
public interface CardsFeignClient {

	@RequestMapping(method = RequestMethod.POST, value = "myCards", consumes = "application/json")
	public List<Cards> getCardsDetails(@RequestHeader("easybank-correlation-id") String correlationId,
			                           @RequestBody Customer customer);
}
