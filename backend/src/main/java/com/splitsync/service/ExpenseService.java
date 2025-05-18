package com.splitsync.service;

import com.splitsync.model.Expense;
import com.splitsync.model.Group;
import com.splitsync.repository.ExpenseRepository;
import com.splitsync.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private GroupRepository groupRepo;

    public Expense addExpense(Long groupID , Expense expense)
    {
//        Group g = groupRepo.findById(groupID).orElseThrow();
//        if(!g.isActive()) throw new RuntimeException("Group is not active yet..");
//        expense.setGroup(g);
//
//        return expenseRepo.save(expense);
        Group g = groupRepo.findById(groupID).orElseThrow();
        if (!g.isActive()) throw new RuntimeException("Group is not active yet..");

        expense.setGroup(g); // Link expense to group
        Expense savedExpense = expenseRepo.save(expense); // Save expense first

        g.getExpenses().add(savedExpense); // VERY IMPORTANT: add expense to group's list
        groupRepo.save(g); // Save updated group with new expense attached

        return savedExpense;
    }
}
