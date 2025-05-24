package com.splitsync.controller;

import com.splitsync.model.Expense;
import com.splitsync.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add")
    public Expense addExpense(@RequestBody Expense expense, @RequestParam Long groupId) {
        return expenseService.addExpense(groupId, expense);
    }


}