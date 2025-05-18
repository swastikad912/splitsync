package com.splitsync.repository;

import com.splitsync.model.Expense;
import com.splitsync.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup(Group group);
}