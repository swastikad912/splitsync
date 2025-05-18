package com.splitsync.service;

import com.splitsync.model.Group;
import com.splitsync.model.User;
import com.splitsync.model.Expense;
import com.splitsync.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepo;
    public Group createGroup(String name , int totalMembers)
    {
        Group g = new Group();
        g.setName(name);
        g.setTotalMembers(totalMembers);

        return groupRepo.save(g);


    }

    public Group getGroup(Long id)
    {
        return groupRepo.findById(id).orElseThrow();
    }

    @Autowired
    private EmailService emailService;

    public void sendGroupSummary(Group group) {
        String summary = calculateSplitSummary(group);
        for (User user : group.getMembers()) {
            emailService.sendEmail(user.getEmail(), "SplitSync Summary", summary);
        }
    }

    public String calculateSplitSummary(Group group) {
        return "Thanks for using SplitSync. Your group '" + group.getName() + "' has completed.\n\nAll balances will be settled soon!";
    }

    public Map<String, Map<String, Double>> calculateSettlements(Group group) {
        Map<String, Double> balances = new HashMap<>();
        Map<String, Map<String, Double>> settlements = new HashMap<>();

        // Initialize balances for all members
        for (User user : group.getMembers()) {
            balances.put(user.getName(), 0.0);
            settlements.put(user.getName(), new HashMap<>());
        }

        // Calculate total expenses per person
        for (Expense expense : group.getExpenses()) {
            double perPersonAmount = expense.getAmount() / expense.getSplitAmong().size();
            balances.put(expense.getPaidBy(), balances.get(expense.getPaidBy()) + expense.getAmount());
            
            for (String member : expense.getSplitAmong()) {
                balances.put(member, balances.get(member) - perPersonAmount);
            }
        }

        // Calculate who owes whom
        while (true) {
            String maxCreditor = null;
            String maxDebtor = null;
            double maxCredit = 0;
            double maxDebt = 0;

            for (Map.Entry<String, Double> entry : balances.entrySet()) {
                if (entry.getValue() > maxCredit) {
                    maxCredit = entry.getValue();
                    maxCreditor = entry.getKey();
                }
                if (entry.getValue() < maxDebt) {
                    maxDebt = entry.getValue();
                    maxDebtor = entry.getKey();
                }
            }

            if (maxCredit < 0.01 && maxDebt > -0.01) break;

            double amount = Math.min(maxCredit, -maxDebt);
            settlements.get(maxDebtor).put(maxCreditor, amount);
            balances.put(maxCreditor, balances.get(maxCreditor) - amount);
            balances.put(maxDebtor, balances.get(maxDebtor) + amount);
        }

        return settlements;
    }
}
