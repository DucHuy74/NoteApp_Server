package com.project.foldermodule.entity;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Folder {
    private String folderId;

    private String folderName;

    private LocalDate createDate;

    private LocalDate updatedAt;
}