package com.example.Animalgram.common.exception;


import com.example.Animalgram.common.error.ErrorCodeIfs;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionIfs{

    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;


    public ApiException(ErrorCodeIfs errorCodeIfs) {
        super(errorCodeIfs.getDescription()); //RuntimeException(부모) 한테 내가 정의한 메시지를 보내버린다
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getDescription();
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, String errorDescription){
        super(errorDescription); //RuntimeException(부모) 한테 내가 정의한 메시지를 보내버린다
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, Throwable tx){
        super(tx); //RuntimeException(부모) 한테 내가 정의한 메시지를 보내버린다
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getDescription();
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, Throwable tx, String errorDescription){
        super(tx); //RuntimeException(부모) 한테 내가 정의한 메시지를 보내버린다
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorDescription;
    }
}
