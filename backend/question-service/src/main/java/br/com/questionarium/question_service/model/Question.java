package br.com.questionarium.question_service.model;

import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import br.com.questionarium.question_service.types.QuestionAccessLevel;
import br.com.questionarium.question_service.types.QuestionEducationLevel;
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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean multipleChoice;

    @Column(nullable = false)
    private Integer numberLines;

    @Column()
    @Enumerated(EnumType.ORDINAL)
    private QuestionEducationLevel educationLevel;

    @Column(name = "person_id", nullable = false)
    private Long personId;

    @Column(name = "header_id", nullable = false)
    private Long headerId;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(nullable = false)
    private boolean enable;

    @Column(name = "access_type")
    @Enumerated(EnumType.ORDINAL)
    private QuestionAccessLevel accessLevel;

    @ManyToMany
    @JoinTable(
        name = "question_tags",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    
    private Set<Alternative> alternatives;

    //Lombok
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id != null && id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
