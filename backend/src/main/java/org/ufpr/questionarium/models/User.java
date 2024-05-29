package org.ufpr.questionarium.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.ufpr.questionarium.types.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDate creationDateAccount;
    private List<Role> roles = new ArrayList<>();
}