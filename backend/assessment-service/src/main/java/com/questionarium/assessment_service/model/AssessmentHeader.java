package com.questionarium.assessment_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "assessment_header")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    private String institution;

    private String department;

    private String course;

    private String classroom;

    private String professor; // NOME QUE APARECE NO CABECALHO

    // @Lob
    private String instructions;

    private String image;

    @Column(updatable = false)
    @NotNull
    private LocalDateTime creationDateTime;
    
    @NotNull
    private LocalDateTime updateDateTime;

    @PrePersist
    protected void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (creationDateTime == null) {
            creationDateTime = now;
            updateDateTime = now;
        }
    }

    @PreUpdate
    protected void onPreUpdate() {
        updateDateTime = LocalDateTime.now();
    }
}
