package com.easybank.gatewayserver.filters;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


@Order(1)
@Component
public class TraceFilter implements GlobalFilter {

	private static final Logger logger = LoggerFactory.getLogger(TraceFilter.class);
	
	@Autowired
	private FilterUtility filterUtility;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders headers = exchange.getRequest().getHeaders();
		if (isCorrelationIdPresent(headers)) {
			logger.debug("Easybank-correlation-id found in tracing filter: {}. ",
					filterUtility.getCorrelationId(headers));
		} else {
			String correlationId = generateCorrelationId();
			exchange = filterUtility.setCorrelationId(exchange, correlationId);
			logger.debug("Easybank-correlation-id generated in tracing filter {}." , correlationId);
		}
		
		return chain.filter(exchange);
	}

	private String generateCorrelationId() {
		return UUID.randomUUID().toString();
	}

	private boolean isCorrelationIdPresent(HttpHeaders headers) {
		return filterUtility.getCorrelationId(headers) != null;
	}
	
}
