package com.project.notesmodule.Service;

import com.project.notesmodule.dto.request.NoteCreationRequest;
import com.project.notesmodule.dto.request.NoteUpdateRequest;
import com.project.notesmodule.entity.Note;

import java.util.List;

public interface NoteServiceImpl {
    Note createNote(NoteCreationRequest request);
    Note updateNote(String noteId, NoteUpdateRequest request);
    void deleteNote(String noteId);
    List<Note> getNotes();
    Note getNote(String id);
}
