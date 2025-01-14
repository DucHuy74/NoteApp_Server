package com.project.folder_module.controller;

import com.project.folder_module.dto.request.ApiResponse;
import com.project.folder_module.dto.request.FolderCreationRequest;
import com.project.folder_module.dto.request.FolderUpdateRequest;
import com.project.folder_module.entity.Folder;
import com.project.folder_module.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folder")
public class FolderController {
    @Autowired
    private FolderService folderService;

    @PostMapping("/createFolder")
    ApiResponse<Folder> createFolder(@RequestBody @Valid FolderCreationRequest request) {
        ApiResponse<Folder> apiResponse = new ApiResponse<>();
        apiResponse.setResult(folderService.createFolder(request));
        return apiResponse;
    }

    @GetMapping
    List<Folder> getFolders() {
        return folderService.getFolders();
    }

    @GetMapping("/{folderid}")
    Folder getFolder(@PathVariable("folderid") String folderid) {
        return folderService.getFolder(folderid);
    }

    @PutMapping("/{folderid}")
    Folder updateFolder(@PathVariable String folderid, @RequestBody FolderUpdateRequest folder){
        return folderService.updateFolder(folderid, folder);
    }

    @DeleteMapping("/{folderid}")
    String deleteFolder(@PathVariable("folderid") String folderid) {
        folderService.deleteFolder(folderid);
        return "Folder has been deleted";
    }
}
