package com.project.folder_module.repository;

import com.project.folder_module.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, String> {
    boolean existsByFolderName(String folderName);
}