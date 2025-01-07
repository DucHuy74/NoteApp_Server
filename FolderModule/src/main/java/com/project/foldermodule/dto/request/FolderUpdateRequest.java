package com.project.foldermodule.dto.request;

import lombok.Data;

import java.time.LocalDate;


@Data
public class FolderUpdateRequest {
    private String folderName;
    private LocalDate creatdate;
    private LocalDate updateAt;
}
