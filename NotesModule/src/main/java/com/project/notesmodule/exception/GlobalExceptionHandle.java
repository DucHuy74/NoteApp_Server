package com.project.notesmodule.exception;

import com.project.notesmodule.dto.request.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandle {

    // Xử lý tất cả ngoại lệ chưa được phân loại
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception exception) {
        logException(exception);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    // Xử lý ngoại lệ của ứng dụng (AppException)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        logException(exception);
        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        HttpStatus status = errorCode == ErrorCode.NOTE_NOT_FOUND ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(apiResponse);
    }

    // Xử lý ngoại lệ validation (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException exception) {
        logException(exception);

        String enumKey = "INVALID_KEY"; // Giá trị mặc định nếu không có thông báo lỗi cụ thể
        if (exception.getFieldError() != null) {
            enumKey = exception.getFieldError().getDefaultMessage();
        }

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            // Nếu không tìm thấy trong enum, giữ nguyên giá trị mặc định
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    // Xử lý ngoại lệ RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception) {
        logException(exception);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage("An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    // Hàm log ngoại lệ để theo dõi chi tiết lỗi
    private void logException(Exception exception) {
        // Sử dụng logger (như Logback, SLF4J) để ghi lại lỗi
        // Ví dụ: LoggerFactory.getLogger(GlobalExceptionHandle.class).error("Exception: ", exception);
        System.err.println("Exception caught: " + exception.getMessage());
        exception.printStackTrace();
    }
}
