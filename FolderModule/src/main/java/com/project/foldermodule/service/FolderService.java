package com.project.foldermodule.service;

import com.project.foldermodule.dto.request.FolderCreationRequest;
import com.project.foldermodule.dto.request.FolderUpdateRequest;
import com.project.foldermodule.entity.Folder;
import com.project.foldermodule.exception.AppException;
import com.project.foldermodule.exception.ErrorCode;
import com.project.foldermodule.repository.FolderRepository;
import com.project.foldermodule.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;

    public Folder createFolder(FolderCreationRequest request) {
        List<Folder> folders = folderRepository.getAllFolders();
        for (Folder folder : folders) {
            if (folder.getFolderName().equals(request.getFolderName())) {
                throw new AppException(ErrorCode.FOLDER_EXISTED);
            }
        }

        Folder folder = new Folder();
        folder.setFolderId(folderRepository.generateFolderId());
        folder.setFolderName(request.getFolderName());

        folderRepository.addFolder(folder);
        return folder;
    }

    public Folder updateFolder(String folderId, FolderUpdateRequest request) {
        List<Folder> folders = folderRepository.getAllFolders();

        Folder folder = folders.stream()
                        .filter(n->n.getFolderId().equals(folderId))
                        .findFirst()
                        .orElseThrow(()-> new AppException(ErrorCode.INVALID_KEY));


        folder.setFolderName(request.getFolderName());
        folderRepository.updateFolder(folderId, folder);
        return folder;
    }

    public void deleteFolder(String folderId) {
        List<Folder> folders = folderRepository.getAllFolders();
        Folder folder = folders.stream()
                .filter(n->n.getFolderId().equals(folderId))
                .findFirst()
                .orElseThrow(()-> new AppException(ErrorCode.INVALID_KEY));
        folderRepository.deleteFolder(folderId);
    }

    public List<Folder> getFolders() {
        return folderRepository.getAllFolders();
    }

    public Folder getFolder(String folderId) {
        List<Folder> folders = folderRepository.getAllFolders();
        return folders.stream()
                .filter(n->n.getFolderId().equals(folderId))
                .findFirst()
                .orElseThrow(()-> new AppException(ErrorCode.INVALID_KEY));
    }

    private static final String JSON_FILE_PATH = "FolderModule/data/folder.json";

    public void saveFolderToFile(List<Folder> folders) throws IOException {
        JsonUtils.writePrettyJsonToFile(folders, JSON_FILE_PATH);
    }
}