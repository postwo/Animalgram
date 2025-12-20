package com.example.Animalgram.exceptionhandler;


import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)   // 가장 마지막에 실행 적용 // default가 max_value이다 지금은 그냥 명시한거다
public class GlobalExceptionHandler {

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