package com.project.foldermodule.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    INVALID_KEY(1001, "Invalid message Key"),
    FOLDER_EXISTED(1002, "Folder existed"),
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
