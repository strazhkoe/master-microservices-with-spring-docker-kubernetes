package com.easybytes.accounts.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Loans {

	private int loanNumber;
	
	private int customerId;
	
	private Date startDt;
	
    private String loanType;
	
	private int totalLoan;

	private int amountPaid;
	
	private int outstandingAmount;
	
	private String createDt;
	
}
