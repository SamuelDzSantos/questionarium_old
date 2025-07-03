package com.github.questionarium.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @Column(nullable = false)
    private LocalDateTime updateDateTime;

    @Lob
    private byte[] image;

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.creationDateTime == null) {
            this.creationDateTime = now;
        }
        this.updateDateTime = now;
    }

    @PreUpdate
    protected void preUpdate() {
        this.updateDateTime = LocalDateTime.now();
    }

}
