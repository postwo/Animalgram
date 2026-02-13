package com.example.Animalgram.exceptionhandler;


import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)   // 가장 마지막에 실행 적용 // default가 max_value이다 지금은 그냥 명시한거다
public class GlobalExceptionHandler {

    // validation(@valid) 에러 처리
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Api<Object>> handleValidationExceptions(Exception ex) {
        log.error("Validation error: ", ex);

        if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                    .collect(Collectors.joining(", "));
            return ResponseEntity
                    .status(400)
                    .body(Api.ERROR(ErrorCode.BAD_REQUEST, errorMessage));
        }

        return ResponseEntity
                .status(400)
                .body(Api.ERROR(ErrorCode.BAD_REQUEST, "잘못된 요청 형식입니다."));
    }

    //모든 exception 은 다 캐치
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception (
            Exception exception
    ){
        log.error("",exception);

        return ResponseEntity
                .status(500) //서버에서 난 에러여서 그냥 500으로 처리
                .body(
                        Api.ERROR(ErrorCode.SERVER_ERROR)
                );
    }
}
