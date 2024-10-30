package org.ufpr.questionarium.models;

import java.util.Date;

import jakarta.persistence.Entity;
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
public class Class {
    private String name;
    private Date creationDate;
    private Date updateDate;
}
