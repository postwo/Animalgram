package com.example.Animalgram.exceptionhandler;


import com.example.Animalgram.common.api.Api;
import com.example.Animalgram.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)   // 최우선처리
public class ApiExceptionHandler {

    /*
    // orElseThrow를 사용한 예외 발생
    memberRepository.findByMemberId(id)
    .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "회원을 찾을 수 없습니다"));

    // 또는 직접 throw
    throw new ApiException(TokenErrorCode.EXPIRED_TOKEN);

    이렇게 요청하게 되면 여기서 캐치해서
    이게 apiException이 동작한다

    apiException 메서드에서 ResponseEntity로 감싸서 처리하기 떄문에 controller에서는 ResponseEntity<>로
    감싸지 않고 바로 api<> 이것만써도 된다
    */

    /*
     ApiException apiException 매개변수로 넘겨지는 원리

     1️⃣ 서비스에서 예외 발생
     throw new ApiException(ErrorCode.BAD_REQUEST, "회원을 찾을 수 없습니다");

     2️⃣ Spring이 예외를 캐치
    서비스 → 예외 발생 → Spring 프레임워크가 감지

     3️⃣ @ExceptionHandler가 자동으로 호출
    @ExceptionHandler(value = ApiException.class)  // ← ApiException 타입의 예외가 발생하면
    public ResponseEntity<Api<Object>> apiException(
            ApiException apiException  // ← Spring이 발생한 예외 객체를 자동으로 주입
    ){
        // apiException에는 발생한 예외 객체가 들어있음
        var errorCode = apiException.getErrorCodeIfs();  // 예외에서 에러 코드 추출
        var description = apiException.getErrorDescription();  // 예외에서 설명 추출
    }
    */

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Api<Object>> apiException(
            ApiException apiException
    ){
        log.error("", apiException);

        var errorCode = apiException.getErrorCodeIfs();

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(
                        Api.ERROR(errorCode, apiException.getErrorDescription())
                );

    }
}
