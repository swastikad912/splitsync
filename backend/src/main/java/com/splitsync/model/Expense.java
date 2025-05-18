package com.splitsync.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Expense {

    @Id @GeneratedValue
    private Long id;
    private double amount;
    private String description;
    private String category;

    private String paidBy;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public Group getGroup() {
        return group;
    }

    public List<String> getSplitAmong() {
        return splitAmong;
    }

    public void setSplitAmong(List<String> splitAmong) {
        this.splitAmong = splitAmong;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group group;

    @CollectionTable(
            name = "expense_split_among",
            joinColumns = @JoinColumn(name = "expense_id")

    )
    private List<String> splitAmong;


}