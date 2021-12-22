package com.easybytes.accounts.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Accounts {
	@Column(name="customer_id")
	private int customerId;
	
	@Id
	@Column(name="account_number")
	private long accountNumber;
	
	@Column(name="account_type")
	private String accountType;
	
	@Column(name="branchAddress")
	private String branchAddress;
	
	@Column(name="create_dt")
	private LocalDate createDt;
}
