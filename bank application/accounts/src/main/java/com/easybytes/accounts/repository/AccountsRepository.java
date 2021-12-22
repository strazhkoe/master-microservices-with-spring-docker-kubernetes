package com.easybytes.accounts.repository;

import org.springframework.stereotype.Repository;

import com.easybytes.accounts.model.Accounts;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface AccountsRepository extends CrudRepository<Accounts, Long> {
	Accounts findByCustomerId(int customerId);
}
