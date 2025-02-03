package com.project.notesmodule.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    INVALID_KEY(1001, "Invalid message Key"),
    NOTE_EXISTED(1002, "Note existed"),
    NOTE_NOT_FOUND(1003, "Note not found"),
    FILE_WRITE_ERROR(1004, "File write error"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
