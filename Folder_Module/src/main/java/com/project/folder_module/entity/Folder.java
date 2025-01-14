package com.project.folder_module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "folder")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String folderId;

    @Column(name = "foldername", nullable = false, length = 250)
    private String folderName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createDate;

    @Column(name = "update_at", nullable = false)
    private LocalDate updatedAt;
}
