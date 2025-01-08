package angel_bridge.angel_bridge_server.global.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ExceptionResponse> handleBadRequestException(ApplicationException e){

        log.error("BadRequestException 발생: {}", e.getMessage(), e);

        return ResponseEntity.status(e.getExceptionCode().getHttpStatus())
                .body(new ExceptionResponse(e.getExceptionCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("MethodArgumentNotValidException 발생: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(ExceptionCode.INVALID_VALUE_EXCEPTION, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e){
        log.error("UnhandledException 발생: {}", e.getMessage(), e);

        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        log.error("HttpMessageNotReadableException 발생: {}", e.getMessage(), e);

        String message = "요청 데이터 형식이 올바르지 않습니다." +
                "( - 날짜/시간 형식은 'yyyy-MM-dd'T'HH:mm' 형식을 사용해야 합니다. " +
                "- 필수 값이 누락되지 않았는지 확인하세요. " +
                "- JSON 구조가 유효한지 확인하세요. )";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(ExceptionCode.INVALID_REQUEST_FORMAT, message));
    }
}