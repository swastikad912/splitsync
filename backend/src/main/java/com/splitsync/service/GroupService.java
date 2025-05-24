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
        if (group == null) {
            throw new RuntimeException("Group cannot be null");
        }

        if (group.getMembers() == null || group.getMembers().isEmpty()) {
            throw new RuntimeException("Group has no members");
        }

        if (group.getExpenses() == null || group.getExpenses().isEmpty()) {
            throw new RuntimeException("Group has no expenses");
        }

        Map<String, Double> balances = new HashMap<>();
        Map<String, Map<String, Double>> settlements = new HashMap<>();

        // Initialize balances for all members
        for (User user : group.getMembers()) {
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                throw new RuntimeException("User name cannot be null or empty");
            }
            balances.put(user.getName(), 0.0);
            settlements.put(user.getName(), new HashMap<>());
        }

        // Calculate total expenses per person
        for (Expense expense : group.getExpenses()) {
            if (expense.getSplitAmong() == null || expense.getSplitAmong().isEmpty()) {
                throw new RuntimeException("Expense must be split among at least one person");
            }
            if (expense.getPaidBy() == null || expense.getPaidBy().trim().isEmpty()) {
                throw new RuntimeException("Expense must have a payer");
            }
            
            double perPersonAmount = expense.getAmount() / expense.getSplitAmong().size();
            balances.put(expense.getPaidBy(), balances.get(expense.getPaidBy()) + expense.getAmount());
            
            for (String member : expense.getSplitAmong()) {
                if (!balances.containsKey(member)) {
                    throw new RuntimeException("Member " + member + " not found in group");
                }
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

        // Send settlement emails to all members
        for (User user : group.getMembers()) {
            try {
                if (user.getEmail() != null && !user.getEmail().isBlank()) {
                    StringBuilder emailContent = new StringBuilder();
                    emailContent.append("Hello ").append(user.getName()).append(",\n\n");
                    emailContent.append("Here are your settlement details for group '").append(group.getName()).append("':\n\n");

                    // Add what others owe to this user
                    boolean hasCredits = false;
                    for (Map.Entry<String, Map<String, Double>> entry : settlements.entrySet()) {
                        String debtor = entry.getKey();
                        Map<String, Double> debts = entry.getValue();
                        if (debts.containsKey(user.getName())) {
                            hasCredits = true;
                            emailContent.append("• ").append(debtor).append(" owes you ₹").append(debts.get(user.getName())).append("\n");
                        }
                    }

                    // Add what this user owes to others
                    Map<String, Double> userDebts = settlements.get(user.getName());
                    if (userDebts != null && !userDebts.isEmpty()) {
                        emailContent.append("\nYou need to pay:\n");
                        for (Map.Entry<String, Double> debt : userDebts.entrySet()) {
                            emailContent.append("• ₹").append(debt.getValue()).append(" to ").append(debt.getKey()).append("\n");
                        }
                    }

                    if (!hasCredits && (userDebts == null || userDebts.isEmpty())) {
                        emailContent.append("You have no pending settlements.\n");
                    }

                    emailContent.append("\nThank you for using SplitSync! 🎉");
                    
                    emailService.sendEmail(
                        user.getEmail(),
                        "SplitSync: Settlement Details for Group - " + group.getName(),
                        emailContent.toString()
                    );
                }
            } catch (Exception e) {
                // Log the error but continue processing other users
                System.err.println("Failed to send email to " + user.getEmail() + ": " + e.getMessage());
            }
        }

        return settlements;
    }
}
