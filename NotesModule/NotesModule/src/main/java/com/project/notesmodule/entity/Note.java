package com.project.notesmodule.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String noteId;
    String content;
    LocalDate createAt;
    LocalDate updateAt;
}
