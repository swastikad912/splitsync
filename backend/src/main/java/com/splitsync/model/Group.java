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
@Table(name = "groups") // "group" is a reserved word
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();
}
