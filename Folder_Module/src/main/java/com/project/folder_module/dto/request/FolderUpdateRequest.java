package com.project.folder_module.dto.request;


import lombok.Data;

import java.time.LocalDate;

@Data
public class FolderUpdateRequest {
    private String folderName;
    private LocalDate creatdate;
    private LocalDate updateAt;
}