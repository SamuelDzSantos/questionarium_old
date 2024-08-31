package dev.questionarium.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "password_tokens", schema = "user_schema")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = true, unique = true)
    private String token;
    @Column(nullable = false)
    private Date expirationDate;
    @Column(nullable = false)
    String code;
}
