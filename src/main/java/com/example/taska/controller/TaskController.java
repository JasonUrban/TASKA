package com.example.taska.controller;

import com.example.taska.exception.AccountNotFoundException;
import com.example.taska.model.Account;
import com.example.taska.service.BalanceServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class TaskController {
    private final BalanceServiceImpl service;

    @Autowired
    public TaskController(BalanceServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/accounts")
    public List<Account> findAll() {
        return service.findAll();
    }

    @GetMapping("/balance")
    public Long getBalance(@Valid @Positive @RequestParam Long id) {
        return service.getBalance(id).orElseThrow(() -> new AccountNotFoundException(id));
    }

    @PutMapping("/transfer")
    public void changeBalance(@Valid @Positive @RequestParam Long id,  @Valid @Positive @RequestParam Long amount) {
        service.changeBalance(id, amount);
    }
}
