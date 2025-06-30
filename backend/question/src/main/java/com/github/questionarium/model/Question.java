package com.github.questionarium.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.github.questionarium.types.QuestionAccessLevel;
import com.github.questionarium.types.QuestionEducationLevel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean multipleChoice;

    @Column(nullable = false)
    private Integer numberLines;

    @Enumerated(EnumType.ORDINAL)
    @Column()
    private QuestionEducationLevel educationLevel;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "header", nullable = false, columnDefinition = "TEXT")
    private String header;

    @Column(name = "header_image", nullable = true)
    private String headerImage;

    @Column(name = "answer_id", nullable = true)
    private Long answerId;

    @Column(nullable = false)
    private boolean enable;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "access_type")
    private QuestionAccessLevel accessLevel;

    @ManyToMany
    @JoinTable(name = "question_tags", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Set<Alternative> alternatives;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDateTime;

    @Column(nullable = false)
    private LocalDateTime updateDateTime;

    @PrePersist
    protected void onCreate() {
        this.creationDateTime = LocalDateTime.now();
        this.updateDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Question question = (Question) o;
        return id != null && id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
