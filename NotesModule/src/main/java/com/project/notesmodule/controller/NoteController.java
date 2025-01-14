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

    @PostMapping()
    Note createNote(@RequestBody NoteCreationRequest request) {
        return noteService.createNote(request);
    }

    @GetMapping()
    List<Note> getNotes() {
        return noteService.getNotes();
    }

    @GetMapping("/{noteId}")
    Note getNote(@PathVariable("noteId") String noteId) {
        return noteService.getNote(noteId);
    }

    @PutMapping("/{noteId}")
    Note updateNote(@PathVariable String noteId, @RequestBody NoteUpdateRequest request) {
        return noteService.updateNote(noteId, request);
    }

    @DeleteMapping("/{noteId}")
    String deleteNote(@PathVariable String noteId) {
        noteService.deleteNote(noteId);
        return "Note has been deleted";
    }
}
