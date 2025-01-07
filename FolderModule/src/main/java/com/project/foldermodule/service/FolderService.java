package com.project.foldermodule.service;


import com.project.foldermodule.dto.request.FolderCreationRequest;
import com.project.foldermodule.dto.request.FolderUpdateRequest;
import com.project.foldermodule.entity.Folder;
import com.project.foldermodule.exception.AppException;
import com.project.foldermodule.exception.ErrorCode;
import com.project.foldermodule.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;

    public Folder createFolder(FolderCreationRequest request){
        Folder folder = new Folder();
        if(folderRepository.existsByFolderName(request.getFolderName())){
            throw new AppException(ErrorCode.FOLDER_EXISTED);
        }
        folder.setFolderName(request.getFolderName());
        folder.setCreateDate(LocalDate.now());
        folder.setUpdatedAt(LocalDate.now());

        return folderRepository.save(folder);
    }

    public Folder updateFolder(String folderId ,FolderUpdateRequest request){
        Folder folder = getFolder(folderId);

        if(folderRepository.existsByFolderName(request.getFolderName())){
            throw new AppException(ErrorCode.FOLDER_EXISTED);
        }

        folder.setFolderName(request.getFolderName());
        folder.setUpdatedAt(LocalDate.now());

        return folderRepository.save(folder);
    }

    public void deleteFolder(String folderId){
        folderRepository.deleteById(folderId);
    }

    public List<Folder> getFolders(){
        return folderRepository.findAll();
    }

    public Folder getFolder(String folderId){
        return folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
    }

}