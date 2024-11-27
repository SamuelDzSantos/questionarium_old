package br.com.questionarium.question_service.model;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_alternative")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alternative {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name= "description")
    private String description;

    @Column(name= "image_path")
    private String imagePath;

    @Column(name= "is_correct")
    private Boolean isCorrect;

    @Column(name= "explanation")
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    //Lombok
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alternative that = (Alternative) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Alternative{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }


}
