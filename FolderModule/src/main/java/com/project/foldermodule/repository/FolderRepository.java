package com.project.foldermodule.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.foldermodule.entity.Folder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FolderRepository {
    private static final String FILE_PATH = "FolderModule/data/folders.json";
    private final ObjectMapper objectMapper;
    public FolderRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); //dki javatime de sp localdate
    }

    //kiem tra va tao tep neu ko ton tai
    private void checkAndCreateFile() throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists()) {
            file.createNewFile();
        }
    }

    //doc du lieu tu file json
    public List<Folder> getAllFolders() {
        try {
            checkAndCreateFile();
            File file = new File(FILE_PATH);
            if(file.length() == 0){
                return new ArrayList<>(); //tra ve dsach rong neu file trong
            }
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Folder.class));
        }catch (IOException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveAllFolders(List<Folder> folders) {
        try {
            if(folders == null || folders.isEmpty()){
                return;
            }
            checkAndCreateFile();

            ObjectMapper objectMapperWithPrettyPrint = new ObjectMapper();
            objectMapperWithPrettyPrint.registerModule(new JavaTimeModule());
            objectMapperWithPrettyPrint.enable(SerializationFeature.INDENT_OUTPUT);

            objectMapperWithPrettyPrint.writeValue(new File(FILE_PATH), folders);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addFolder(Folder folder) {
        List<Folder> folders = getAllFolders();
        folders.add(folder);
        saveAllFolders(folders);
    }

    public void updateFolder(String folderId, Folder updatefolder) {
        List<Folder> folders = getAllFolders();
        for (Folder folder : folders) {
            if (folder.getFolderId().equals(folderId)) {
                folder.setFolderName(updatefolder.getFolderName());
                break;
            }
        }
        saveAllFolders(folders);
    }

    public void deleteFolder(String folderId) {
        List<Folder> folders = getAllFolders();
        folders.removeIf(folder -> folder.getFolderId().equals(folderId));
        saveAllFolders(folders);
    }

    public String generateFolderId() {
        return UUID.randomUUID().toString();
    }
}