package br.com.questionarium.question_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_alternative")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "alternative_order", nullable = false)
    private Integer alternativeOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Alternative that = (Alternative) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
