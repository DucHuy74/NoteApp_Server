package com.project.notesmodule.controller;

import com.project.notesmodule.Service.NoteService;
import com.project.notesmodule.dto.request.NoteCreationRequest;
import com.project.notesmodule.dto.request.NoteUpdateRequest;
import com.project.notesmodule.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    // Tạo ghi chú mới
    @PostMapping()
    public Note createNote(@RequestBody NoteCreationRequest request) {
        // Gọi service để tạo ghi chú và tự động lưu vào file JSON
        return noteService.createNote(request);
    }

    // Lấy danh sách ghi chú
    @GetMapping()
    public List<Note> getNotes() {
        return noteService.getNotes();
    }

    // Lấy ghi chú theo ID
    @GetMapping("/{noteId}")
    public Note getNote(@PathVariable("noteId") String noteId) {
        return noteService.getNote(noteId);
    }

    // Cập nhật ghi chú
    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable String noteId, @RequestBody NoteUpdateRequest request) {
        // Gọi service để cập nhật ghi chú và tự động lưu vào file JSON
        return noteService.updateNote(noteId, request);
    }

    // Xóa ghi chú
    @DeleteMapping("/{noteId}")
    public String deleteNote(@PathVariable String noteId) {
        noteService.deleteNote(noteId);
        return "Note has been deleted";
    }
}
