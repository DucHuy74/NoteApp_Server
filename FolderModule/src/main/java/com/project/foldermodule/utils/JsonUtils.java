package com.project.foldermodule.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.foldermodule.entity.Folder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonUtils {
    public static void writePrettyJsonToFile(List<Folder> folders, String FILE_PATH) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File(FILE_PATH), folders);
    }
}
