package com.project.foldermodule.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.foldermodule.entity.Folder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FolderRepository {
    private static final String FILE_PATH = "data/folders.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Folder> readFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Folder>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file", e);
        }
    }


    private void writeToFile(List<Folder> folders) {
        try {
            File file = new File(FILE_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // Tạo thư mục cha nếu chưa có
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writeValue(file, folders);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write JSON file", e);
        }
    }

    public Folder save(Folder folder) {
        List<Folder> folders = readFromFile();
        folders.add(folder);
        writeToFile(folders);
        return folder;
    }

    public List<Folder> findAll() {
        return readFromFile();
    }

    public Optional<Folder> findById(String folderId) {
        return readFromFile().stream().filter(f -> f.getFolderId().equals(folderId)).findFirst();
    }

    public boolean existsByFolderName(String folderName) {
        return readFromFile().stream().anyMatch(f -> f.getFolderName().equalsIgnoreCase(folderName));
    }

    public void deleteById(String folderId) {
        List<Folder> folders = readFromFile();
        folders.removeIf(f -> f.getFolderId().equals(folderId));
        writeToFile(folders);
    }
}