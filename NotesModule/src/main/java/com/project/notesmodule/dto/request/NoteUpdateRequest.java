package com.project.notesmodule.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteUpdateRequest {
    String content;
    LocalDate createAt;
    LocalDate updateAt;
}
