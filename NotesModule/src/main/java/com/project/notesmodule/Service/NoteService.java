package com.project.notesmodule.Service;

import com.project.notesmodule.dto.request.NoteCreationRequest;
import com.project.notesmodule.dto.request.NoteUpdateRequest;
import com.project.notesmodule.entity.Note;
import com.project.notesmodule.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NoteService implements NoteServiceImpl{
    @Autowired
    private NoteRepository noteRepository;

    public Note createNote(NoteCreationRequest request) {
        Note note = new Note();

        note.setContent(request.getContent());
        note.setCreateAt(LocalDate.now());
        note.setUpdateAt(LocalDate.now());

        return noteRepository.save(note);
    }

    public Note updateNote(String noteId, NoteUpdateRequest request) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found to be updated"));

        note.setContent(request.getContent());
        note.setUpdateAt(LocalDate.now()); // Tự động cập nhật ngày hiện tại

        return noteRepository.save(note);
    }


    public void deleteNote(String noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found to be delete"));

        noteRepository.deleteById(noteId);
    }

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    public Note getNote(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }
}
