package com.example.taska.repository;

import com.example.taska.model.Account;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Cacheable("accounts")
    Optional<Account> findAccountById(Long id);
}
