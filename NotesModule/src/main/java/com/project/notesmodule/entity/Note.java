package com.project.notesmodule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private String noteId;
    private String content;
    private LocalDate createAt = LocalDate.now();
    private LocalDate updateAt = LocalDate.now();
}