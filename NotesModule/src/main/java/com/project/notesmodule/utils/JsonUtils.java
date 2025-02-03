package com.project.notesmodule.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.notesmodule.entity.Note;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonUtils {
    public static void writePrettyJsonToFile(List<Note> notes, String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Kích hoạt pretty print
        objectMapper.writeValue(new File(filePath), notes); // Ghi dữ liệu vào file
    }
}
