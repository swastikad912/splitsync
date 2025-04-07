package com.splitsync.model;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String category;
    private String description;

    @ManyToOne
    private User paidBy;

    @ManyToMany
    private List<User> splitAmong = new ArrayList<>();
}
