package com.project.notesmodule.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Import JavaTimeModule
import com.project.notesmodule.entity.Note;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class NoteRepository {

    private final String filePath = "notesmodule/data/notes.json"; // Đường dẫn tới file JSON
    private final ObjectMapper objectMapper;

    // Constructor với ObjectMapper đã cấu hình JavaTimeModule
    public NoteRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Đăng ký JavaTimeModule để hỗ trợ LocalDate
    }

    // Kiểm tra và tạo tệp nếu không tồn tại
    private void checkAndCreateFile() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile(); // Tạo tệp nếu không tồn tại
        }
    }

    // Đọc dữ liệu từ file JSON
    public List<Note> getAllNotes() {
        try {
            checkAndCreateFile(); // Kiểm tra và tạo file nếu cần
            File file = new File(filePath);
            if (file.length() == 0) {
                return new ArrayList<>();  // Trả về danh sách rỗng nếu file trống
            }
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Note.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();  // Trả về danh sách rỗng nếu có lỗi
        }
    }

    // Lưu danh sách ghi chú vào file JSON
    // Lưu danh sách ghi chú vào file JSON
    public void saveAllNotes(List<Note> notes) {
        try {
            if (notes == null || notes.isEmpty()) {
                System.out.println("Không có ghi chú để lưu.");
                return;  // Nếu không có ghi chú, không cần ghi vào file
            }
            checkAndCreateFile();

            // Sử dụng ObjectMapper với tính năng pretty print
            ObjectMapper objectMapperWithPrettyPrint = new ObjectMapper();
            objectMapperWithPrettyPrint.registerModule(new JavaTimeModule()); // Đăng ký JavaTimeModule để hỗ trợ LocalDate
            objectMapperWithPrettyPrint.enable(SerializationFeature.INDENT_OUTPUT); // Kích hoạt pretty print

            objectMapperWithPrettyPrint.writeValue(new File(filePath), notes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Thêm một ghi chú mới
    public void addNote(Note note) {
        List<Note> notes = getAllNotes();
        notes.add(note);
        saveAllNotes(notes);
    }

    // Cập nhật một ghi chú
    public void updateNote(String noteId, Note updatedNote) {
        List<Note> notes = getAllNotes();
        for (Note note : notes) {
            if (note.getNoteId().equals(noteId)) {
                note.setContent(updatedNote.getContent());
                note.setUpdateAt(updatedNote.getUpdateAt());
                break;
            }
        }
        saveAllNotes(notes);
    }

    // Xóa một ghi chú
    public void deleteNote(String noteId) {
        List<Note> notes = getAllNotes();
        notes.removeIf(note -> note.getNoteId().equals(noteId));
        saveAllNotes(notes);
    }

    // Tạo ID mới cho ghi chú
    public String generateNewId() {
        return UUID.randomUUID().toString();
    }
}
