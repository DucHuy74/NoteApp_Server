package com.project.notesmodule.Service;

import com.project.notesmodule.dto.request.NoteCreationRequest;
import com.project.notesmodule.dto.request.NoteUpdateRequest;
import com.project.notesmodule.entity.Note;
import com.project.notesmodule.exception.AppException;
import com.project.notesmodule.exception.ErrorCode;
import com.project.notesmodule.repository.NoteRepository;
import com.project.notesmodule.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note createNote(NoteCreationRequest request) {
        List<Note> notes = noteRepository.getAllNotes();

        // Kiểm tra xem ghi chú đã tồn tại chưa
        for (Note note : notes) {
            if (note.getContent().equals(request.getContent())) {
                throw new AppException(ErrorCode.NOTE_EXISTED);
            }
        }

        // Tạo ghi chú mới
        Note note = new Note();
        note.setNoteId(noteRepository.generateNewId()); // Tạo ID mới cho ghi chú
        note.setContent(request.getContent());
        note.setCreateAt(LocalDate.now());
        note.setUpdateAt(LocalDate.now());

        // Thêm ghi chú vào danh sách và lưu vào file JSON
        noteRepository.addNote(note);

        return note;
    }

    public Note updateNote(String noteId, NoteUpdateRequest request) {
        List<Note> notes = noteRepository.getAllNotes();

        // Tìm ghi chú theo ID
        Note note = notes.stream()
                .filter(n -> n.getNoteId().equals(noteId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        // Cập nhật nội dung ghi chú
        note.setContent(request.getContent());
        note.setUpdateAt(LocalDate.now());

        // Lưu lại vào file JSON
        noteRepository.updateNote(noteId, note);

        return note;
    }

    public void deleteNote(String noteId) {
        List<Note> notes = noteRepository.getAllNotes();

        // Kiểm tra nếu ghi chú không tồn tại
        Note note = notes.stream()
                .filter(n -> n.getNoteId().equals(noteId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        // Xóa ghi chú
        noteRepository.deleteNote(noteId);
    }

    public List<Note> getNotes() {
        return noteRepository.getAllNotes();
    }

    public Note getNote(String id) {
        List<Note> notes = noteRepository.getAllNotes();

        // Tìm ghi chú theo ID
        return notes.stream()
                .filter(note -> note.getNoteId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
    }

    private static final String JSON_FILE_PATH = "notesmodule/notes.json";

    public void saveNotesToFile(List<Note> notes) throws IOException {
        JsonUtils.writePrettyJsonToFile(notes, JSON_FILE_PATH);
    }
}
