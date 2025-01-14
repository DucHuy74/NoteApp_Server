package com.project.folder_module.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FolderCreationRequest {
    @NotBlank(message = "Folder name must not be blank")
    @Size(max = 250, message = "Folder name must not exceed 250 characters")
    private String folderName;
    private LocalDate creatdate;
    private LocalDate updateAt;
}
