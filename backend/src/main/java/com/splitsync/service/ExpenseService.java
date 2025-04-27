package com.splitsync.service;

import com.splitsync.model.Expense;
import com.splitsync.model.Group;
import com.splitsync.repository.ExpenseRepository;
import com.splitsync.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenceRepo;

    @Autowired
    private GroupRepository groupRepo;

    public Expense addExpence(Long groupID , Expense expense)
    {
        Group g = groupRepo.findById(groupID).orElseThrow();
        if(!g.isActive()) throw new RuntimeException("Group is not active yet..");
        expense.setGroup(g);

        
    }
}
