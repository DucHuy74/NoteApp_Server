package com.project.folder_module.service;

import com.project.folder_module.dto.request.FolderCreationRequest;
import com.project.folder_module.dto.request.FolderUpdateRequest;
import com.project.folder_module.entity.Folder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FolderServiceImpl {
    Folder createrFolder(FolderCreationRequest request);
    Folder updateFolder(String folderId , FolderUpdateRequest request);
    void deleteFolder(String folderId);
    List<Folder> getAllFolders();
    Folder getFolder(String folderId);
}
