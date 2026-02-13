package com.example.Animalgram.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCodeIfs{

    PASSWORD_NOT_MATCH(400 , 1401 , "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(404 , 1404 , "사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS(409, 1409, "이미 사용 중인 아이디입니다."),
    MEMBER_ALREADY_REGISTERED(409, 1410, "이미 등록된 사용자입니다."),
    DUPLICATE_EMAIL(409, 1411, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(409, 1412, "이미 사용 중인 닉네임입니다."),
    DUPLICATE_TEL_NUMBER(409, 1413, "이미 사용 중인 전화번호입니다.")
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
