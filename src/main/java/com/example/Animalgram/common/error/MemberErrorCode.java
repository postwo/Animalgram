package com.example.Animalgram.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeIfs{

    PASSWORD_NOT_MATCH(400 , 1401 , "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(400 , 1404 , "사용자를 찾을 수 없음."),
    MEMBER_ALREADY_EXISTS(409, 1409, "이미 사용 중인 아이디입니다."),
    MEMBER_ALREADY_REGISTERED(410, 1410, "이미 등록된 사용자입니다. 가입할 수 없습니다.")

    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
